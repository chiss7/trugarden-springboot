package com.chis.trugarden.application.stock.service;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.application.stock.abstractions.StockReservationRepository;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
import com.chis.trugarden.domain.stock.StockReservation;
import com.chis.trugarden.domain.stock.StockReservationErrors;
import com.chis.trugarden.shared.enums.ReservationStatus;
import com.chis.trugarden.shared.properties.StockProperties;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockReservationService {

    private final StockReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final StockProperties stockProperties;

    /**
     * Crea reservas de stock para los items del carrito.
     * NO decrementa el stock real, solo crea reservas temporales.
     */
    @Transactional
    public Result<List<StockReservation>> createReservations(Set<CartItem> items, Long orderId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("createReservations must be called within a transaction");
        }

        // Ordenar por productId para evitar deadlocks
        List<CartItem> sortedItems = items.stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(stockProperties.getReservationTtlMinutes());
        List<StockReservation> reservations = new ArrayList<>();

        for (CartItem item : sortedItems) {
            Long productId = item.getProduct().getId();
            
            // Obtener producto con lock
            Optional<Product> productOpt = productRepository.findByIdWithLock(productId);
            if (productOpt.isEmpty()) {
                log.error("Product not found: {}", productId);
                return Result.failure(ProductErrors.notFound(productId));
            }

            Product product = productOpt.get();
            
            // Calcular stock disponible (físico - reservas activas)
            int reservedQuantity = reservationRepository.sumActiveReservationsByProductId(productId);
            int availableStock = (int) product.getStock() - reservedQuantity;

            log.debug("Product {}: physical stock={}, reserved={}, available={}, requested={}", 
                    product.getName(), product.getStock(), reservedQuantity, availableStock, item.getQuantity());

            // Validar disponibilidad
            if (availableStock < item.getQuantity()) {
                log.warn("Insufficient available stock for product: {} — available: {}, requested: {}",
                        product.getName(), availableStock, item.getQuantity());
                return Result.failure(StockReservationErrors.insufficientStock(
                        product.getName(), 
                        item.getQuantity(), 
                        availableStock
                ));
            }

            // Crear reserva (NO tocar el stock físico)
            StockReservation reservation = StockReservation.ofNew(
                    productId,
                    product.getName(),
                    orderId,
                    item.getQuantity(),
                    expiresAt
            );

            reservations.add(reservation);
        }

        // Guardar todas las reservas
        List<StockReservation> savedReservations = reservationRepository.saveAll(reservations);
        
        log.info("Created {} stock reservations for order {} with TTL of {} minutes", 
                savedReservations.size(), orderId, stockProperties.getReservationTtlMinutes());

        return Result.success(savedReservations);
    }

    /**
     * Confirma las reservas y DECREMENTA el stock real del producto.
     * Se llama cuando el pago es exitoso.
     */
    @Transactional
    public Result<Void> confirmReservations(Long orderId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("confirmReservations must be called within a transaction");
        }

        // Obtener reservas activas con lock
        List<StockReservation> activeReservations = reservationRepository.findActiveByOrderIdWithLock(orderId);

        if (activeReservations.isEmpty()) {
            log.warn("No active reservations found for order: {}", orderId);
            return Result.failure(StockReservationErrors.reservationNotFound(orderId));
        }

        // Ordenar por productId para evitar deadlocks
        List<StockReservation> sortedReservations = activeReservations.stream()
                .sorted(Comparator.comparing(StockReservation::getProductId))
                .toList();

        List<Product> productsToUpdate = new ArrayList<>();
        List<StockReservation> reservationsToUpdate = new ArrayList<>();

        for (StockReservation reservation : sortedReservations) {
            // Obtener producto con lock
            Optional<Product> productOpt = productRepository.findByIdWithLock(reservation.getProductId());
            if (productOpt.isEmpty()) {
                log.error("Product not found when confirming reservation: {}", reservation.getProductId());
                return Result.failure(ProductErrors.notFound(reservation.getProductId()));
            }

            Product product = productOpt.get();

            // AQUÍ SÍ DECREMENTAMOS EL STOCK REAL
            Product updatedProduct = product.withStock(product.getStock() - reservation.getQuantity());
            productsToUpdate.add(updatedProduct);

            // Cambiar status de la reserva a CONFIRMED
            StockReservation confirmedReservation = reservation.withStatus(ReservationStatus.CONFIRMED);
            reservationsToUpdate.add(confirmedReservation);

            log.debug("Confirming reservation for product {}: stock {} -> {}", 
                    product.getName(), product.getStock(), updatedProduct.getStock());
        }

        // Guardar productos actualizados
        productRepository.saveAll(productsToUpdate);

        // Guardar reservas confirmadas
        reservationRepository.saveAll(reservationsToUpdate);

        log.info("Confirmed {} reservations for order {}. Stock decremented successfully.", 
                reservationsToUpdate.size(), orderId);

        return Result.success(null);
    }

    /**
     * Cancela las reservas sin tocar el stock físico (porque nunca se decrementó).
     * Se llama cuando el pago es cancelado.
     */
    @Transactional
    public Result<Void> cancelReservations(Long orderId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("cancelReservations must be called within a transaction");
        }

        List<StockReservation> activeReservations = reservationRepository.findByOrderIdAndStatus(
                orderId, 
                ReservationStatus.ACTIVE
        );

        if (activeReservations.isEmpty()) {
            log.warn("No active reservations found to cancel for order: {}", orderId);
            return Result.success(null); // No es error, simplemente no hay nada que cancelar
        }

        // Cambiar status a CANCELLED (NO tocar stock físico)
        List<StockReservation> cancelledReservations = activeReservations.stream()
                .map(r -> r.withStatus(ReservationStatus.CANCELLED))
                .collect(Collectors.toList());

        reservationRepository.saveAll(cancelledReservations);

        log.info("Cancelled {} reservations for order {}. No stock changes needed.", 
                cancelledReservations.size(), orderId);

        return Result.success(null);
    }

    /**
     * Expira las reservas sin tocar el stock físico.
     * Se llama desde el job programado cuando el TTL expira.
     */
    @Transactional
    public Result<Void> expireReservations(List<StockReservation> reservations) {
        if (reservations.isEmpty()) {
            return Result.success(null);
        }

        // Cambiar status a EXPIRED (NO tocar stock físico)
        List<StockReservation> expiredReservations = reservations.stream()
                .map(r -> r.withStatus(ReservationStatus.EXPIRED))
                .collect(Collectors.toList());

        reservationRepository.saveAll(expiredReservations);

        log.info("Expired {} reservations. No stock changes needed.", expiredReservations.size());

        return Result.success(null);
    }

    /**
     * Calcula el stock disponible para un producto (stock físico - reservas activas).
     */
    public int getAvailableStock(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return 0;
        }

        Product product = productOpt.get();
        int reservedQuantity = reservationRepository.sumActiveReservationsByProductId(productId);
        
        return Math.max(0, (int) product.getStock() - reservedQuantity);
    }

    /**
     * Verifica qué items del carrito tienen stock insuficiente considerando las reservas activas.
     */
    public Set<CartItem> getItemsWithInsufficientStock(Set<CartItem> items) {
        return items.stream()
                .filter(item -> {
                    int availableStock = getAvailableStock(item.getProduct().getId());
                    boolean insufficient = item.getQuantity() > availableStock;
                    
                    if (insufficient) {
                        log.debug("Item {} has insufficient stock: requested={}, available={}", 
                                item.getProduct().getName(), item.getQuantity(), availableStock);
                    }
                    
                    return insufficient;
                })
                .collect(Collectors.toSet());
    }
}

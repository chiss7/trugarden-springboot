package com.chis.trugarden.application.stock.job;

import com.chis.trugarden.application.order.abstractions.OrderRepository;
import com.chis.trugarden.application.stock.abstractions.StockReservationRepository;
import com.chis.trugarden.application.stock.service.StockReservationService;
import com.chis.trugarden.domain.order.Order;
import com.chis.trugarden.domain.stock.StockReservation;
import com.chis.trugarden.shared.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockReservationExpirationJob {

    private final StockReservationRepository reservationRepository;
    private final StockReservationService stockReservationService;
    private final OrderRepository orderRepository;

    /**
     * Job programado que se ejecuta periódicamente para expirar reservas vencidas.
     * Por defecto se ejecuta cada 1 minuto.
     */
    @Scheduled(fixedRateString = "${trugarden.stock.expiration-check-interval-ms:60000}")
    @Transactional
    public void expireReservations() {
        log.debug("Running stock reservation expiration job...");

        LocalDateTime now = LocalDateTime.now();
        List<StockReservation> expiredReservations = reservationRepository.findExpiredReservations(now);

        if (expiredReservations.isEmpty()) {
            log.debug("No expired reservations found.");
            return;
        }

        log.info("Found {} expired reservations to process", expiredReservations.size());

        // Agrupar reservas por orderId
        Map<Long, List<StockReservation>> reservationsByOrder = expiredReservations.stream()
                .collect(Collectors.groupingBy(StockReservation::getOrderId));

        int expiredOrdersCount = 0;

        for (Map.Entry<Long, List<StockReservation>> entry : reservationsByOrder.entrySet()) {
            Long orderId = entry.getKey();
            List<StockReservation> reservations = entry.getValue();

            try {
                // Buscar la orden
                Order order = orderRepository.findById(orderId).orElse(null);

                if (order == null) {
                    log.warn("Order {} not found for expired reservations. Expiring reservations anyway.", orderId);
                    stockReservationService.expireReservations(reservations);
                    continue;
                }

                // Solo marcar orden como EXPIRED si está en estado PENDING
                if (order.getOrderStatus() == OrderStatus.PENDING) {
                    Order expiredOrder = order.withStatus(OrderStatus.EXPIRED);
                    orderRepository.save(expiredOrder);
                    expiredOrdersCount++;
                    
                    log.info("Order {} marked as EXPIRED due to reservation timeout", orderId);
                }

                // Expirar las reservas (no toca stock físico)
                stockReservationService.expireReservations(reservations);

            } catch (Exception e) {
                log.error("Error processing expired reservations for order {}: {}", orderId, e.getMessage(), e);
            }
        }

        log.info("Stock reservation expiration job completed. Expired {} reservations across {} orders.",
                expiredReservations.size(), expiredOrdersCount);
    }
}

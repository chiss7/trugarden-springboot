package com.chis.trugarden.application.product;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.cart.CartItem;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    public Result<Void> deductStock(Set<CartItem> items) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("deductStock must be called within a transaction");
        }

        List<CartItem> sortedItems = items.stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        List<Product> productsToUpdate = new ArrayList<>();
        for (CartItem item : sortedItems) {
            Optional<Product> productOpt = productRepository
                    .findByIdWithLock(item.getProduct().getId());

            if (productOpt.isEmpty()) {
                return Result.failure(ProductErrors.notFound(item.getProduct().getId()));
            }

            Product product = productOpt.get();

            if (product.getStock() < item.getQuantity()) {
                log.warn("Insufficient stock for product: {} — available: {}, requested: {}",
                        product.getName(), product.getStock(), item.getQuantity());
                return Result.failure(ProductErrors.insufficientStock(product.getName()));
            }

            Product updated = product.withStock(product.getStock() - item.getQuantity());
            productsToUpdate.add(updated);
        }

        productRepository.saveAll(productsToUpdate);

        return Result.success(null);
    }
}

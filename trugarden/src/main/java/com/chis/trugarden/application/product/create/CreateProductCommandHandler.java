package com.chis.trugarden.application.product.create;

import com.chis.trugarden.application.category.abstractions.CategoryRepository;
import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.domain.category.CategoryErrors;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductCommandHandler {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @CommandHandler
    @Transactional
    public Result<Long> handle(CreateProductCommand command) {
        log.info("Creando producto: {}", command.name());
        Optional<Category> categoryOpt = categoryRepository.findByCode(command.category());
        if (categoryOpt.isEmpty()) {
            return Result.failure(CategoryErrors.notFound(command.category()));
        }

        Product savedProduct = saveProduct(command, categoryOpt.get());
        log.info("Producto {} creado exitosamente.", savedProduct.getId());
        return Result.success(savedProduct.getId());
    }

    private Product saveProduct(CreateProductCommand command, Category category) {
        Product product = Product.of(
                command.name(),
                command.slug(),
                command.description(),
                command.mrpPrice(),
                command.sellingPrice(),
                command.imageUrls(),
                command.hasIva(),
                command.ivaPercentage(),
                0,
                command.stock(),
                category
        );
        return productRepository.save(product);
    }
}

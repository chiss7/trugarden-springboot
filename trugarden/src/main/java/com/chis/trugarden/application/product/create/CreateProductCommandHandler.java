package com.chis.trugarden.application.product.create;

import com.chis.trugarden.application.category.abstractions.CategoryRepository;
import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.application.storage.service.StorageService;
import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.domain.category.CategoryErrors;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
import com.chis.trugarden.shared.enums.ProductType;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductCommandHandler {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;

    @CommandHandler
    @Transactional
    public Result<Long> handle(CreateProductCommand command) {
        log.info("Creando producto: {}", command.getName());
        Optional<Category> categoryOpt = categoryRepository.findByCode(command.getCategory());
        if (categoryOpt.isEmpty()) {
            return Result.failure(CategoryErrors.notFound(command.getCategory()));
        }

        Optional<Product> existingProductOpt = productRepository.findBySlug(command.getSlug());
        if (existingProductOpt.isPresent()) {
            return Result.failure(ProductErrors.slugAlreadyExists(command.getSlug()));
        }

        // uploading images
        if (command.getImages().isEmpty()) {
            return Result.failure(ProductErrors.noImagesProvided());
        }
        List<String> imageUrls = storageService.uploadProductImages(command.getImages());

        Product savedProduct = saveProduct(command, categoryOpt.get(), imageUrls);
        log.info("Producto {} creado exitosamente.", savedProduct.getId());
        return Result.success(savedProduct.getId());
    }

    private Product saveProduct(CreateProductCommand command, Category category, List<String> imageUrls) {
        Product product = Product.of(
                command.getName(),
                command.getSlug(),
                command.getDescription(),
                command.getOriginalPrice(),
                command.getUnitPrice(),
                imageUrls,
                command.isHasIva(),
                command.getIvaPercentage(),
                0,
                command.getStock(),
                command.getLeadTimeMinDays(),
                command.getLeadTimeMaxDays(),
                command.getProductType() != null ? command.getProductType() : ProductType.STOCK,
                category
        );
        return productRepository.save(product);
    }
}

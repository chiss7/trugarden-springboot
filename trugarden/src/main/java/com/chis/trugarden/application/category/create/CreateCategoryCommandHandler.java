package com.chis.trugarden.application.category.create;

import com.chis.trugarden.application.category.abstractions.CategoryRepository;
import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCategoryCommandHandler {
    private final CategoryRepository categoryRepository;
    @CommandHandler
    public Result<Long> handle(CreateCategoryCommand command) {
        try {
            log.info("Creando categoría padre {} con hijos {}, {}", command.category(), command.category2(), command.category3());
            Optional<Category> categoryOpt = categoryRepository.findByCode(command.category());
            Category parentCategory;
            if (categoryOpt.isEmpty()) {
                Category category = Category.of(command.name(), command.category(), null, 1);
                parentCategory = categoryRepository.save(category);
                log.info("Categoría padre {} creada exitosamente.", category.getCategoryCode());
            } else {
                parentCategory = categoryOpt.get();
                log.info("Categoría padre {} ya existe, se usará la existente.", parentCategory.getCategoryCode());
            }

            if (command.category2() == null) {
                return Result.success(parentCategory.getId());
            }

            Optional<Category> categoryOpt2 = categoryRepository.findByCode(command.category2());
            Category secondLevelCategory;
            if (categoryOpt2.isEmpty()) {
                Category category = Category.of(command.name2(), command.category2(), parentCategory, 2);
                secondLevelCategory = categoryRepository.save(category);
                log.info("Categoría {} creada exitosamente.", category.getCategoryCode());
            } else {
                secondLevelCategory = categoryOpt2.get();
                log.info("Categoría {} ya existe, se usará la existente.", secondLevelCategory.getCategoryCode());
            }

            if (command.category3() == null) {
                return Result.success(secondLevelCategory.getId());
            }

            Optional<Category> categoryOpt3 = categoryRepository.findByCode(command.category3());
            Category thirdLevelCategory;
            if (categoryOpt3.isEmpty()) {
                Category category = Category.of(command.name3(), command.category3(), secondLevelCategory, 3);
                thirdLevelCategory = categoryRepository.save(category);
                log.info("Categoría {} creada exitosamente.", category.getCategoryCode());
            } else {
                thirdLevelCategory = categoryOpt3.get();
                log.info("Categoría {} ya existe, se usará la existente.", thirdLevelCategory.getCategoryCode());
            }

            return Result.success(thirdLevelCategory.getId());
        } catch (Exception e) {
            log.error("Error al crear la categoría: {}", e.getMessage());
            return Result.failure(Error.failure("CREATE_CATEGORY_ERROR", "Ha ocurrido un error al crear la categoría. Vuelve a intentarlo más tarde."));
        }
    }
}

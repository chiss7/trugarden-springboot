package com.chis.trugarden.application.storage.service;

import com.chis.trugarden.application.storage.factory.StorageStrategyFactory;
import com.chis.trugarden.application.storage.strategy.StorageStrategy;
import com.chis.trugarden.shared.enums.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${storage.active-provider}")
    private StorageProvider activeProvider;

    private final StorageStrategyFactory storageStrategyFactory;

    public List<String> uploadProductImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadProductImage)
                .toList();
    }

    public String uploadProductImage(MultipartFile file) {
        StorageStrategy strategy = storageStrategyFactory.getStrategy(activeProvider);
        try {
            String extension = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + extension;

            return strategy.upload(
                    "products",
                    filename,
                    file.getBytes(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen", e);
        }
    }

    public void deleteImage(String imageUrl) {
        StorageStrategy strategy = storageStrategyFactory.getStrategy(activeProvider);
        strategy.delete(imageUrl);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}

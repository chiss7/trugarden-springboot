package com.chis.trugarden.application.storage.strategy;

import com.chis.trugarden.shared.enums.StorageProvider;

public interface StorageStrategy {
    String upload(String folder, String filename, byte[] content, String contentType);
    void delete(String fileUrl);
    StorageProvider provider();
}

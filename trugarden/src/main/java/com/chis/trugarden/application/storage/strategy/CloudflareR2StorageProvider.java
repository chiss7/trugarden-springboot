package com.chis.trugarden.application.storage.strategy;

import com.chis.trugarden.shared.enums.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CloudflareR2StorageProvider implements StorageStrategy {

    @Value("${storage.r2.access-key-id}")
    private String accessKeyId;

    @Value("${storage.r2.secret-access-key}")
    private String secretAccessKey;

    @Value("${storage.r2.endpoint}")
    private String endpoint;

    @Value("${storage.r2.bucket}")
    private String bucket;

    @Value("${storage.r2.public-url}")
    private String publicUrl;

    private S3Client buildClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .region(Region.of("auto"))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Override
    public String upload(String folder, String filename, byte[] content, String contentType) {
        String key = folder + "/" + filename;

        try (S3Client s3 = buildClient()) {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(contentType)
                            .contentLength((long) content.length)
                            .build(),
                    RequestBody.fromBytes(content)
            );
        }

        return publicUrl + "/" + key;
    }

    @Override
    public void delete(String fileUrl) {
        String key = fileUrl.replace(publicUrl + "/", "");

        try (S3Client s3 = buildClient()) {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        }
    }

    @Override
    public StorageProvider provider() {
        return StorageProvider.CLOUDFLARE;
    }
}

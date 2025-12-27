package nextpos.app.nextpos.service.storage.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.storage.StorageProperties;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.model.enums.StorageProvider;
import nextpos.app.nextpos.service.storage.StorageContext;
import nextpos.app.nextpos.service.storage.StorageService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GcsStorageService implements StorageService {

    private final StorageProperties storageProperties;
    private final Storage storage;
    private final Tika tika;

    @Autowired
    public GcsStorageService(StorageProperties storageProperties) throws IOException {
        this.storageProperties = storageProperties;
        this.tika = new Tika();

        // Check if GCS is enabled
        if (!storageProperties.getGcs().isEnabled()) {
            log.info("GCS storage is disabled. Skipping initialization.");
            this.storage = null;
            return;
        }

        // Validate required properties
        validateGcsConfiguration();

        try {
            GoogleCredentials credentials;
            if (StringUtils.hasText(storageProperties.getGcs().getCredentialsJson())) {
                credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(storageProperties.getGcs().getCredentialsJson().getBytes()));
            } else {
                // Use default application credentials
                credentials = GoogleCredentials.getApplicationDefault();
            }

            this.storage = StorageOptions.newBuilder()
                    .setProjectId(storageProperties.getGcs().getProjectId())
                    .setCredentials(credentials)
                    .build()
                    .getService();

            // Ensure bucket exists
            ensureBucketExists();
            log.info("GCS storage service initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize GCS storage service", e);
            throw new RuntimeException("GCS storage initialization failed", e);
        }
    }

    private void validateGcsConfiguration() {
        StorageProperties.Gcs gcsConfig = storageProperties.getGcs();

        if (!StringUtils.hasText(gcsConfig.getProjectId())) {
            throw new IllegalArgumentException("GCS project ID is required when GCS is enabled");
        }

        if (!StringUtils.hasText(gcsConfig.getBucket())) {
            throw new IllegalArgumentException("GCS bucket name is required when GCS is enabled");
        }

        // Either credentials JSON or default credentials must be available
        if (!StringUtils.hasText(gcsConfig.getCredentialsJson())) {
            log.warn("GCS credentials JSON not provided. Will try to use default application credentials.");
        }
    }

    private void ensureBucketExists() {
        if (storage == null) {
            return;
        }

        try {
            Bucket bucket = storage.get(storageProperties.getGcs().getBucket());
            if (bucket == null) {
                log.info("Creating GCS bucket: {}", storageProperties.getGcs().getBucket());
                storage.create(BucketInfo.of(storageProperties.getGcs().getBucket()));
            }
        } catch (Exception e) {
            log.error("Failed to create GCS bucket", e);
            throw new RuntimeException("GCS bucket initialization failed", e);
        }
    }

    @Override
    public Media store(MultipartFile file, StorageContext context) throws IOException {
        if (storage == null) {
            throw new IllegalStateException("GCS storage is not enabled");
        }

        validateFile(file);

        String mimeType = tika.detect(file.getInputStream());
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String blobName = generateBlobName(context, extension);

        BlobId blobId = BlobId.of(storageProperties.getGcs().getBucket(), blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType)
                .setMetadata(Map.of(
                        "original-filename", file.getOriginalFilename(),
                        "company-id", String.valueOf(context.getCompanyId()),
                        "user-id", String.valueOf(context.getUserId())))
                .build();

        storage.create(blobInfo, file.getBytes());

        log.debug("File uploaded to GCS: {}", blobName);

        return Media.builder()
                .originalFilename(file.getOriginalFilename())
                .storedFilename(blobName.substring(blobName.lastIndexOf('/') + 1))
                .filePath(blobName)
                .fileSize(file.getSize())
                .mimeType(mimeType)
                .extension(extension)
                .storageProvider(StorageProvider.GCS.name())
                .storagePath(blobName)
                .isPublic(context.isPublic())
                .metadata(extractMetadata(file, mimeType))
                .entityType(context.getEntityType())
                .entityId(context.getEntityId())
                .companyId(context.getCompanyId())
                .warehouseId(context.getWarehouseId())
                .uploadedBy(context.getUserId())
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String getPublicUrl(Media media) {
        if (storage == null) {
            throw new IllegalStateException("GCS storage is not enabled");
        }

        if (StringUtils.hasText(storageProperties.getGcs().getCdnUrl())) {
            return storageProperties.getGcs().getCdnUrl() + "/" + media.getStoragePath();
        }

        return String.format("https://storage.googleapis.com/%s/%s",
                storageProperties.getGcs().getBucket(),
                media.getStoragePath());
    }

    @Override
    public String getSignedUrl(Media media, long expiryMinutes) {
        if (storage == null) {
            throw new IllegalStateException("GCS storage is not enabled");
        }

        BlobId blobId = BlobId.of(storageProperties.getGcs().getBucket(), media.getStoragePath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        URL signedUrl = storage.signUrl(
                blobInfo,
                expiryMinutes,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }

    private String generateBlobName(StorageContext context, String extension) {
        String path = context.getStoragePath();
        String filename = UUID.randomUUID() + "." + extension;
        return path + filename;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            String mimeType = tika.detect(file.getInputStream());
            if (!storageProperties.getAllowedContentTypes().contains(mimeType)) {
                throw new RuntimeException("File type not allowed: " + mimeType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to validate file", e);
        }
    }

    private String extractMetadata(MultipartFile file, String mimeType) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("detectedMimeType", mimeType);
        metadata.put("fileSize", file.getSize());

        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(metadata);
        } catch (Exception e) {
            return "{}";
        }
    }

    @Override
    public Media store(InputStream inputStream, String filename, String contentType, StorageContext context)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Resource load(Media media) throws IOException {
        throw new UnsupportedOperationException("Use getPublicUrl or getSignedUrl for GCS");
    }

    @Override
    public void delete(Media media) throws IOException {
        if (storage == null) {
            throw new IllegalStateException("GCS storage is not enabled");
        }

        BlobId blobId = BlobId.of(storageProperties.getGcs().getBucket(), media.getStoragePath());
        storage.delete(blobId);
        log.debug("File deleted from GCS: {}", media.getStoragePath());
    }

    @Override
    public Media copy(Media source, StorageContext newContext) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean exists(Media media) {
        if (storage == null) {
            return false;
        }

        BlobId blobId = BlobId.of(storageProperties.getGcs().getBucket(), media.getStoragePath());
        Blob blob = storage.get(blobId);
        return blob != null && blob.exists();
    }

    @Override
    public String getProviderName() {
        return StorageProvider.GCS.name();
    }
}
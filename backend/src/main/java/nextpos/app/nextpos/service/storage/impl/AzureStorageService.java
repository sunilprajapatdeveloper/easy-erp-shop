package nextpos.app.nextpos.service.storage.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AzureStorageService implements StorageService {

    private final StorageProperties storageProperties;
    private final BlobContainerClient blobContainerClient;
    private final Tika tika;

    @Autowired
    public AzureStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.tika = new Tika();

        if (!storageProperties.getAzure().isEnabled()) {
            this.blobContainerClient = null;
            return;
        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(storageProperties.getAzure().getConnectionString())
                .buildClient();

        this.blobContainerClient = blobServiceClient.getBlobContainerClient(
                storageProperties.getAzure().getContainer());

        // Create container if it doesn't exist
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
            log.info("Azure container created: {}", storageProperties.getAzure().getContainer());
        }
    }

    @Override
    public Media store(MultipartFile file, StorageContext context) throws IOException {
        validateFile(file);

        String mimeType = tika.detect(file.getInputStream());
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String blobName = generateBlobName(context, extension);

        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        // Set content type
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(mimeType)
                .setContentDisposition("inline; filename=\"" + file.getOriginalFilename() + "\"");

        // Upload blob
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);

        // Set metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("originalFilename", file.getOriginalFilename());
        metadata.put("companyId", String.valueOf(context.getCompanyId()));
        metadata.put("userId", String.valueOf(context.getUserId()));
        blobClient.setMetadata(metadata);

        log.debug("File uploaded to Azure: {}", blobName);

        return Media.builder()
                .originalFilename(file.getOriginalFilename())
                .storedFilename(blobName.substring(blobName.lastIndexOf('/') + 1))
                .filePath(blobName)
                .fileSize(file.getSize())
                .mimeType(mimeType)
                .extension(extension)
                .storageProvider(StorageProvider.AZURE.name())
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
        if (StringUtils.hasText(storageProperties.getAzure().getCdnUrl())) {
            return storageProperties.getAzure().getCdnUrl() + "/" + media.getStoragePath();
        }

        BlobClient blobClient = blobContainerClient.getBlobClient(media.getStoragePath());
        return blobClient.getBlobUrl();
    }

    @Override
    public String getSignedUrl(Media media, long expiryMinutes) {
        BlobClient blobClient = blobContainerClient.getBlobClient(media.getStoragePath());

        BlobSasPermission permissions = new BlobSasPermission()
                .setReadPermission(true);

        OffsetDateTime expiryTime = OffsetDateTime.now().plusMinutes(expiryMinutes);

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                expiryTime, permissions);

        String sasToken = blobClient.generateSas(sasValues);
        return blobClient.getBlobUrl() + "?" + sasToken;
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
        throw new UnsupportedOperationException("Use getPublicUrl or getSignedUrl for Azure");
    }

    @Override
    public void delete(Media media) throws IOException {
        BlobClient blobClient = blobContainerClient.getBlobClient(media.getStoragePath());
        blobClient.delete();
        log.debug("File deleted from Azure: {}", media.getStoragePath());
    }

    @Override
    public Media copy(Media source, StorageContext newContext) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean exists(Media media) {
        BlobClient blobClient = blobContainerClient.getBlobClient(media.getStoragePath());
        return blobClient.exists();
    }

    @Override
    public String getProviderName() {
        return StorageProvider.AZURE.name();
    }
}
package nextpos.app.nextpos.service.storage.impl;

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
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class S3StorageService implements StorageService {

    private final StorageProperties storageProperties;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final Tika tika;

    @Autowired
    public S3StorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.tika = new Tika();

        // Check if S3 is enabled
        if (!storageProperties.getS3().isEnabled()) {
            log.info("S3 storage is disabled. Skipping initialization.");
            this.s3Client = null;
            this.s3Presigner = null;
            return;
        }

        // Validate required properties
        validateS3Configuration();

        try {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    storageProperties.getS3().getAccessKey(),
                    storageProperties.getS3().getSecretKey());

            S3ClientBuilder builder = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(Region.of(storageProperties.getS3().getRegion()));

            // Add custom endpoint if provided (for S3-compatible services)
            if (StringUtils.hasText(storageProperties.getS3().getEndpoint())) {
                builder.endpointOverride(java.net.URI.create(storageProperties.getS3().getEndpoint()));
            }

            this.s3Client = builder.build();
            this.s3Presigner = S3Presigner.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(Region.of(storageProperties.getS3().getRegion()))
                    .build();

            createBucketIfNotExists();
            log.info("S3 storage service initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize S3 storage service", e);
            throw new RuntimeException("S3 storage initialization failed", e);
        }
    }

    private void validateS3Configuration() {
        StorageProperties.S3 s3Config = storageProperties.getS3();

        if (!StringUtils.hasText(s3Config.getAccessKey())) {
            throw new IllegalArgumentException("S3 access key is required when S3 is enabled");
        }

        if (!StringUtils.hasText(s3Config.getSecretKey())) {
            throw new IllegalArgumentException("S3 secret key is required when S3 is enabled");
        }

        if (!StringUtils.hasText(s3Config.getBucket())) {
            throw new IllegalArgumentException("S3 bucket name is required when S3 is enabled");
        }

        if (!StringUtils.hasText(s3Config.getRegion())) {
            throw new IllegalArgumentException("S3 region is required when S3 is enabled");
        }
    }

    private void createBucketIfNotExists() {
        if (s3Client == null) {
            return;
        }

        try {
            String bucket = storageProperties.getS3().getBucket();
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build();

            s3Client.headBucket(headBucketRequest);
            log.info("S3 bucket exists: {}", bucket);
        } catch (NoSuchBucketException e) {
            log.info("Creating S3 bucket: {}", storageProperties.getS3().getBucket());
            try {
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                        .bucket(storageProperties.getS3().getBucket())
                        .build();
                s3Client.createBucket(createBucketRequest);
                log.info("S3 bucket created successfully");
            } catch (Exception ex) {
                log.error("Failed to create S3 bucket", ex);
                throw new RuntimeException("Failed to create S3 bucket", ex);
            }
        } catch (Exception e) {
            log.error("Failed to check S3 bucket existence", e);
            throw new RuntimeException("S3 bucket check failed", e);
        }
    }

    @Override
    public Media store(MultipartFile file, StorageContext context) throws IOException {
        if (s3Client == null) {
            throw new IllegalStateException("S3 storage is not enabled");
        }

        validateFile(file);

        String mimeType = tika.detect(file.getInputStream());
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = generateKey(context, extension);

        // Upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(storageProperties.getS3().getBucket())
                .key(key)
                .contentType(mimeType)
                .contentLength(file.getSize())
                .metadata(Map.of(
                        "original-filename", file.getOriginalFilename(),
                        "company-id", String.valueOf(context.getCompanyId()),
                        "uploaded-by", String.valueOf(context.getUserId())))
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        log.debug("File uploaded to S3: {}", key);

        return Media.builder()
                .originalFilename(file.getOriginalFilename())
                .storedFilename(key.substring(key.lastIndexOf('/') + 1))
                .filePath(key)
                .fileSize(file.getSize())
                .mimeType(mimeType)
                .extension(extension)
                .storageProvider(StorageProvider.S3.name())
                .storagePath(key)
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
        if (s3Client == null) {
            throw new IllegalStateException("S3 storage is not enabled");
        }

        if (StringUtils.hasText(storageProperties.getS3().getCdnUrl())) {
            return storageProperties.getS3().getCdnUrl() + "/" + media.getStoragePath();
        }

        // Generate S3 URL
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                storageProperties.getS3().getBucket(),
                storageProperties.getS3().getRegion(),
                media.getStoragePath());
    }

    @Override
    public String getSignedUrl(Media media, long expiryMinutes) {
        if (s3Client == null || s3Presigner == null) {
            throw new IllegalStateException("S3 storage is not enabled");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(storageProperties.getS3().getBucket())
                .key(media.getStoragePath())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expiryMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private String generateKey(StorageContext context, String extension) {
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
        throw new UnsupportedOperationException("Use getPublicUrl or getSignedUrl for S3");
    }

    @Override
    public void delete(Media media) throws IOException {
        if (s3Client == null) {
            throw new IllegalStateException("S3 storage is not enabled");
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(storageProperties.getS3().getBucket())
                .key(media.getStoragePath())
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.debug("File deleted from S3: {}", media.getStoragePath());
    }

    @Override
    public Media copy(Media source, StorageContext newContext) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean exists(Media media) {
        if (s3Client == null) {
            return false;
        }

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(storageProperties.getS3().getBucket())
                    .key(media.getStoragePath())
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.error("Failed to check if file exists in S3", e);
            return false;
        }
    }

    @Override
    public String getProviderName() {
        return StorageProvider.S3.name();
    }
}
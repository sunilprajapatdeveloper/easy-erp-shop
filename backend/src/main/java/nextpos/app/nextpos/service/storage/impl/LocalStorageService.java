package nextpos.app.nextpos.service.storage.impl;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.storage.StorageProperties;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.model.enums.StorageProvider;
import nextpos.app.nextpos.service.storage.StorageContext;
import nextpos.app.nextpos.service.storage.StorageService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class LocalStorageService implements StorageService {

    private final StorageProperties storageProperties;
    private final Tika tika;

    @Autowired
    public LocalStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.tika = new Tika();
        initStorageDirectory();
    }

    private void initStorageDirectory() {
        try {
            Path storagePath = Paths.get(storageProperties.getLocal().getBasePath());
            Files.createDirectories(storagePath);
            log.info("Local storage initialized at: {}", storagePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to initialize local storage directory", e);
            throw new RuntimeException("Storage directory initialization failed", e);
        }
    }

    @Override
    public Media store(MultipartFile file, StorageContext context) throws IOException {
        // Validate file
        validateFile(file);

        // Detect mime type
        String mimeType = tika.detect(file.getInputStream());
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // Generate storage path
        String storagePath = context.getStoragePath();
        String filename = UUID.randomUUID() + "." + extension;
        Path destination = Paths.get(storageProperties.getLocal().getBasePath(),
                storagePath, filename);

        // Create directories
        Files.createDirectories(destination.getParent());

        // Copy file
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Generate thumbnail if needed
        String thumbnailPath = null;
        if (context.isGenerateThumbnail() && isImage(mimeType)) {
            thumbnailPath = generateThumbnail(destination, storagePath, filename);
        }

        // Build Media entity
        return Media.builder()
                .originalFilename(file.getOriginalFilename())
                .storedFilename(filename)
                .filePath(destination.toString())
                .fileSize(file.getSize())
                .mimeType(mimeType)
                .extension(extension)
                .storageProvider(StorageProvider.LOCAL.name())
                .storagePath(storagePath + filename)
                .isPublic(context.isPublic())
                .metadata(extractMetadata(destination, mimeType))
                .thumbnailPath(thumbnailPath)
                .entityType(context.getEntityType())
                .entityId(context.getEntityId())
                .companyId(context.getCompanyId())
                .warehouseId(context.getWarehouseId())
                .uploadedBy(context.getUserId())
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Media store(InputStream inputStream, String filename, String contentType,
            StorageContext context) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Resource load(Media media) throws IOException {
        Path filePath = Paths.get(media.getFilePath());
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + media.getFilePath());
        }

        // Update access time
        media.setAccessedAt(LocalDateTime.now());

        return new FileSystemResource(filePath);
    }

    @Override
    public String getPublicUrl(Media media) {
        String baseUrl = storageProperties.getLocal().getPublicUrlPrefix();
        
        // Clean up any double slashes
        baseUrl = baseUrl.replaceAll("(?<!:)//", "/");
        
        // Remove trailing slash if present
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        // Generate the correct URL that matches your controller endpoint
        // The endpoint is: @GetMapping("/local/{companyId}/{filename}")
        // So the URL should be: /api/v1/media/local/{companyId}/{filename}
        return baseUrl + "/api/v1/media/local/" + media.getCompanyId() + "/" + media.getStoredFilename();
    }

    @Override
    public String getSignedUrl(Media media, long expiryMinutes) {
        // Local storage doesn't need signed URLs for public files
        return getPublicUrl(media);
    }

    @Override
    public void delete(Media media) throws IOException {
        Path filePath = Paths.get(media.getFilePath());
        Files.deleteIfExists(filePath);

        // Delete thumbnail if exists
        if (media.getThumbnailPath() != null) {
            Path thumbnailPath = Paths.get(media.getThumbnailPath());
            Files.deleteIfExists(thumbnailPath);
        }
    }

    @Override
    public Media copy(Media source, StorageContext newContext) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean exists(Media media) {
        return Files.exists(Paths.get(media.getFilePath()));
    }

    @Override
    public String getProviderName() {
        return StorageProvider.LOCAL.name();
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

    private boolean isImage(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }

    private String generateThumbnail(Path source, String storagePath, String filename) throws IOException {
        String thumbnailFilename = "thumb_" + filename;
        Path thumbnailPath = Paths.get(storageProperties.getLocal().getBasePath(),
                storagePath, "thumbnails", thumbnailFilename);

        Files.createDirectories(thumbnailPath.getParent());

        Thumbnails.of(source.toFile())
                .size(storageProperties.getThumbnail().getWidth(),
                        storageProperties.getThumbnail().getHeight())
                .outputQuality((float) storageProperties.getThumbnail().getQuality())
                .toFile(thumbnailPath.toFile());

        return thumbnailPath.toString();
    }

    private String extractMetadata(Path filePath, String mimeType) throws IOException {
        // Extract metadata based on file type
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("detectedMimeType", mimeType);
        metadata.put("fileSize", Files.size(filePath));

        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(metadata);
        } catch (Exception e) {
            return "{}";
        }
    }

    public Resource loadThumbnail(Media media) throws IOException {
        if (media.getThumbnailPath() == null) {
            throw new IOException("Thumbnail not available for media: " + media.getId());
        }

        Path thumbnailPath = Paths.get(media.getThumbnailPath());
        if (!Files.exists(thumbnailPath)) {
            // Try to generate thumbnail if it doesn't exist
            try {
                generateThumbnailForExistingFile(media);
            } catch (Exception e) {
                log.warn("Failed to generate thumbnail, falling back to original: {}", media.getId(), e);
                // Fall back to original file
                return load(media);
            }

            // Check again after generation
            if (!Files.exists(thumbnailPath)) {
                throw new IOException("Thumbnail not found: " + thumbnailPath);
            }
        }

        return new FileSystemResource(thumbnailPath);
    }

    // Add this helper method to generate thumbnail for existing files
    private void generateThumbnailForExistingFile(Media media) throws IOException {
        Path sourcePath = Paths.get(media.getFilePath());

        if (!Files.exists(sourcePath)) {
            throw new IOException("Source file not found: " + media.getFilePath());
        }

        // Extract storage path from media
        String storagePath = media.getStoragePath();
        String filename = media.getStoredFilename();

        // Generate thumbnail
        String thumbnailPath = generateThumbnail(sourcePath, storagePath, filename);

        // Update media entity with thumbnail path
        media.setThumbnailPath(thumbnailPath);
    }
}
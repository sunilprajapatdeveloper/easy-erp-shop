package nextpos.app.nextpos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.storage.StorageContext;
import nextpos.app.nextpos.service.storage.StorageService;
import nextpos.app.nextpos.service.storage.StorageServiceFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final StorageServiceFactory storageServiceFactory;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request, Long userId) throws IOException {
        validateUploadRequest(request, userId);

        StorageContext context = StorageContext.builder()
                .companyId(request.getCompanyId())
                .warehouseId(request.getWarehouseId())
                .userId(userId)
                .mediaType(request.getMediaType())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .isPublic(request.isPublic())
                .generateThumbnail(request.isGenerateThumbnail())
                .metadata(request.getMetadata())
                .build();

        StorageService storageService = storageServiceFactory.getStorageService();
        Media media = storageService.store(file, context);

        // Save to database
        media = mediaRepository.save(media);

        log.info("Media uploaded: {} for company {}", media.getId(), request.getCompanyId());
        return convertToResponse(media);
    }

    @Override
    @Transactional
    public List<MediaResponse> uploadFiles(List<MultipartFile> files, MediaUploadRequest request, Long userId)
            throws IOException {
        validateUploadRequest(request, userId);

        List<CompletableFuture<Media>> uploadFutures = new ArrayList<>();
        StorageService storageService = storageServiceFactory.getStorageService();

        for (MultipartFile file : files) {
            StorageContext context = StorageContext.builder()
                    .companyId(request.getCompanyId())
                    .warehouseId(request.getWarehouseId())
                    .userId(userId)
                    .mediaType(request.getMediaType())
                    .entityType(request.getEntityType())
                    .entityId(request.getEntityId())
                    .isPublic(request.isPublic())
                    .generateThumbnail(request.isGenerateThumbnail())
                    .metadata(request.getMetadata())
                    .build();

            CompletableFuture<Media> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return storageService.store(file, context);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                }
            });

            uploadFutures.add(future);
        }

        // Wait for all uploads to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                uploadFutures.toArray(new CompletableFuture[0]));

        allFutures.join();

        // Collect results and save to database
        List<Media> mediaList = uploadFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        mediaList = mediaRepository.saveAll(mediaList);

        log.info("{} files uploaded for company {}", mediaList.size(), request.getCompanyId());
        return mediaList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "media", key = "#mediaId + '_' + #companyId")
    public MediaResponse getMedia(String mediaId, Long companyId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        // Update accessed at
        media.setAccessedAt(LocalDateTime.now());
        mediaRepository.save(media);

        return convertToResponse(media);
    }

    @Override
    @Cacheable(value = "entity-media", key = "#companyId + '_' + #entityType + '_' + #entityId")
    public List<MediaResponse> getMediaByEntity(Long companyId, String entityType, Long entityId) {
        List<Media> mediaList = mediaRepository.findByCompanyIdAndEntityTypeAndEntityId(
                companyId, entityType, entityId);

        return mediaList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MediaResponse> getCompanyMedia(Long companyId, Pageable pageable) {
        return mediaRepository.findByCompanyId(companyId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Cacheable(value = "media-url", key = "#mediaId + '_' + #companyId + '_public'")
    public String getPublicUrl(String mediaId, Long companyId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        return storageService.getPublicUrl(media);
    }

    @Override
    @Cacheable(value = "media-url", key = "#mediaId + '_' + #companyId + '_signed_' + #expiryMinutes")
    public String getSignedUrl(String mediaId, Long companyId, long expiryMinutes) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        return storageService.getSignedUrl(media, expiryMinutes);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "media", "entity-media", "media-url" }, allEntries = true)
    public void deleteMedia(String mediaId, Long companyId, Long userId) throws IOException {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        // Soft delete in database
        media.setDeletedAt(LocalDateTime.now());
        media.setUpdatedBy(userId);
        mediaRepository.save(media);

        // Delete from storage (optional - could keep for backup)
        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        storageService.delete(media);

        log.info("Media deleted: {} by user {}", mediaId, userId);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "entity-media", "media-url" }, allEntries = true)
    public void deleteMediaByEntity(Long companyId, String entityType, Long entityId, Long userId) throws IOException {
        List<Media> mediaList = mediaRepository.findByCompanyIdAndEntityTypeAndEntityId(
                companyId, entityType, entityId);

        for (Media media : mediaList) {
            media.setDeletedAt(LocalDateTime.now());
            media.setUpdatedBy(userId);

            StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
            storageService.delete(media);
        }

        mediaRepository.saveAll(mediaList);
        log.info("Deleted {} media files for {}/{}", mediaList.size(), entityType, entityId);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "media", "entity-media" }, allEntries = true)
    public MediaResponse moveMedia(String mediaId, String newEntityType, Long newEntityId,
            Long companyId, Long userId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        media.setEntityType(newEntityType);
        media.setEntityId(newEntityId);
        media.setUpdatedBy(userId);

        media = mediaRepository.save(media);

        return convertToResponse(media);
    }

    @Override
    @Transactional
    public Map<String, Object> getStorageUsage(Long companyId) {
        Long totalSize = mediaRepository.getTotalStorageUsedByCompany(companyId);
        totalSize = totalSize != null ? totalSize : 0L;

        // Get recently accessed files
        Pageable recentPageable = org.springframework.data.domain.PageRequest.of(0, 10);
        List<Media> recentFiles = mediaRepository.findRecentlyAccessed(companyId, recentPageable);

        Map<String, Object> usage = new HashMap<>();
        usage.put("totalSizeBytes", totalSize);
        usage.put("totalSizeMB", totalSize / (1024 * 1024));
        usage.put("totalFiles", mediaRepository.countByCompanyId(companyId));
        usage.put("recentFiles", recentFiles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));

        return usage;
    }

    // @Override
    // @Async
    // @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    // public void cleanupTempFiles() {
    // LocalDateTime expiryTime = LocalDateTime.now().minusDays(1);

    // // Get all companies with temp files
    // List<Media> tempFiles = mediaRepository.findTempFilesExpired(expiryTime);

    // for (Media media : tempFiles) {
    // try {
    // StorageService storageService =
    // storageServiceFactory.getStorageService(media.getStorageProvider());
    // storageService.delete(media);
    // mediaRepository.delete(media);
    // log.debug("Cleaned up temp file: {}", media.getId());
    // } catch (Exception e) {
    // log.error("Failed to cleanup temp file: {}", media.getId(), e);
    // }
    // }

    // log.info("Cleaned up {} temporary files", tempFiles.size());
    // }

    @Override
    public List<MediaResponse> copyMedia(List<String> mediaIds, String newEntityType, Long newEntityId,
            Long companyId, Long userId) throws IOException {
        List<MediaResponse> copiedMedia = new ArrayList<>();
        for (String mediaId : mediaIds) {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

            validateCompanyAccess(media, companyId);

            // Create a copy (simplified - just linking to new entity)
            Media copy = Media.builder()
                    .originalFilename(media.getOriginalFilename())
                    .storedFilename(media.getStoredFilename())
                    .filePath(media.getFilePath())
                    .fileSize(media.getFileSize())
                    .mimeType(media.getMimeType())
                    .extension(media.getExtension())
                    .storageProvider(media.getStorageProvider())
                    .storagePath(media.getStoragePath())
                    .isPublic(media.getIsPublic())
                    .metadata(media.getMetadata())
                    .thumbnailPath(media.getThumbnailPath())
                    .entityType(newEntityType)
                    .entityId(newEntityId)
                    .companyId(companyId)
                    .warehouseId(media.getWarehouseId())
                    .uploadedBy(userId)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            copy = mediaRepository.save(copy);
            copiedMedia.add(convertToResponse(copy));
        }

        return copiedMedia;
    }

    @Override
    public MediaResponse updateMediaMetadata(String mediaId, Map<String, Object> metadata,
            Long companyId, Long userId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);

        try {
            media.setMetadata(objectMapper.writeValueAsString(metadata));
            media.setUpdatedBy(userId);
            media = mediaRepository.save(media);

            return convertToResponse(media);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update metadata", e);
        }
    }

    private void validateUploadRequest(MediaUploadRequest request, Long userId) {
        if (request.getCompanyId() == null) {
            throw new RuntimeException("Company ID is required");
        }

        if (request.getEntityType() == null || request.getEntityId() == null) {
            throw new RuntimeException("Entity type and ID are required");
        }

        // Additional validations based on your business rules
    }

    private void validateCompanyAccess(Media media, Long companyId) {
        if (!media.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Access denied to media");
        }
    }

    private MediaResponse convertToResponse(Media media) {
        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        String url = media.getIsPublic() ? storageService.getPublicUrl(media) : storageService.getSignedUrl(media, 60); // 60
                                                                                                                        // minutes
                                                                                                                        // expiry
                                                                                                                        // for
                                                                                                                        // private
                                                                                                                        // files

        String thumbnailUrl = null;
        if (media.getThumbnailPath() != null) {
            // For local storage, we might need a different approach
            // For cloud storage, we can append a query parameter or use a different path
            thumbnailUrl = url + "?thumb=true";
        }

        return MediaResponse.builder()
                .id(media.getId())
                .originalFilename(media.getOriginalFilename())
                .storedFilename(media.getStoredFilename())
                .url(url)
                .thumbnailUrl(thumbnailUrl)
                .fileSize(media.getFileSize())
                .mimeType(media.getMimeType())
                .extension(media.getExtension())
                .storageProvider(media.getStorageProvider())
                .isPublic(media.getIsPublic())
                .entityType(media.getEntityType())
                .entityId(media.getEntityId())
                .companyId(media.getCompanyId())
                .warehouseId(media.getWarehouseId())
                .uploadedBy(media.getUploadedBy())
                .uploadedAt(media.getUploadedAt())
                .createdAt(media.getCreatedAt())
                .updatedAt(media.getUpdatedAt())
                .metadata(media.getMetadata())
                .build();
    }

    @Override
    public Map<Long, List<MediaResponse>> getMediaForEntities(Long companyId, String entityType, List<Long> entityIds) {
        if (entityIds == null || entityIds.isEmpty()) {
            return new HashMap<>();
        }

        try {
            // Get media for all entities in one query
            List<Media> mediaList = mediaRepository.findByCompanyIdAndEntityTypeAndEntityIds(
                    companyId, entityType, entityIds);

            // Group by entity ID
            return mediaList.stream()
                    .collect(Collectors.groupingBy(
                            Media::getEntityId,
                            Collectors.mapping(this::convertToResponse, Collectors.toList())));
        } catch (Exception e) {
            log.error("Failed to get media for multiple entities", e);
            return new HashMap<>();
        }
    }

    @Override
    public Resource loadMediaResource(String filename, Long companyId, boolean thumbnail) throws IOException {
        // Find media by filename and companyId
        List<Media> mediaList = mediaRepository.findByCompanyIdAndStoredFilename(companyId, filename);

        if (mediaList.isEmpty()) {
            throw new IOException("Media not found with filename: " + filename);
        }

        Media media = mediaList.get(0); // Assuming filename is unique per company
        return loadMediaResourceInternal(media, thumbnail);
    }

    @Override
    public Resource loadMediaResourceById(String mediaId, Long companyId, boolean thumbnail) throws IOException {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new IOException("Media not found: " + mediaId));

        validateCompanyAccess(media, companyId);
        return loadMediaResourceInternal(media, thumbnail);
    }

    private Resource loadMediaResourceInternal(Media media, boolean thumbnail) throws IOException {
        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());

        // For local storage and thumbnails
        if (thumbnail && media.getThumbnailPath() != null) {
            // Check if it's local storage
            if (storageService instanceof nextpos.app.nextpos.service.storage.impl.LocalStorageService) {
                nextpos.app.nextpos.service.storage.impl.LocalStorageService localService = (nextpos.app.nextpos.service.storage.impl.LocalStorageService) storageService;

                try {
                    // Try to load thumbnail
                    return loadThumbnailFromLocalStorage(media, localService);
                } catch (Exception e) {
                    log.warn("Failed to load thumbnail, falling back to original: {}", media.getId(), e);
                    // Fall back to original file
                    return storageService.load(media);
                }
            }
        }

        // Load original file
        return storageService.load(media);
    }

    private Resource loadThumbnailFromLocalStorage(Media media,
            nextpos.app.nextpos.service.storage.impl.LocalStorageService localService) throws IOException {
        // Use reflection to call the loadThumbnail method if it exists
        try {
            java.lang.reflect.Method method = localService.getClass()
                    .getMethod("loadThumbnail", Media.class);
            return (Resource) method.invoke(localService, media);
        } catch (Exception e) {
            throw new IOException("Failed to load thumbnail", e);
        }
    }
}
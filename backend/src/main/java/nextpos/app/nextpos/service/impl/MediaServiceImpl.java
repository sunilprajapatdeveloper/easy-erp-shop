package nextpos.app.nextpos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.storage.StorageContext;
import nextpos.app.nextpos.service.storage.StorageService;
import nextpos.app.nextpos.service.storage.StorageServiceFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final WarehouseAccessService warehouseAccessService;

    @Override
    @Transactional
    public MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request) throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        validateWarehouseRequest(request);

        validateUploadRequest(request, companyId, userId);

        StorageContext context = StorageContext.builder()
                .companyId(companyId)
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

        log.info("Media uploaded: {} for company {}", media.getId(), companyId);
        return convertToResponse(media);
    }

    @Override
    @Transactional
    public MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request, Long userId, Long companyId)
            throws IOException {
        validateUploadRequest(request, companyId, userId);

        StorageContext context = StorageContext.builder()
                .companyId(companyId)
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

        log.info("Media uploaded: {} for company {}", media.getId(), companyId);
        return convertToResponse(media);
    }

    @Override
    @Transactional
    public List<MediaResponse> uploadFiles(List<MultipartFile> files, MediaUploadRequest request) throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        validateWarehouseRequest(request);

        validateUploadRequest(request, companyId, userId);

        List<CompletableFuture<Media>> uploadFutures = new ArrayList<>();
        StorageService storageService = storageServiceFactory.getStorageService();

        for (MultipartFile file : files) {
            StorageContext context = StorageContext.builder()
                    .companyId(companyId)
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

        log.info("{} files uploaded for company {}", mediaList.size(), companyId);
        return mediaList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "media", key = "#mediaId")
    public MediaResponse getMedia(String mediaId) {
        Long companyId = UserContext.getCurrentCompanyId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

        // Update accessed at
        media.setAccessedAt(LocalDateTime.now());
        mediaRepository.save(media);

        return convertToResponse(media);
    }

    @Override
    @Cacheable(value = "entity-media", key = "T(nextpos.app.nextpos.security.context.UserContext).getCurrentCompanyId() + '_' + #entityType + '_' + #entityId")
    public List<MediaResponse> getMediaByEntity(String entityType, Long entityId) {
        Long companyId = UserContext.getCurrentCompanyId();

        List<Media> mediaList = mediaRepository.findByCompanyIdAndEntityTypeAndEntityId(
                companyId, entityType, entityId);

        var accessibleWarehouses = warehouseAccessService.accessibleWarehouses().stream()
                .map(warehouse -> warehouse.getId()).collect(Collectors.toSet());
        return mediaList.stream()
                .filter(media -> media.getWarehouseId() == null || accessibleWarehouses.contains(media.getWarehouseId()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MediaResponse> getCompanyMedia(Pageable pageable) {
        Long companyId = UserContext.getCurrentCompanyId();

        return mediaRepository.findByCompanyId(companyId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Cacheable(value = "media-url", key = "#mediaId + '_public'")
    public String getPublicUrl(String mediaId) {
        Long companyId = UserContext.getCurrentCompanyId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        return storageService.getPublicUrl(media);
    }

    @Override
    @Cacheable(value = "media-url", key = "#mediaId + '_signed_' + #expiryMinutes")
    public String getSignedUrl(String mediaId, long expiryMinutes) {
        Long companyId = UserContext.getCurrentCompanyId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

        StorageService storageService = storageServiceFactory.getStorageService(media.getStorageProvider());
        return storageService.getSignedUrl(media, expiryMinutes);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "media", "entity-media", "media-url" }, allEntries = true)
    public void deleteMedia(String mediaId) throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

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
    public void deleteMediaByEntity(String entityType, Long entityId) throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

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
    public MediaResponse moveMedia(String mediaId, String newEntityType, Long newEntityId) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

        media.setEntityType(newEntityType);
        media.setEntityId(newEntityId);
        media.setUpdatedBy(userId);

        media = mediaRepository.save(media);

        return convertToResponse(media);
    }

    @Override
    @Transactional
    public Map<String, Object> getStorageUsage() {
        Long companyId = UserContext.getCurrentCompanyId();

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

    @Override
    public List<MediaResponse> copyMedia(List<String> mediaIds, String newEntityType, Long newEntityId)
            throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        List<MediaResponse> copiedMedia = new ArrayList<>();
        for (String mediaId : mediaIds) {
            Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                    .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

            validateWarehouseAccess(media);

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
    public MediaResponse updateMediaMetadata(String mediaId, Map<String, Object> metadata) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new RuntimeException("Media not found: " + mediaId));

        validateWarehouseAccess(media);

        try {
            media.setMetadata(objectMapper.writeValueAsString(metadata));
            media.setUpdatedBy(userId);
            media = mediaRepository.save(media);

            return convertToResponse(media);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update metadata", e);
        }
    }

    private void validateUploadRequest(MediaUploadRequest request, Long companyId, Long userId) {
        if (request.getCompanyId() != null && !request.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Company ID in request does not match authenticated user");
        }
        if (request.getEntityType() == null || request.getEntityId() == null) {
            throw new RuntimeException("Entity type and ID are required");
        }
    }

    private void validateWarehouseRequest(MediaUploadRequest request) {
        if (request.getWarehouseId() != null) {
            warehouseAccessService.requireAccessible(request.getWarehouseId());
        }
    }

    private void validateWarehouseAccess(Media media) {
        if (media.getWarehouseId() != null) {
            warehouseAccessService.requireAccessible(media.getWarehouseId());
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
    public Map<Long, List<MediaResponse>> getMediaForEntities(String entityType, List<Long> entityIds) {
        Long companyId = UserContext.getCurrentCompanyId();
        return getMediaForEntities(entityType, entityIds, companyId);
    }

    @Override
    public Map<Long, List<MediaResponse>> getMediaForEntities(String entityType, List<Long> entityIds, Long companyId) {
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
            log.error("Failed to get media for multiple entities with companyId {}", companyId, e);
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
    public Resource loadMediaResourceById(String mediaId, boolean thumbnail) throws IOException {
        Long companyId = UserContext.getCurrentCompanyId();

        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new IOException("Media not found: " + mediaId));

        validateWarehouseAccess(media);
        return loadMediaResourceInternal(media, thumbnail);
    }

    @Override
    public Resource loadMediaResourceById(String mediaId, boolean thumbnail, Long companyId) throws IOException {
        Media media = mediaRepository.findByIdAndCompanyId(mediaId, companyId)
                .orElseThrow(() -> new IOException("Media not found: " + mediaId));
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

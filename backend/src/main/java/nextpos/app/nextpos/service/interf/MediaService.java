package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MediaService {

        MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request, Long userId) throws IOException;

        List<MediaResponse> uploadFiles(List<MultipartFile> files, MediaUploadRequest request, Long userId)
                        throws IOException;

        MediaResponse getMedia(String mediaId, Long companyId);

        List<MediaResponse> getMediaByEntity(Long companyId, String entityType, Long entityId);

        Page<MediaResponse> getCompanyMedia(Long companyId, Pageable pageable);

        String getPublicUrl(String mediaId, Long companyId);

        String getSignedUrl(String mediaId, Long companyId, long expiryMinutes);

        void deleteMedia(String mediaId, Long companyId, Long userId) throws IOException;

        void deleteMediaByEntity(Long companyId, String entityType, Long entityId, Long userId) throws IOException;

        MediaResponse moveMedia(String mediaId, String newEntityType, Long newEntityId, Long companyId, Long userId);

        List<MediaResponse> copyMedia(List<String> mediaIds, String newEntityType, Long newEntityId,
                        Long companyId, Long userId) throws IOException;

        Map<String, Object> getStorageUsage(Long companyId);

        // void cleanupTempFiles();

        MediaResponse updateMediaMetadata(String mediaId, Map<String, Object> metadata,
                        Long companyId, Long userId);

        Map<Long, List<MediaResponse>> getMediaForEntities(Long companyId, String entityType, List<Long> entityIds);

        // In MediaService interface
        Resource loadMediaResource(String filename, Long companyId, boolean thumbnail) throws IOException;

        Resource loadMediaResourceById(String mediaId, Long companyId, boolean thumbnail) throws IOException;
}
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

        MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request) throws IOException;

        MediaResponse uploadFile(MultipartFile file, MediaUploadRequest request, Long userId, Long companyId) throws IOException;

        List<MediaResponse> uploadFiles(List<MultipartFile> files, MediaUploadRequest request) throws IOException;

        MediaResponse getMedia(String mediaId);

        List<MediaResponse> getMediaByEntity(String entityType, Long entityId);

        Page<MediaResponse> getCompanyMedia(Pageable pageable);

        String getPublicUrl(String mediaId);

        String getSignedUrl(String mediaId, long expiryMinutes);

        void deleteMedia(String mediaId) throws IOException;

        void deleteMediaByEntity(String entityType, Long entityId) throws IOException;

        MediaResponse moveMedia(String mediaId, String newEntityType, Long newEntityId);

        List<MediaResponse> copyMedia(List<String> mediaIds, String newEntityType, Long newEntityId) throws IOException;

        Map<String, Object> getStorageUsage();

        MediaResponse updateMediaMetadata(String mediaId, Map<String, Object> metadata);

        Map<Long, List<MediaResponse>> getMediaForEntities(String entityType, List<Long> entityIds);

        Resource loadMediaResource(String filename, Long companyId, boolean thumbnail) throws IOException;

        Resource loadMediaResourceById(String mediaId, boolean thumbnail) throws IOException;
}
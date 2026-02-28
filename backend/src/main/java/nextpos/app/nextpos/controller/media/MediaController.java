package nextpos.app.nextpos.controller.media;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.MediaUploadRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.service.interf.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute @Valid MediaUploadRequest request) throws IOException {

        MediaResponse response = mediaService.uploadFile(file, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaResponse>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @ModelAttribute @Valid MediaUploadRequest request) throws IOException {

        List<MediaResponse> responses = mediaService.uploadFiles(files, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaResponse> getMedia(@PathVariable String mediaId) {
        MediaResponse response = mediaService.getMedia(mediaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<MediaResponse>> getMediaByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {

        List<MediaResponse> responses = mediaService.getMediaByEntity(entityType, entityId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/company")
    public ResponseEntity<Page<MediaResponse>> getCompanyMedia(@PageableDefault(size = 20) Pageable pageable) {
        Page<MediaResponse> responses = mediaService.getCompanyMedia(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{mediaId}/url")
    public ResponseEntity<Map<String, String>> getMediaUrl(@PathVariable String mediaId) {
        String url = mediaService.getPublicUrl(mediaId);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/{mediaId}/signed-url")
    public ResponseEntity<Map<String, String>> getSignedUrl(
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "60") long expiryMinutes) {

        String url = mediaService.getSignedUrl(mediaId, expiryMinutes);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String mediaId) throws IOException {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<Void> deleteMediaByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) throws IOException {

        mediaService.deleteMediaByEntity(entityType, entityId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{mediaId}/move")
    public ResponseEntity<MediaResponse> moveMedia(
            @PathVariable String mediaId,
            @RequestParam String newEntityType,
            @RequestParam Long newEntityId) {

        MediaResponse response = mediaService.moveMedia(mediaId, newEntityType, newEntityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/storage/usage")
    public ResponseEntity<Map<String, Object>> getStorageUsage() {
        Map<String, Object> usage = mediaService.getStorageUsage();
        return ResponseEntity.ok(usage);
    }

    // Add this new endpoint to serve files
    @GetMapping("/local/{companyId}/{filename}")
    public ResponseEntity<Resource> serveLocalFile(
            @PathVariable Long companyId,
            @PathVariable String filename,
            @RequestParam(value = "thumb", required = false, defaultValue = "false") boolean thumb) {

        try {
            Resource resource = mediaService.loadMediaResource(filename, companyId, thumb);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IOException e) {
            // log.error("Failed to load file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Add this new endpoint to serve files by media ID
    @GetMapping("/{mediaId}/file")
    public ResponseEntity<Resource> serveFileByMediaId(
            @PathVariable String mediaId,
            @RequestParam(value = "thumb", required = false, defaultValue = "false") boolean thumb) {

        try {
            Resource resource = mediaService.loadMediaResourceById(mediaId, thumb);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Get media info to determine content type
            MediaResponse media = mediaService.getMedia(mediaId);
            String contentType = media.getMimeType();
            String filename = media.getOriginalFilename();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            // log.error("Failed to load file by media ID: {}", mediaId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Alternative: Direct file serving without media lookup
    @GetMapping("/serve/{companyId}/{entityType}/{entityId}/{filename}")
    public ResponseEntity<Resource> serveFileDirect(
            @PathVariable Long companyId,
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @PathVariable String filename,
            @RequestParam(value = "thumb", required = false, defaultValue = "false") boolean thumb) {

        try {
            // Get media by entity to find the correct file
            List<MediaResponse> mediaList = mediaService.getMediaByEntity(entityType, entityId);

            // Find media with matching filename
            MediaResponse targetMedia = mediaList.stream()
                    .filter(media -> media.getStoredFilename().equals(filename))
                    .findFirst()
                    .orElseThrow(() -> new IOException("File not found"));

            Resource resource = mediaService.loadMediaResourceById(targetMedia.getId(), thumb);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = targetMedia.getMimeType();
            String originalFilename = targetMedia.getOriginalFilename();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + originalFilename + "\"")
                    .body(resource);

        } catch (Exception e) {
            // log.error("Failed to load file: {}/{}/{}/{}", companyId, entityType,
            // entityId, filename, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String determineContentType(String filename) {
        if (filename == null) {
            return "application/octet-stream";
        }

        String extension = filename.toLowerCase();
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".pdf")) {
            return "application/pdf";
        } else if (extension.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }
}
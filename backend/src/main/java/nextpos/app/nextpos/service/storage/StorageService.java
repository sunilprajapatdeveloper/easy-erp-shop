package nextpos.app.nextpos.service.storage;

import nextpos.app.nextpos.model.entity.Media;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    /**
     * Store file with metadata
     */
    Media store(MultipartFile file, StorageContext context) throws IOException;

    /**
     * Store file from InputStream
     */
    Media store(InputStream inputStream, String filename, String contentType,
            StorageContext context) throws IOException;

    /**
     * Retrieve file as Resource
     */
    Resource load(Media media) throws IOException;

    /**
     * Get publicly accessible URL
     */
    String getPublicUrl(Media media);

    /**
     * Get signed URL for temporary access (for private files)
     */
    String getSignedUrl(Media media, long expiryMinutes);

    /**
     * Delete file from storage
     */
    void delete(Media media) throws IOException;

    /**
     * Copy file to another location
     */
    Media copy(Media source, StorageContext newContext) throws IOException;

    /**
     * Check if file exists
     */
    boolean exists(Media media);

    /**
     * Get storage provider name
     */
    String getProviderName();
}
package nextpos.app.nextpos.importexport.util;

import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import nextpos.app.nextpos.service.interf.MediaService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class MediaResource implements Resource {

    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final String mediaId;

    public MediaResource(MediaService mediaService, MediaRepository mediaRepository, String mediaId) {
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
        this.mediaId = mediaId;
    }

    @Override
    public boolean exists() {
        try {
            mediaService.loadMediaResourceById(mediaId, false).getInputStream().close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return mediaService.loadMediaResourceById(mediaId, false).getInputStream();
    }

    @Override
    public String getDescription() {
        return "MediaResource for media ID: " + mediaId;
    }

    @Override
    public long contentLength() throws IOException {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if (media != null && media.getFileSize() != null) {
            return media.getFileSize();
        }
        // fallback (inefficient)
        try (InputStream is = getInputStream()) {
            return is.available(); // not guaranteed for all streams
        }
    }

    @Override
    public java.io.File getFile() throws IOException {
        throw new UnsupportedOperationException("getFile not supported for media resources");
    }

    @Override
    public URL getURL() throws IOException {
        throw new UnsupportedOperationException("getURL not supported");
    }

    @Override
    public URI getURI() throws IOException {
        throw new UnsupportedOperationException("getURI not supported");
    }

    @Override
    public long lastModified() throws IOException {
        throw new UnsupportedOperationException("lastModified not supported");
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new UnsupportedOperationException("createRelative not supported");
    }

    @Override
    public String getFilename() {
        Media media = mediaRepository.findById(mediaId).orElse(null);
        return media != null ? media.getOriginalFilename() : null;
    }
}
package nextpos.app.nextpos.config.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String active;
    private String maxFileSize;
    private List<String> allowedContentTypes;
    private Thumbnail thumbnail;
    private Local local;
    private S3 s3;
    private Azure azure;
    private Gcs gcs;

    @Getter
    @Setter
    public static class Thumbnail {
        private boolean enabled;
        private int width;
        private int height;
        private double quality;
    }

    @Getter
    @Setter
    public static class Local {
        private String basePath;
        private String publicUrlPrefix;
    }

    @Getter
    @Setter
    public static class S3 {
        private boolean enabled;
        private String bucket;
        private String region;
        private String accessKey;
        private String secretKey;
        private String endpoint;
        private String cdnUrl;
    }

    @Getter
    @Setter
    public static class Azure {
        private boolean enabled;
        private String connectionString;
        private String container;
        private String cdnUrl;
    }

    @Getter
    @Setter
    public static class Gcs {
        private boolean enabled;
        private String projectId;
        private String credentialsJson;
        private String bucket;
        private String cdnUrl;
    }
}
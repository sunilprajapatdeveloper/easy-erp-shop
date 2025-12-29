package nextpos.app.nextpos.config.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String active = "LOCAL";
    private String maxFileSize = "5MB";

    private List<String> allowedContentTypes = new ArrayList<>();
    private Thumbnail thumbnail = new Thumbnail();
    private Local local = new Local();
    private S3 s3 = new S3();
    private Azure azure = new Azure();
    private Gcs gcs = new Gcs();

    @Getter
    @Setter
    public static class Thumbnail {
        private boolean enabled = false;
        private int width = 200;
        private int height = 200;
        private double quality = 0.8;
    }

    @Getter
    @Setter
    public static class Local {
        private String basePath = "/app/uploads";
        private String publicUrlPrefix;
    }

    @Getter
    @Setter
    public static class S3 {
        private boolean enabled = false;
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
        private boolean enabled = false;
        private String connectionString;
        private String container;
        private String cdnUrl;
    }

    @Getter
    @Setter
    public static class Gcs {
        private boolean enabled = false;
        private String projectId;
        private String credentialsJson;
        private String bucket;
        private String cdnUrl;
    }
}
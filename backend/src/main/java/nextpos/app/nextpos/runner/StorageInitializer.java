package nextpos.app.nextpos.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.storage.StorageProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
@RequiredArgsConstructor
public class StorageInitializer implements ApplicationRunner {

    private final StorageProperties storageProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing storage system...");

        // Create local storage directory if using local storage
        if ("local".equalsIgnoreCase(storageProperties.getActive())) {
            try {
                String basePath = storageProperties.getLocal().getBasePath();
                Files.createDirectories(Paths.get(basePath));
                log.info("Local storage directory created/verified: {}", basePath);
            } catch (Exception e) {
                log.error("Failed to create local storage directory", e);
            }
        }

        log.info("Storage system initialized with provider: {}", storageProperties.getActive());
    }
}
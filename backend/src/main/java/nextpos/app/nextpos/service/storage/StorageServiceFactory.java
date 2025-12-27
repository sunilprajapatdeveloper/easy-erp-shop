package nextpos.app.nextpos.service.storage;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.config.storage.StorageProperties;
import nextpos.app.nextpos.model.enums.StorageProvider;
import nextpos.app.nextpos.service.storage.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Component
public class StorageServiceFactory {

    private final Map<StorageProvider, StorageService> storageServices = new EnumMap<>(StorageProvider.class);
    private final StorageProperties storageProperties;

    @Autowired
    public StorageServiceFactory(
            StorageProperties storageProperties,
            LocalStorageService localStorageService,
            S3StorageService s3StorageService,
            AzureStorageService azureStorageService,
            GcsStorageService gcsStorageService) {
        this.storageProperties = storageProperties;

        storageServices.put(StorageProvider.LOCAL, localStorageService);

        if (storageProperties.getS3().isEnabled()) {
            storageServices.put(StorageProvider.S3, s3StorageService);
        }

        if (storageProperties.getAzure().isEnabled()) {
            storageServices.put(StorageProvider.AZURE, azureStorageService);
        }

        if (storageProperties.getGcs().isEnabled()) {
            storageServices.put(StorageProvider.GCS, gcsStorageService);
        }

        log.info("Storage services registered: {}", storageServices.keySet());
        log.info("Active storage provider: {}", getActiveProvider());
    }

    public StorageService getStorageService() {
        StorageProvider provider = getActiveProvider();
        return getStorageService(provider);
    }

    public StorageService getStorageService(StorageProvider provider) {
        StorageService service = storageServices.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("Storage provider not configured or enabled: " + provider);
        }
        return service;
    }

    public StorageService getStorageService(String providerName) {
        try {
            StorageProvider provider = StorageProvider.valueOf(providerName.toUpperCase());
            return getStorageService(provider);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported storage provider: " + providerName);
        }
    }

    public StorageProvider getActiveProvider() {
        try {
            return StorageProvider.valueOf(storageProperties.getActive().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid storage provider configured: {}. Defaulting to LOCAL.", storageProperties.getActive());
            return StorageProvider.LOCAL;
        }
    }
}
package nextpos.app.nextpos.security.integration;

import static org.assertj.core.api.Assertions.assertThat;

import nextpos.app.nextpos.model.entity.Media;
import nextpos.app.nextpos.repository.MediaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MediaTenantIsolationIntegrationTest {

    @Autowired
    private MediaRepository repository;

    @Test
    void sameIdentifierAndIndirectEntityReferenceCannotCrossCompanyBoundary() {
        Media tenantA = repository.saveAndFlush(media(10L, 100L, "tenant-a.pdf"));
        Media tenantB = repository.saveAndFlush(media(20L, 100L, "tenant-b.pdf"));

        assertThat(repository.findByIdAndCompanyId(tenantA.getId(), 20L)).isEmpty();
        assertThat(repository.findByIdAndCompanyId(tenantA.getId(), 10L)).contains(tenantA);
        assertThat(repository.findByCompanyIdAndEntityTypeAndEntityId(10L, "SALE", 100L))
                .extracting(Media::getId).containsExactly(tenantA.getId());
        assertThat(repository.findByCompanyIdAndEntityTypeAndEntityId(20L, "SALE", 100L))
                .extracting(Media::getId).containsExactly(tenantB.getId());
    }

    private Media media(Long companyId, Long entityId, String filename) {
        return Media.builder()
                .companyId(companyId)
                .entityType("SALE")
                .entityId(entityId)
                .originalFilename(filename)
                .storedFilename(filename)
                .filePath("/test/" + filename)
                .fileSize(10L)
                .mimeType("application/pdf")
                .extension("pdf")
                .storageProvider("LOCAL")
                .build();
    }
}

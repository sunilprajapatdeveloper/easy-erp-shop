package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Brand;
import nextpos.app.nextpos.model.enums.DefaultBrand;
import nextpos.app.nextpos.repository.BrandRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(3) // Adjust the order as needed
@RequiredArgsConstructor
public class BrandSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        for (DefaultBrand defaultBrand : DefaultBrand.values()) {
            // Check if brand already exists by name and company
            boolean exists = brandRepository.findByNameAndCompanyId(defaultBrand.getName(), defaultCompanyId)
                    .isPresent();

            if (!exists) {
                Brand brand = Brand.builder()
                        .name(defaultBrand.getName())
                        .description(defaultBrand.getDescription())
                        .image(defaultBrand.getImage())
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .updatedBy(defaultCreatedBy)
                        .updatedAt(now)
                        .build();

                brandRepository.save(brand);
                System.out.println("Default brand created: " + defaultBrand.getName());
            }
        }
    }
}

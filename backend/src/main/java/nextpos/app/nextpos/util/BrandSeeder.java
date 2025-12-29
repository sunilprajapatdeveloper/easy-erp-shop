package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Brand;
import nextpos.app.nextpos.model.enums.DefaultBrand;
import nextpos.app.nextpos.repository.BrandRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(5)
@RequiredArgsConstructor
public class BrandSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        List<DefaultBrand> brandEnums = Arrays.asList(
            DefaultBrand.GENERIC,
            DefaultBrand.NO_BRAND,
            DefaultBrand.UNBRANDED,
            DefaultBrand.HOUSE_BRAND
        );

        for (DefaultBrand brandEnum : brandEnums) {
            seedBrand(brandEnum, defaultCompanyId, defaultCreatedBy, now);
        }

        System.out.println("Brand seeding completed.");
    }

    private void seedBrand(DefaultBrand brandEnum, Long companyId, Long createdBy, LocalDateTime now) {
        String brandName = brandEnum.getDisplayName();
        
        // Check if brand already exists for this company
        boolean exists = brandRepository.existsByNameAndCompanyId(brandName, companyId);
        
        if (!exists) {
            Brand brand = Brand.builder()
                .name(brandName)
                .code(generateBrandCode(brandEnum))
                .description(getBrandDescription(brandEnum))
                .isActive(true)
                .companyId(companyId)
                .createdBy(createdBy)
                .createdAt(now)
                .build();

            brandRepository.save(brand);
            System.out.println("Created Brand: " + brandName + " for company " + companyId);
        }
    }

    private String generateBrandCode(DefaultBrand brandEnum) {
        switch (brandEnum) {
            case GENERIC:
                return "GEN";
            case NO_BRAND:
                return "NOBR";
            case UNBRANDED:
                return "UNBR";
            case HOUSE_BRAND:
                return "HOUSE";
            default:
                return brandEnum.name().substring(0, Math.min(4, brandEnum.name().length()));
        }
    }

    private String getBrandDescription(DefaultBrand brandEnum) {
        switch (brandEnum) {
            case GENERIC:
                return "Generic brand for standard products";
            case NO_BRAND:
                return "Products without specific brand";
            case UNBRANDED:
                return "Unbranded or white-label products";
            case HOUSE_BRAND:
                return "Store's own house brand";
            default:
                return brandEnum.getDisplayName() + " brand";
        }
    }
}
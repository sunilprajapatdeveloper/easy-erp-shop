package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.ProductUnit;
import nextpos.app.nextpos.model.enums.DefaultUnit;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(5) // Adjust the order as needed
@RequiredArgsConstructor
public class ProductUnitSeeder implements CommandLineRunner {

    private final ProductUnitRepository productUnitRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        for (DefaultUnit defaultUnit : DefaultUnit.values()) {
            // Check if unit already exists by name and company
            boolean exists = productUnitRepository.findByNameAndCompanyId(defaultUnit.getName(), defaultCompanyId)
                    .isPresent();

            if (!exists) {
                ProductUnit productUnit = ProductUnit.builder()
                        .name(defaultUnit.getName())
                        .shortName(defaultUnit.getShortName())
                        .baseUnit(defaultUnit.getBaseUnit())
                        .operator(defaultUnit.getOperator())
                        .operatorValue(defaultUnit.getOperatorValue())
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .updatedBy(defaultCreatedBy)
                        .updatedAt(now)
                        .build();

                productUnitRepository.save(productUnit);
                System.out.println(
                        "Default unit created: " + defaultUnit.getName() + " (" + defaultUnit.getShortName() + ")");
            }
        }
    }
}

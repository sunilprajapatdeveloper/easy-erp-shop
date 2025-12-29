package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.ProductUnit;
import nextpos.app.nextpos.model.enums.UnitType;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(3)
@RequiredArgsConstructor
public class ProductUnitSeeder implements CommandLineRunner {

    private final ProductUnitRepository productUnitRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        // Define default units to seed (excluding CUSTOM as it's for user-defined
        // units)
        List<UnitType> unitTypesToSeed = Arrays.asList(
                UnitType.PIECE,
                UnitType.KILOGRAM,
                UnitType.LITRE,
                UnitType.BOX,
                UnitType.PACK);

        for (UnitType unitType : unitTypesToSeed) {
            String unitName = formatUnitName(unitType);
            String abbreviation = getAbbreviation(unitType);

            // Check if unit already exists for this company
            boolean exists = productUnitRepository.existsByNameAndCompanyId(unitName, defaultCompanyId);

            if (!exists) {
                ProductUnit productUnit = ProductUnit.builder()
                        .name(unitName)
                        .abbreviation(abbreviation)
                        .unitType(unitType)
                        .isBaseUnit(true)
                        .conversionFactor(1.0)
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .build();

                productUnitRepository.save(productUnit);
                System.out.println("Created ProductUnit: " + unitName + " for company " + defaultCompanyId);
            }
        }

        System.out.println("ProductUnit seeding completed.");
    }

    private String formatUnitName(UnitType unitType) {
        switch (unitType) {
            case PIECE:
                return "Piece";
            case KILOGRAM:
                return "Kilogram";
            case LITRE:
                return "Litre";
            case BOX:
                return "Box";
            case PACK:
                return "Pack";
            default:
                return unitType.name();
        }
    }

    private String getAbbreviation(UnitType unitType) {
        switch (unitType) {
            case PIECE:
                return "pc";
            case KILOGRAM:
                return "kg";
            case LITRE:
                return "L";
            case BOX:
                return "box";
            case PACK:
                return "pack";
            default:
                return unitType.name().substring(0, 2).toLowerCase();
        }
    }
}
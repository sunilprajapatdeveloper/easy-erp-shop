package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.enums.DefaultCategory;
import nextpos.app.nextpos.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(4)
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTimeNow();

        // Seed essential categories first, then additional ones
        List<DefaultCategory> essentialCategories = Arrays.asList(
            DefaultCategory.UNCATEGORIZED,
            DefaultCategory.GENERAL
        );

        List<DefaultCategory> additionalCategories = Arrays.asList(
            DefaultCategory.FOOD,
            DefaultCategory.ELECTRONICS,
            DefaultCategory.CLOTHING,
            DefaultCategory.STATIONERY,
            DefaultCategory.HOUSEHOLD,
            DefaultCategory.TOILETRIES,
            DefaultCategory.BEVERAGES,
            DefaultCategory.SNACKS
        );

        // First seed essential categories
        for (DefaultCategory categoryEnum : essentialCategories) {
            seedCategory(categoryEnum, defaultCompanyId, defaultCreatedBy, now, true);
        }

        // Then seed additional categories
        for (DefaultCategory categoryEnum : additionalCategories) {
            seedCategory(categoryEnum, defaultCompanyId, defaultCreatedBy, now, false);
        }

        System.out.println("Category seeding completed.");
    }

    private void seedCategory(DefaultCategory categoryEnum, Long companyId, Long createdBy, 
                             LocalDateTime now, boolean isEssential) {
        String categoryName = categoryEnum.getDisplayName();
        
        // Check if category already exists for this company
        boolean exists = categoryRepository.existsByNameAndCompanyId(categoryName, companyId);
        
        if (!exists) {
            Category category = Category.builder()
                .name(categoryName)
                .code(generateCategoryCode(categoryEnum))
                .description(getCategoryDescription(categoryEnum))
                .isActive(true)
                .companyId(companyId)
                .createdBy(createdBy)
                .createdAt(now)
                .build();

            categoryRepository.save(category);
            System.out.println("Created Category: " + categoryName + 
                (isEssential ? " (Essential)" : "") + " for company " + companyId);
        }
    }

    private String generateCategoryCode(DefaultCategory categoryEnum) {
        switch (categoryEnum) {
            case UNCATEGORIZED:
                return "UNCAT";
            case GENERAL:
                return "GEN";
            case FOOD:
                return "FNB";
            case ELECTRONICS:
                return "ELEC";
            case CLOTHING:
                return "CLOTH";
            case STATIONERY:
                return "STAT";
            case HOUSEHOLD:
                return "HH";
            case TOILETRIES:
                return "TOIL";
            case BEVERAGES:
                return "BEV";
            case SNACKS:
                return "SNACK";
            default:
                return categoryEnum.name().substring(0, Math.min(5, categoryEnum.name().length()));
        }
    }

    private String getCategoryDescription(DefaultCategory categoryEnum) {
        switch (categoryEnum) {
            case UNCATEGORIZED:
                return "Default category for items without specific category";
            case GENERAL:
                return "General purpose category";
            case FOOD:
                return "Food and beverage products";
            case ELECTRONICS:
                return "Electronic items and gadgets";
            case CLOTHING:
                return "Clothing and apparel";
            case STATIONERY:
                return "Stationery and office supplies";
            case HOUSEHOLD:
                return "Household goods and supplies";
            case TOILETRIES:
                return "Personal care and toiletries";
            case BEVERAGES:
                return "Beverages and drinks";
            case SNACKS:
                return "Snacks and quick bites";
            default:
                return categoryEnum.getDisplayName() + " category";
        }
    }
}
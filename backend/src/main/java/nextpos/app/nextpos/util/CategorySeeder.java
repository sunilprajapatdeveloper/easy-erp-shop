package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.enums.DefaultCategory;
import nextpos.app.nextpos.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(4) // Adjust the order as needed
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        for (DefaultCategory defaultCategory : DefaultCategory.values()) {
            // Check if category already exists by code and company
            boolean exists = categoryRepository.findByCode(defaultCategory.getCode())
                    .stream()
                    .anyMatch(category -> category.getCompanyId().equals(defaultCompanyId));

            if (!exists) {
                Category category = Category.builder()
                        .name(defaultCategory.getName())
                        .code(defaultCategory.getCode())
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .updatedBy(defaultCreatedBy)
                        .updatedAt(now)
                        .build();

                categoryRepository.save(category);
                System.out.println("Default category created: " + defaultCategory.getName() + " ("
                        + defaultCategory.getCode() + ")");
            }
        }
    }
}
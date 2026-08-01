package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCategoryRequest;
import nextpos.app.nextpos.model.dto.response.CategoryResponse;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setCreatedBy(UserContext.getCurrentUserId());
        category.setCreatedAt(LocalDateTime.now());
        category.setCompanyId(UserContext.getCurrentCompanyId());

        return new CategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .map(CategoryResponse::new)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryResponse> findAllByCreatedBy() {
        Long userId = UserContext.getCurrentUserId();
        return categoryRepository.findAllByCreatedBy(userId).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByCompanyId(UserContext.getCurrentCompanyId()).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setUpdatedBy(UserContext.getCurrentUserId());
        category.setUpdatedAt(LocalDateTime.now());

        return new CategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}

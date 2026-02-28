package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCategoryRequest;
import nextpos.app.nextpos.model.dto.response.CategoryResponse;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // Get authenticated user using helper
        User createdBy = UserContext.getAuthenticatedUser(userRepository);

        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setCreatedBy(createdBy.getId());
        category.setCreatedAt(LocalDateTime.now());
        category.setCompanyId(createdBy.getCompanyId());

        return new CategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::new)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryResponse> findAllByCreatedBy() {
        User user = UserContext.getAuthenticatedUser(userRepository);
        return categoryRepository.findAllByCreatedBy(user.getId()).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Get authenticated user using helper
        User updatedBy = UserContext.getAuthenticatedUser(userRepository);

        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setUpdatedBy(updatedBy.getId());
        category.setUpdatedAt(LocalDateTime.now());

        return new CategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
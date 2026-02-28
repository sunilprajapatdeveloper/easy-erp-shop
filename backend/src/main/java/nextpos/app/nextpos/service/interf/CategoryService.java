package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateCategoryRequest;
import nextpos.app.nextpos.model.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> findAllByCreatedBy();
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long id, CreateCategoryRequest request);
    void deleteCategory(Long id);
}
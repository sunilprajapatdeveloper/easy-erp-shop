package nextpos.app.nextpos.service.interf;

import java.util.List;

import nextpos.app.nextpos.model.dto.request.CreateBrandRequest;
import nextpos.app.nextpos.model.dto.response.BrandResponse;

public interface BrandService {
    BrandResponse createBrand(CreateBrandRequest request);
    BrandResponse getBrandById(Long id);
    List<BrandResponse> findAllByCreatedBy(Long id);
    List<BrandResponse> getAllBrands();
    BrandResponse updateBrand(Long id, CreateBrandRequest request);
    void deleteBrand(Long id);
}

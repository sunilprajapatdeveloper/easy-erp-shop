package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateBrandRequest;
import nextpos.app.nextpos.model.dto.response.BrandResponse;
import nextpos.app.nextpos.model.entity.Brand;
import nextpos.app.nextpos.repository.BrandRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.BrandService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setImage(request.getImage());
        brand.setCreatedBy(UserContext.getCurrentUserId());
        brand.setCreatedAt(LocalDateTime.now());
        brand.setCompanyId(UserContext.getCurrentCompanyId());

        return new BrandResponse(brandRepository.save(brand));
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(BrandResponse::new)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Override
    public List<BrandResponse> findAllByCreatedBy() {
        Long userId = UserContext.getCurrentUserId();
        return brandRepository.findAllByCreatedBy(userId).stream()
                .map(BrandResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(BrandResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BrandResponse updateBrand(Long id, CreateBrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setImage(request.getImage());
        brand.setUpdatedBy(UserContext.getCurrentUserId());
        brand.setUpdatedAt(LocalDateTime.now());

        return new BrandResponse(brandRepository.save(brand));
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
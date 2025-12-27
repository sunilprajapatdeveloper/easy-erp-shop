package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateBrandRequest;
import nextpos.app.nextpos.model.dto.response.BrandResponse;
import nextpos.app.nextpos.model.entity.Brand;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.BrandRepository;
import nextpos.app.nextpos.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        // Get authenticated user using helper
        User createdBy = UserContext.getAuthenticatedUser(userRepository);

        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setImage(request.getImage());
        brand.setCreatedBy(createdBy.getId());
        brand.setCreatedAt(LocalDateTime.now());
        brand.setCompanyId(createdBy.getCompanyId());

        return new BrandResponse(brandRepository.save(brand));
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(BrandResponse::new)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Override
    public List<BrandResponse> findAllByCreatedBy(Long userId) {
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
        // Get authenticated user using helper
        User updatedBy = UserContext.getAuthenticatedUser(userRepository);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setImage(request.getImage());
        brand.setUpdatedBy(updatedBy.getId());
        brand.setUpdatedAt(LocalDateTime.now());

        return new BrandResponse(brandRepository.save(brand));
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
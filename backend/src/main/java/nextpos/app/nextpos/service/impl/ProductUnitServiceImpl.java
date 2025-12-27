package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateProductUnitRequest;
import nextpos.app.nextpos.model.dto.response.ProductUnitResponse;
import nextpos.app.nextpos.model.entity.ProductUnit;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductUnitService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductUnitServiceImpl implements ProductUnitService {

    private final ProductUnitRepository productUnitRepository;
    private final UserRepository userRepository;

    @Override
    public ProductUnitResponse createProductUnit(CreateProductUnitRequest request) {
        // Get authenticated user using helper
        User createdBy = UserContext.getAuthenticatedUser(userRepository);

        // Create and save product unit
        ProductUnit unit = new ProductUnit();
        unit.setName(request.getName());
        unit.setShortName(request.getShortName());
        unit.setBaseUnit(request.getBaseUnit());
        unit.setOperator(request.getOperator());
        unit.setOperatorValue(request.getOperatorValue());
        unit.setCreatedBy(createdBy.getId());
        unit.setCreatedAt(LocalDateTime.now());
        unit.setCompanyId(createdBy.getCompanyId());

        return new ProductUnitResponse(productUnitRepository.save(unit));
    }

    @Override
    public ProductUnitResponse getProductUnitById(Long id) {
        return productUnitRepository.findById(id)
                .map(ProductUnitResponse::new)
                .orElseThrow(() -> new RuntimeException("Product unit not found"));
    }

    @Override
    public List<ProductUnitResponse> findAllByCreatedBy(Long userId) {
        return productUnitRepository.findAllByCreatedBy(userId).stream()
                .map(ProductUnitResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductUnitResponse> getAllProductUnits() {
        return productUnitRepository.findAll().stream()
                .map(ProductUnitResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductUnitResponse updateProductUnit(Long id, CreateProductUnitRequest request) {
        // Get authenticated user using helper
        User updatedBy = UserContext.getAuthenticatedUser(userRepository);

        ProductUnit unit = productUnitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product unit not found"));
                
        unit.setName(request.getName());
        unit.setShortName(request.getShortName());
        unit.setBaseUnit(request.getBaseUnit());
        unit.setOperator(request.getOperator());
        unit.setOperatorValue(request.getOperatorValue());
        unit.setUpdatedBy(updatedBy.getId());
        unit.setUpdatedAt(LocalDateTime.now());

        return new ProductUnitResponse(productUnitRepository.save(unit));
    }

    @Override
    public void deleteProductUnit(Long id) {
        productUnitRepository.deleteById(id);
    }
}

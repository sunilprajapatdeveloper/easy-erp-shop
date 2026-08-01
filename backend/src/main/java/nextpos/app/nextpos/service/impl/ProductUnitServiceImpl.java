package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateProductUnitRequest;
import nextpos.app.nextpos.model.dto.response.ProductUnitResponse;
import nextpos.app.nextpos.model.entity.ProductUnit;
import nextpos.app.nextpos.repository.ProductUnitRepository;
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

    @Override
    public ProductUnitResponse createProductUnit(CreateProductUnitRequest request) {
        // Get authenticated user ID and company ID using the new parameterless
        Long createdById = UserContext.getCurrentUserId();
        Long companyId = UserContext.getCurrentCompanyId();

        // Create and save product unit
        ProductUnit unit = new ProductUnit();
        unit.setName(request.getName());
        unit.setShortName(request.getShortName());
        unit.setBaseUnit(request.getBaseUnit());
        unit.setOperator(request.getOperator());
        unit.setOperatorValue(request.getOperatorValue());
        unit.setCreatedBy(createdById);
        unit.setCreatedAt(LocalDateTime.now());
        unit.setCompanyId(companyId);

        return new ProductUnitResponse(productUnitRepository.save(unit));
    }

    @Override
    public ProductUnitResponse getProductUnitById(Long id) {
        return productUnitRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
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
        return productUnitRepository.findByCompanyId(UserContext.getCurrentCompanyId()).stream()
                .map(ProductUnitResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProductUnitResponse updateProductUnit(Long id, CreateProductUnitRequest request) {
        // Get authenticated user ID using parameterless UserContext
        Long updatedById = UserContext.getCurrentUserId();

        ProductUnit unit = productUnitRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Product unit not found"));

        unit.setName(request.getName());
        unit.setShortName(request.getShortName());
        unit.setBaseUnit(request.getBaseUnit());
        unit.setOperator(request.getOperator());
        unit.setOperatorValue(request.getOperatorValue());
        unit.setUpdatedBy(updatedById);
        unit.setUpdatedAt(LocalDateTime.now());

        return new ProductUnitResponse(productUnitRepository.save(unit));
    }

    @Override
    public void deleteProductUnit(Long id) {
        ProductUnit unit = productUnitRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Product unit not found"));
        productUnitRepository.delete(unit);
    }
}

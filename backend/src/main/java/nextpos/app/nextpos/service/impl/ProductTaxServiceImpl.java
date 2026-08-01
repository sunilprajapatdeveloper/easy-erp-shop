package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductTaxRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductTaxRequest;
import nextpos.app.nextpos.model.dto.response.ProductTaxResponse;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductTax;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.ProductTaxRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.service.interf.ProductTaxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductTaxServiceImpl implements ProductTaxService {

    private final ProductTaxRepository productTaxRepository;
    private final ProductRepository productRepository;
    private final WarehouseAccessService warehouseAccessService;

    @Override
    public ProductTaxResponse createProductTax(CreateProductTaxRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(request.getProductId(), companyId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));

        Warehouse warehouse = null;
        if (request.getWarehouseId() != null) {
            warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
        }

        // Check uniqueness by tax code
        boolean exists;
        if (warehouse != null) {
            exists = productTaxRepository.existsByProductIdAndWarehouseIdAndTaxCodeAndCompanyId(
                    product.getId(), warehouse.getId(), request.getTaxCode(), companyId);
        } else {
            exists = productTaxRepository.existsByProductIdAndWarehouseIsNullAndTaxCodeAndCompanyId(
                    product.getId(), request.getTaxCode(), companyId);
        }
        if (exists) {
            throw new IllegalArgumentException("Tax with this code already exists for this product/warehouse");
        }

        ProductTax tax = ProductTax.builder()
                .product(product)
                .warehouse(warehouse)
                .taxCode(request.getTaxCode())
                .taxName(request.getTaxName())
                .taxCategory(request.getTaxCategory())
                .taxRate(request.getTaxRate())
                .overrideInclusionType(request.getOverrideInclusionType())
                .overrideApplicationOrder(request.getOverrideApplicationOrder())
                .isCompound(request.getIsCompound())
                .isActive(request.getIsActive())
                .companyId(companyId)
                .createdBy(currentUserId)
                .createdAt(LocalDateTime.now())
                .build();

        ProductTax saved = productTaxRepository.save(tax);
        return ProductTaxResponse.fromEntity(saved);
    }

    @Override
    public ProductTaxResponse updateProductTax(Long taxId, UpdateProductTaxRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        ProductTax existing = productTaxRepository.findByIdAndCompanyId(taxId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductTax not found with id: " + taxId));

        // Update product if changed
        if (request.getProductId() != null && !request.getProductId().equals(existing.getProduct().getId())) {
            Product newProduct = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(request.getProductId(), companyId)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));
            existing.setProduct(newProduct);
        }

        // Update warehouse if changed
        if (request.getWarehouseId() != null) {
            if (existing.getWarehouse() == null || !request.getWarehouseId().equals(existing.getWarehouse().getId())) {
                Warehouse newWarehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                existing.setWarehouse(newWarehouse);
            }
        }

        if (request.getTaxCode() != null)
            existing.setTaxCode(request.getTaxCode());
        if (request.getTaxName() != null)
            existing.setTaxName(request.getTaxName());
        if (request.getTaxCategory() != null)
            existing.setTaxCategory(request.getTaxCategory());
        if (request.getTaxRate() != null)
            existing.setTaxRate(request.getTaxRate());
        if (request.getOverrideInclusionType() != null)
            existing.setOverrideInclusionType(request.getOverrideInclusionType());
        if (request.getOverrideApplicationOrder() != null)
            existing.setOverrideApplicationOrder(request.getOverrideApplicationOrder());
        if (request.getIsCompound() != null)
            existing.setIsCompound(request.getIsCompound());
        if (request.getIsActive() != null)
            existing.setIsActive(request.getIsActive());

        existing.setUpdatedBy(currentUserId);
        existing.setUpdatedAt(LocalDateTime.now());

        ProductTax saved = productTaxRepository.save(existing);
        return ProductTaxResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductTaxResponse getProductTaxById(Long taxId) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductTax entity = productTaxRepository.findByIdAndCompanyId(taxId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductTax not found with id: " + taxId));
        return ProductTaxResponse.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTaxResponse> listTaxesByProduct(Long productId) {
        Long companyId = UserContext.getCurrentCompanyId();

        return productTaxRepository.findAllByProductIdAndCompanyId(productId, companyId)
                .stream().map(ProductTaxResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTaxResponse> listTaxesByWarehouse(Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();
        warehouseAccessService.requireAccessible(warehouseId);

        return productTaxRepository.findAllByWarehouseIdAndCompanyId(warehouseId, companyId)
                .stream().map(ProductTaxResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTaxResponse> listAllTaxes() {
        Long companyId = UserContext.getCurrentCompanyId();

        return productTaxRepository.findAllByCompanyId(companyId)
                .stream().map(ProductTaxResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteProductTax(Long taxId) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductTax existing = productTaxRepository.findByIdAndCompanyId(taxId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductTax not found with id: " + taxId));
        productTaxRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTaxResponse> findEffectiveTax(Long productId, Long warehouseId, String taxCode) {
        Long companyId = UserContext.getCurrentCompanyId();

        Optional<ProductTax> exact = Optional.empty();
        if (warehouseId != null) {
            warehouseAccessService.requireAccessible(warehouseId);
            exact = productTaxRepository.findAllByProductIdAndCompanyId(productId, companyId)
                    .stream()
                    .filter(t -> warehouseId.equals(t.getWarehouse() != null ? t.getWarehouse().getId() : null)
                            && taxCode.equals(t.getTaxCode()))
                    .findFirst();
        }

        if (exact.isPresent())
            return exact.map(ProductTaxResponse::fromEntity);

        // Fallback to global tax (warehouse = null)
        return productTaxRepository.findAllByProductIdAndCompanyId(productId, companyId)
                .stream()
                .filter(t -> t.getWarehouse() == null && taxCode.equals(t.getTaxCode()))
                .findFirst()
                .map(ProductTaxResponse::fromEntity);
    }
}

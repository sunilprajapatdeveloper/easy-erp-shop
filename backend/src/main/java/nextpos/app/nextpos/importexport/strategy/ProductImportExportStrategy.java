package nextpos.app.nextpos.importexport.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ProductType;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.service.helper.BarcodeHelper;
import nextpos.app.nextpos.service.interf.ProductService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductImportExportStrategy implements ImportExportStrategy {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductRepository productRepository;
    private final BarcodeHelper barcodeHelper;

    @Override
    public String getModuleName() {
        return "Product";
    }

    @Override
    public FlatFileItemReader<Map<String, Object>> getReader(String fileUrl, Map<String, Object> options) {
        throw new UnsupportedOperationException("Use batch job reader instead");
    }

    @Override
    public ItemProcessor<Map<String, Object>, Object> getProcessor(Map<String, Object> options) {
        return new ItemProcessor<Map<String, Object>, Object>() {
            @Override
            public Object process(Map<String, Object> row) throws Exception {
                CreateProductRequest request = mapRowToCreateRequest(row);
                if (request.getName() == null || request.getName().trim().isEmpty()) {
                    throw new IllegalArgumentException("Product name is required");
                }
                if (request.getSku() == null || request.getSku().trim().isEmpty()) {
                    request.setSku(barcodeHelper.generateProductCode());
                }
                if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                    request.setCode(barcodeHelper.generateProductCode());
                }
                if (request.getStatus() == null) {
                    request.setStatus(ProductStatus.ACTIVE);
                }
                if (request.getProductType() == null) {
                    request.setProductType(ProductType.STOCK);
                }
                Long companyId = (Long) options.get("companyId");
                validateReferences(request, companyId);
                return request;
            }
        };
    }

    @Override
    public ItemWriter<Object> getWriter(Map<String, Object> options) {
        Long userId = (Long) options.get("userId");
        Long companyId = (Long) options.get("companyId");

        return items -> {
            for (Object item : items.getItems()) {
                CreateProductRequest req = (CreateProductRequest) item;
                try {
                    // Check for existing product (including soft-deleted) by code and company
                    Optional<Product> existingOpt = productRepository.findByCodeAndCompanyId(req.getCode(), companyId);
                    if (existingOpt.isPresent()) {
                        Product existing = existingOpt.get();
                        if (existing.getIsDeleted()) {
                            // Reactivate and update the existing product
                            log.info("Reactivating deleted product with code: {}", req.getCode());
                            existing.setIsDeleted(false);
                            existing.setStatus(ProductStatus.ACTIVE);
                            existing.setUpdatedBy(userId);
                            existing.setUpdatedAt(LocalDateTime.now());
                            productRepository.save(existing);

                            // Convert CreateProductRequest to UpdateProductRequest with the existing product ID
                            UpdateProductRequest updateReq = toUpdateProductRequest(req, existing.getId());
                            productService.updateProduct(existing.getId(), updateReq, userId, companyId);
                        } else {
                            // Active product already exists → skip (current behavior)
                            log.info("Product already exists (active) with code: {}, skipping", req.getCode());
                        }
                    } else {
                        // No product with this code → create new
                        productService.createProduct(req, userId, companyId);
                    }
                } catch (DataIntegrityViolationException | IllegalArgumentException e) {
                    throw e;
                } catch (Exception e) {
                    log.error("Unexpected failure to create/update product: {}", req.getSku(), e);
                    throw new RuntimeException("Failed to create/update product: " + e.getMessage(), e);
                }
            }
        };
    }

    @Override
    public List<Map<String, Object>> exportData(Map<String, Object> filters) {
        Long companyId = (Long) filters.get("companyId");
        log.info("Exporting products for companyId: {}", companyId);
        List<ProductResponse> products = productService.getAllProducts(companyId);
        log.info("Found {} products to export", products.size());
        return products.stream()
                .map(this::toExportRow)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getColumnHeaders() {
        return Arrays.asList(
                "name", "code", "sku", "barcode", "categoryId", "categoryName",
                "brandId", "brandName", "productType", "status", "productUnitId",
                "salesUnitId", "purchaseUnitId", "unitConversionFactor",
                "isBatchManaged", "isSerialized", "isComposite", "hasVariants",
                "weight", "volume", "dimensions", "description", "productImage",
                "imageUrls");
    }

    @Override
    public Map<String, Object> toExportRow(Object entity) {
        ProductResponse product = (ProductResponse) entity;
        Map<String, Object> row = new HashMap<>();
        row.put("name", product.getName());
        row.put("code", product.getCode());
        row.put("sku", product.getSku());
        row.put("barcode", product.getBarcode());
        row.put("categoryId", product.getCategoryId());
        row.put("categoryName", product.getCategoryName());
        row.put("brandId", product.getBrandId());
        row.put("brandName", product.getBrandName());
        row.put("productType", product.getProductType());
        row.put("status", product.getStatus());
        row.put("productUnitId", product.getProductUnitId());
        row.put("salesUnitId", product.getSalesUnitId());
        row.put("purchaseUnitId", product.getPurchaseUnitId());
        row.put("unitConversionFactor", product.getUnitConversionFactor());
        row.put("isBatchManaged", product.getIsBatchManaged());
        row.put("isSerialized", product.getIsSerialized());
        row.put("isComposite", product.getIsComposite());
        row.put("hasVariants", product.getHasVariants());
        row.put("weight", product.getWeight());
        row.put("volume", product.getVolume());
        row.put("dimensions", product.getDimensions());
        row.put("description", product.getDescription());
        row.put("productImage", product.getProductImage());
        if (product.getImageUrls() != null) {
            row.put("imageUrls", String.join(",", product.getImageUrls()));
        } else {
            row.put("imageUrls", null);
        }
        return row;
    }

    private CreateProductRequest mapRowToCreateRequest(Map<String, Object> row) {
        CreateProductRequest request = new CreateProductRequest();
        request.setName((String) row.get("name"));
        request.setCode((String) row.get("code"));
        request.setSku((String) row.get("sku"));
        request.setBarcode((String) row.get("barcode"));
        request.setCategoryId(row.get("categoryId") != null ? Long.valueOf(row.get("categoryId").toString()) : null);
        request.setBrandId(row.get("brandId") != null ? Long.valueOf(row.get("brandId").toString()) : null);
        String productTypeStr = (String) row.get("productType");
        if (productTypeStr != null) {
            request.setProductType(ProductType.valueOf(productTypeStr.toUpperCase()));
        }
        String statusStr = (String) row.get("status");
        if (statusStr != null) {
            request.setStatus(ProductStatus.valueOf(statusStr.toUpperCase()));
        }
        request.setProductUnitId(
                row.get("productUnitId") != null ? Long.valueOf(row.get("productUnitId").toString()) : null);
        request.setSalesUnitId(row.get("salesUnitId") != null ? Long.valueOf(row.get("salesUnitId").toString()) : null);
        request.setPurchaseUnitId(
                row.get("purchaseUnitId") != null ? Long.valueOf(row.get("purchaseUnitId").toString()) : null);
        if (row.get("unitConversionFactor") != null) {
            request.setUnitConversionFactor(new BigDecimal(row.get("unitConversionFactor").toString()));
        }
        request.setIsBatchManaged(
                row.get("isBatchManaged") != null ? Boolean.valueOf(row.get("isBatchManaged").toString()) : false);
        request.setIsSerialized(
                row.get("isSerialized") != null ? Boolean.valueOf(row.get("isSerialized").toString()) : false);
        request.setIsComposite(
                row.get("isComposite") != null ? Boolean.valueOf(row.get("isComposite").toString()) : false);
        request.setHasVariants(
                row.get("hasVariants") != null ? Boolean.valueOf(row.get("hasVariants").toString()) : false);
        if (row.get("weight") != null) {
            request.setWeight(new BigDecimal(row.get("weight").toString()));
        }
        if (row.get("volume") != null) {
            request.setVolume(new BigDecimal(row.get("volume").toString()));
        }
        request.setDimensions((String) row.get("dimensions"));
        request.setDescription((String) row.get("description"));
        request.setProductImage((String) row.get("productImage"));
        String imageUrlsStr = (String) row.get("imageUrls");
        if (imageUrlsStr != null && !imageUrlsStr.trim().isEmpty()) {
            request.setImageUrls(Arrays.asList(imageUrlsStr.split(",")));
        }
        return request;
    }

    private void validateReferences(CreateProductRequest request, Long companyId) {
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            if (!category.getCompanyId().equals(companyId)) {
                throw new IllegalArgumentException("Category not found in company: " + request.getCategoryId());
            }
        } else {
            throw new IllegalArgumentException("Category ID is required");
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            if (!brand.getCompanyId().equals(companyId)) {
                throw new IllegalArgumentException("Brand not found in company: " + request.getBrandId());
            }
        }
        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findById(request.getProductUnitId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            if (!unit.getCompanyId().equals(companyId)) {
                throw new IllegalArgumentException("Product unit not found in company: " + request.getProductUnitId());
            }
        }
        if (request.getSalesUnitId() != null) {
            ProductUnit unit = productUnitRepository.findById(request.getSalesUnitId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            if (!unit.getCompanyId().equals(companyId)) {
                throw new IllegalArgumentException("Sales unit not found in company: " + request.getSalesUnitId());
            }
        }
        if (request.getPurchaseUnitId() != null) {
            ProductUnit unit = productUnitRepository.findById(request.getPurchaseUnitId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Purchase unit not found: " + request.getPurchaseUnitId()));
            if (!unit.getCompanyId().equals(companyId)) {
                throw new IllegalArgumentException(
                        "Purchase unit not found in company: " + request.getPurchaseUnitId());
            }
        }
    }

    private UpdateProductRequest toUpdateProductRequest(CreateProductRequest createReq, Long productId) {
        UpdateProductRequest updateReq = new UpdateProductRequest();
        updateReq.setId(productId);
        updateReq.setName(createReq.getName());
        updateReq.setCode(createReq.getCode());
        updateReq.setSku(createReq.getSku());
        updateReq.setBarcode(createReq.getBarcode());
        updateReq.setCategoryId(createReq.getCategoryId());
        updateReq.setBrandId(createReq.getBrandId());
        updateReq.setProductType(createReq.getProductType());
        updateReq.setStatus(createReq.getStatus());
        updateReq.setProductUnitId(createReq.getProductUnitId());
        updateReq.setSalesUnitId(createReq.getSalesUnitId());
        updateReq.setPurchaseUnitId(createReq.getPurchaseUnitId());
        updateReq.setUnitConversionFactor(createReq.getUnitConversionFactor());
        updateReq.setIsBatchManaged(createReq.getIsBatchManaged());
        updateReq.setIsSerialized(createReq.getIsSerialized());
        updateReq.setIsComposite(createReq.getIsComposite());
        updateReq.setHasVariants(createReq.getHasVariants());
        updateReq.setWeight(createReq.getWeight());
        updateReq.setVolume(createReq.getVolume());
        updateReq.setDimensions(createReq.getDimensions());
        updateReq.setDescription(createReq.getDescription());
        updateReq.setProductImage(createReq.getProductImage());
        updateReq.setImageUrls(createReq.getImageUrls());
        updateReq.setIsDeleted(createReq.getIsDeleted());
        return updateReq;
    }
}
package nextpos.app.nextpos.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductRequest;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.dto.response.ProductPriceResponse;
import nextpos.app.nextpos.model.dto.response.ProductResponse;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.model.dto.response.ProductTaxResponse;
import nextpos.app.nextpos.model.entity.Brand;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductStatus;
import nextpos.app.nextpos.model.entity.ProductUnit;
import nextpos.app.nextpos.pagination.dto.PaginationRequest;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.repository.BrandRepository;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.ProductStockRepository;
import nextpos.app.nextpos.repository.ProductTaxRepository;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.helper.BarcodeHelper;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.interf.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductTaxRepository productTaxRepository;
    private final BarcodeHelper barcodeHelper;
    private final MediaService mediaService;
    private final WarehouseAccessService warehouseAccessService;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        // Generate code and barcode if not provided
        String codeToUse = (request.getCode() != null && !request.getCode().isBlank())
                ? request.getCode().trim()
                : barcodeHelper.generateProductCode();

        if (productRepository.findByCodeAndCompanyIdAndIsDeletedFalse(codeToUse, companyId).isPresent()) {
            throw new IllegalArgumentException("Product code already in use for company: " + codeToUse);
        }

        String barcodeToUse = (request.getBarcode() != null && !request.getBarcode().isBlank())
                ? request.getBarcode().trim()
                : barcodeHelper.generateBarcode();

        if (productRepository.findByBarcodeAndCompanyIdAndIsDeletedFalse(barcodeToUse, companyId).isPresent()) {
            throw new IllegalArgumentException("Product barcode already in use for company: " + barcodeToUse);
        }

        // Create product entity
        Product product = new Product();
        product.setCode(codeToUse);
        product.setBarcode(barcodeToUse);
        product.setCreatedBy(currentUserId);
        product.setUpdatedBy(currentUserId);
        product.setCompanyId(companyId);

        // Directly map request fields
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getSku() != null)
            product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndCompanyId(request.getCategoryId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findByIdAndCompanyId(request.getBrandId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }

        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findByIdAndCompanyId(request.getProductUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            product.setProductUnit(unit);
        }

        if (request.getSalesUnitId() != null) {
            ProductUnit salesUnit = productUnitRepository.findByIdAndCompanyId(request.getSalesUnitId(), companyId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            product.setSalesUnit(salesUnit);
        }

        if (request.getPurchaseUnitId() != null) {
            ProductUnit purchaseUnit = productUnitRepository.findByIdAndCompanyId(request.getPurchaseUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Purchase unit not found: " + request.getPurchaseUnitId()));
            product.setPurchaseUnit(purchaseUnit);
        }

        if (request.getUnitConversionFactor() != null)
            product.setUnitConversionFactor(request.getUnitConversionFactor());
        if (request.getIsBatchManaged() != null)
            product.setIsBatchManaged(request.getIsBatchManaged());
        if (request.getIsSerialized() != null)
            product.setIsSerialized(request.getIsSerialized());
        if (request.getIsComposite() != null)
            product.setIsComposite(request.getIsComposite());
        if (request.getHasVariants() != null)
            product.setHasVariants(request.getHasVariants());
        if (request.getWeight() != null)
            product.setWeight(request.getWeight());
        if (request.getVolume() != null)
            product.setVolume(request.getVolume());
        if (request.getDimensions() != null)
            product.setDimensions(request.getDimensions());
        if (request.getDescription() != null)
            product.setDescription(request.getDescription());
        if (request.getProductImage() != null)
            product.setProductImage(request.getProductImage());
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty())
            product.setImageUrls(String.join(",", request.getImageUrls()));
        if (request.getStatus() != null)
            product.setStatus(request.getStatus());
        if (request.getProductType() != null)
            product.setProductType(request.getProductType());
        if (request.getIsDeleted() != null)
            product.setIsDeleted(request.getIsDeleted());

        // Get full media response
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        // Save and return response
        return ProductResponse.fromEntity(productRepository.save(product), mediaResponse);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, Long userId, Long companyId) {
        // Generate code and barcode if not provided
        String codeToUse = (request.getCode() != null && !request.getCode().isBlank())
                ? request.getCode().trim()
                : barcodeHelper.generateProductCode();

        if (productRepository.findByCodeAndCompanyIdAndIsDeletedFalse(codeToUse, companyId).isPresent()) {
            throw new IllegalArgumentException("Product code already in use for company: " + codeToUse);
        }

        String barcodeToUse = (request.getBarcode() != null && !request.getBarcode().isBlank())
                ? request.getBarcode().trim()
                : barcodeHelper.generateBarcode();

        if (productRepository.findByBarcodeAndCompanyIdAndIsDeletedFalse(barcodeToUse, companyId).isPresent()) {
            throw new IllegalArgumentException("Product barcode already in use for company: " + barcodeToUse);
        }

        // Create product entity
        Product product = new Product();
        product.setCode(codeToUse);
        product.setBarcode(barcodeToUse);
        product.setCreatedBy(userId);
        product.setUpdatedBy(userId);
        product.setCompanyId(companyId);

        // Directly map request fields (same as original)
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getSku() != null)
            product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndCompanyId(request.getCategoryId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findByIdAndCompanyId(request.getBrandId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }

        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findByIdAndCompanyId(request.getProductUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            product.setProductUnit(unit);
        }

        if (request.getSalesUnitId() != null) {
            ProductUnit salesUnit = productUnitRepository.findByIdAndCompanyId(request.getSalesUnitId(), companyId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            product.setSalesUnit(salesUnit);
        }

        if (request.getPurchaseUnitId() != null) {
            ProductUnit purchaseUnit = productUnitRepository.findByIdAndCompanyId(request.getPurchaseUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Purchase unit not found: " + request.getPurchaseUnitId()));
            product.setPurchaseUnit(purchaseUnit);
        }

        if (request.getUnitConversionFactor() != null)
            product.setUnitConversionFactor(request.getUnitConversionFactor());
        if (request.getIsBatchManaged() != null)
            product.setIsBatchManaged(request.getIsBatchManaged());
        if (request.getIsSerialized() != null)
            product.setIsSerialized(request.getIsSerialized());
        if (request.getIsComposite() != null)
            product.setIsComposite(request.getIsComposite());
        if (request.getHasVariants() != null)
            product.setHasVariants(request.getHasVariants());
        if (request.getWeight() != null)
            product.setWeight(request.getWeight());
        if (request.getVolume() != null)
            product.setVolume(request.getVolume());
        if (request.getDimensions() != null)
            product.setDimensions(request.getDimensions());
        if (request.getDescription() != null)
            product.setDescription(request.getDescription());
        if (request.getProductImage() != null)
            product.setProductImage(request.getProductImage());
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            product.setImageUrls(String.join(",", request.getImageUrls()));
        }
        if (request.getStatus() != null)
            product.setStatus(request.getStatus());
        if (request.getProductType() != null)
            product.setProductType(request.getProductType());
        if (request.getIsDeleted() != null)
            product.setIsDeleted(request.getIsDeleted());

        // Save and return response (no media images in batch context – pass empty list)
        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved, Collections.emptyList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Long companyId = UserContext.getCurrentCompanyId();
        return getProductById(id, null, false, false, false, companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id, Long warehouseId, boolean includePrice, boolean includeStock,
            boolean includeTax) {
        Long companyId = UserContext.getCurrentCompanyId();
        return getProductById(id, warehouseId, includePrice, includeStock, includeTax, companyId);
    }

    private ProductResponse getProductById(Long id, Long warehouseId, boolean includePrice, boolean includeStock,
            boolean includeTax, Long companyId) {
        if (warehouseId != null) {
            warehouseAccessService.requireAccessible(warehouseId);
        }
        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Fetch media
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);
        ProductResponse response = ProductResponse.fromEntity(product, mediaResponse);

        // If a warehouse is specified, attach price/stock/tax for that warehouse
        if (warehouseId != null) {
            if (includePrice) {
                productPriceRepository.findByProductIdAndWarehouseIdAndCompanyId(id, warehouseId, companyId)
                        .ifPresent(price -> response.setPrice(ProductPriceResponse.fromEntity(price)));
            }
            if (includeStock) {
                productStockRepository.findByProductIdAndWarehouseIdAndCompanyId(id, warehouseId, companyId)
                        .ifPresent(stock -> response.setStock(ProductStockResponse.fromEntity(stock)));
            }
            if (includeTax) {
                productTaxRepository.findByProductIdAndWarehouseIdAndCompanyId(id, warehouseId, companyId)
                        .ifPresent(tax -> response.setTax(ProductTaxResponse.fromEntity(tax)));
            }
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByCode(String code) {
        Long companyId = UserContext.getCurrentCompanyId();

        Product product = productRepository
                .findByCodeAndCompanyIdAndIsDeletedFalse(code, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with code: " + code));

        // Fetch media for THIS product
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        return ProductResponse.fromEntity(product, mediaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByBarcode(String barcode) {
        Long companyId = UserContext.getCurrentCompanyId();

        Product product = productRepository
                .findByBarcodeAndCompanyIdAndIsDeletedFalse(barcode, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with barcode: " + barcode));

        // Fetch media for THIS product
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        return ProductResponse.fromEntity(product, mediaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        Long companyId = UserContext.getCurrentCompanyId();

        List<Product> products = productRepository
                .findAllByCompanyIdAndIsDeletedFalse(companyId);

        return products.stream()
                .map(product -> {
                    List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);
                    return ProductResponse.fromEntity(product, mediaResponse);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(Long companyId) {
        List<Product> products = productRepository.findAllByCompanyIdAndIsDeletedFalse(companyId);
        return products.stream()
                .map(product -> ProductResponse.fromEntity(product, Collections.emptyList()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Long userId = UserContext.getCurrentUserId();
        Long companyId = UserContext.getCurrentCompanyId();
        return updateProduct(id, request, userId, companyId);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request, Long userId, Long companyId) {
        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Code and barcode validation
        if (request.getCode() != null && !request.getCode().isBlank()) {
            String newCode = request.getCode().trim();
            if (!newCode.equals(product.getCode())
                    && productRepository.findByCodeAndCompanyIdAndIsDeletedFalse(newCode, companyId).isPresent()) {
                throw new IllegalArgumentException("Product code already in use: " + newCode);
            }
            product.setCode(newCode);
        }

        if (request.getBarcode() != null && !request.getBarcode().isBlank()) {
            String newBarcode = request.getBarcode().trim();
            if (!newBarcode.equals(product.getBarcode())
                    && productRepository.findByBarcodeAndCompanyIdAndIsDeletedFalse(newBarcode, companyId)
                            .isPresent()) {
                throw new IllegalArgumentException("Product barcode already in use: " + newBarcode);
            }
            product.setBarcode(newBarcode);
        }

        // Direct mapping inside update method
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getSku() != null)
            product.setSku(request.getSku());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndCompanyId(request.getCategoryId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findByIdAndCompanyId(request.getBrandId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }
        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findByIdAndCompanyId(request.getProductUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            product.setProductUnit(unit);
        }
        if (request.getSalesUnitId() != null) {
            ProductUnit salesUnit = productUnitRepository.findByIdAndCompanyId(request.getSalesUnitId(), companyId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            product.setSalesUnit(salesUnit);
        }
        if (request.getPurchaseUnitId() != null) {
            ProductUnit purchaseUnit = productUnitRepository.findByIdAndCompanyId(request.getPurchaseUnitId(), companyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Purchase unit not found: " + request.getPurchaseUnitId()));
            product.setPurchaseUnit(purchaseUnit);
        }

        if (request.getUnitConversionFactor() != null)
            product.setUnitConversionFactor(request.getUnitConversionFactor());
        if (request.getIsBatchManaged() != null)
            product.setIsBatchManaged(request.getIsBatchManaged());
        if (request.getIsSerialized() != null)
            product.setIsSerialized(request.getIsSerialized());
        if (request.getIsComposite() != null)
            product.setIsComposite(request.getIsComposite());
        if (request.getHasVariants() != null)
            product.setHasVariants(request.getHasVariants());
        if (request.getWeight() != null)
            product.setWeight(request.getWeight());
        if (request.getVolume() != null)
            product.setVolume(request.getVolume());
        if (request.getDimensions() != null)
            product.setDimensions(request.getDimensions());
        if (request.getDescription() != null)
            product.setDescription(request.getDescription());
        if (request.getProductImage() != null)
            product.setProductImage(request.getProductImage());
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty())
            product.setImageUrls(String.join(",", request.getImageUrls()));
        if (request.getStatus() != null)
            product.setStatus(request.getStatus());
        if (request.getProductType() != null)
            product.setProductType(request.getProductType());
        if (request.getIsDeleted() != null)
            product.setIsDeleted(request.getIsDeleted());

        product.setUpdatedBy(userId);
        product.setUpdatedAt(LocalDateTime.now());

        // Fetch media for THIS product
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        return ProductResponse.fromEntity(productRepository.save(product), mediaResponse);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        product.setIsDeleted(true);
        product.setStatus(ProductStatus.INACTIVE);
        product.setUpdatedBy(currentUserId);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    @Override
    public void adjustStock(Long productId, Integer quantityDelta) {
        throw new UnsupportedOperationException("Stock is managed via ProductStockService");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long warehouseId,
            Long userId,
            boolean includePrice,
            boolean includeStock,
            boolean includeTax,
            boolean onlyComplete) {
        Long companyId = UserContext.getCurrentCompanyId();

        // 1. Fetch all products for company in one query
        List<Product> products = productRepository.findAllByCompanyIdAndIsDeletedFalse(companyId);

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        // 2. Fetch related entities in bulk
        // Single warehouse maps
        Map<Long, ProductPriceResponse> singlePriceMap = new HashMap<>();
        Map<Long, ProductStockResponse> singleStockMap = new HashMap<>();
        Map<Long, ProductTaxResponse> singleTaxMap = new HashMap<>();

        // Multi-warehouse maps
        Map<Long, List<ProductPriceResponse>> multiPriceMap = new HashMap<>();
        Map<Long, List<ProductStockResponse>> multiStockMap = new HashMap<>();
        Map<Long, List<ProductTaxResponse>> multiTaxMap = new HashMap<>();

        if (warehouseId != null) {
            warehouseAccessService.requireAccessible(warehouseId);
            // Single warehouse mode
            if (includePrice) {
                productPriceRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(price -> singlePriceMap.put(price.getProduct().getId(),
                                ProductPriceResponse.fromEntity(price)));
            }
            if (includeStock) {
                productStockRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(stock -> singleStockMap.put(stock.getProduct().getId(),
                                ProductStockResponse.fromEntity(stock)));
            }
            if (includeTax) {
                productTaxRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(tax -> singleTaxMap.put(tax.getProduct().getId(),
                                ProductTaxResponse.fromEntity(tax)));
            }
        } else {
            // Multi-warehouse mode — using groupingBy
            Set<Long> accessibleWarehouseIds = warehouseAccessService.accessibleWarehouses().stream()
                    .map(warehouse -> warehouse.getId())
                    .collect(Collectors.toSet());
            if (includePrice) {
                Map<Long, List<ProductPriceResponse>> groupedPrices = productPriceRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .filter(price -> price.getWarehouse() == null
                                || accessibleWarehouseIds.contains(price.getWarehouse().getId()))
                        .collect(Collectors.groupingBy(
                                price -> price.getProduct().getId(),
                                Collectors.mapping(ProductPriceResponse::fromEntity, Collectors.toList())));
                multiPriceMap.putAll(groupedPrices);
            }
            if (includeStock) {
                Map<Long, List<ProductStockResponse>> groupedStocks = productStockRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .filter(stock -> accessibleWarehouseIds.contains(stock.getWarehouse().getId()))
                        .collect(Collectors.groupingBy(
                                stock -> stock.getProduct().getId(),
                                Collectors.mapping(ProductStockResponse::fromEntity, Collectors.toList())));
                multiStockMap.putAll(groupedStocks);
            }
            if (includeTax) {
                Map<Long, List<ProductTaxResponse>> groupedTaxes = productTaxRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .filter(tax -> tax.getWarehouse() == null
                                || accessibleWarehouseIds.contains(tax.getWarehouse().getId()))
                        .collect(Collectors.groupingBy(
                                tax -> tax.getProduct().getId(),
                                Collectors.mapping(ProductTaxResponse::fromEntity, Collectors.toList())));
                multiTaxMap.putAll(groupedTaxes);
            }
        }

        // 3. Map products to response DTOs
        List<ProductResponse> responses = products.stream().map(product -> {
            List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);
            ProductResponse response = ProductResponse.fromEntity(product, mediaResponse);

            if (warehouseId != null) {
                response.setPrice(singlePriceMap.get(product.getId()));
                response.setStock(singleStockMap.get(product.getId()));
                response.setTax(singleTaxMap.get(product.getId()));
            } else {
                response.setPrices(multiPriceMap.getOrDefault(product.getId(), Collections.emptyList()));
                response.setStocks(multiStockMap.getOrDefault(product.getId(), Collections.emptyList()));
                response.setTaxes(multiTaxMap.getOrDefault(product.getId(), Collections.emptyList()));
            }

            return response;
        }).collect(Collectors.toList());

        // 4. Apply onlyComplete filter if requested (requires warehouseId)
        if (onlyComplete && warehouseId != null) {
            responses = responses.stream()
                    .filter(product -> isProductCompleteForWarehouse(product))
                    .collect(Collectors.toList());
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String query, int limit) {
        Long companyId = UserContext.getCurrentCompanyId();

        List<Product> products = productRepository
                .findByCompanyIdAndIsDeletedFalseAndNameContainingIgnoreCaseOrCompanyIdAndIsDeletedFalseAndCodeContainingIgnoreCaseOrCompanyIdAndIsDeletedFalseAndSkuContainingIgnoreCase(
                        companyId, query, companyId, query, companyId, query)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());

        return products.stream()
                .map(p -> ProductResponse.fromEntity(p, Collections.emptyList()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String query, int page, int size) {
        Long companyId = UserContext.getCurrentCompanyId();
        int offset = page * size;

        String tsQuery = Arrays.stream(query.trim().toLowerCase().split("\\s+"))
                .filter(word -> word.length() > 0)
                .map(word -> word + ":*")
                .collect(Collectors.joining(" & "));

        // If the query is empty after processing, return empty list
        if (tsQuery.isEmpty()) {
            return Collections.emptyList();
        }

        List<Product> products = productRepository.searchByFullText(companyId, tsQuery, offset, size);
        return products.stream()
                .map(p -> ProductResponse.fromEntity(p, Collections.emptyList()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ProductResponse> getProducts(PaginationRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long warehouseId = request.getWarehouseId();
        if (warehouseId != null) {
            warehouseAccessService.requireAccessible(warehouseId);
        }
        boolean includePrice = request.isIncludePrice();
        boolean includeStock = request.isIncludeStock();
        boolean includeTax = request.isIncludeTax();

        // 1. Fetch paginated products (with search)
        Page<Product> productPage;
        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            productPage = productRepository.searchFullText(companyId, request.getSearch(), request.toPageable());
        } else {
            productPage = productRepository.findAllByCompanyIdAndSearch(companyId, request.getSearch(),
                    request.toPageable());
        }

        if (productPage.isEmpty()) {
            return PaginationResponse.of(productPage.map(p -> ProductResponse.fromEntity(p, Collections.emptyList())));
        }

        List<Product> products = productPage.getContent();
        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        // 2. Bulk fetch warehouse-specific data (only if a warehouse is selected)
        Map<Long, ProductPriceResponse> priceMap = new HashMap<>();
        Map<Long, ProductStockResponse> stockMap = new HashMap<>();
        Map<Long, ProductTaxResponse> taxMap = new HashMap<>();

        if (warehouseId != null) {
            if (includePrice) {
                productPriceRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(price -> priceMap.put(price.getProduct().getId(),
                                ProductPriceResponse.fromEntity(price)));
            }
            if (includeStock) {
                productStockRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(stock -> stockMap.put(stock.getProduct().getId(),
                                ProductStockResponse.fromEntity(stock)));
            }
            if (includeTax) {
                productTaxRepository
                        .findAllByProductIdInAndWarehouseIdAndCompanyId(productIds, warehouseId, companyId)
                        .forEach(tax -> taxMap.put(tax.getProduct().getId(), ProductTaxResponse.fromEntity(tax)));
            }
        }

        // 3. Convert to response DTOs
        List<ProductResponse> responseList = products.stream().map(product -> {
            List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);
            ProductResponse response = ProductResponse.fromEntity(product, mediaResponse);
            if (warehouseId != null) {
                response.setPrice(priceMap.get(product.getId()));
                response.setStock(stockMap.get(product.getId()));
                response.setTax(taxMap.get(product.getId()));
            }
            return response;
        }).collect(Collectors.toList());

        // 4. Build pagination response
        PaginationResponse<ProductResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setData(responseList);
        paginationResponse.setPagination(PaginationResponse.PaginationMeta.builder()
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .hasNext(productPage.hasNext())
                .hasPrevious(productPage.hasPrevious())
                .build());
        return paginationResponse;
    }

    private List<MediaResponse> getProductImagesFromMedia(Long productId, Long companyId) {
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities(
                "PRODUCT",
                Collections.singletonList(productId),
                companyId);
        List<MediaResponse> media = mediaMap.get(productId);

        return (media != null && !media.isEmpty()) ? media : Collections.emptyList();
    }

    @Override
    @Transactional
    public void bulkDelete(List<Long> ids) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        List<Product> products = productRepository.findAllByIdInAndCompanyIdAndIsDeletedFalse(ids, companyId);
        if (products.size() != ids.stream().distinct().count()) {
            throw new IllegalArgumentException("One or more products were not found in the authenticated company");
        }
        for (Product product : products) {
            product.setIsDeleted(true);
            product.setStatus(ProductStatus.INACTIVE);
            product.setUpdatedBy(currentUserId);
            product.setUpdatedAt(LocalDateTime.now());
        }
        productRepository.saveAll(products);
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(List<Long> ids, ProductStatus status) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        List<Product> products = productRepository.findAllByIdInAndCompanyIdAndIsDeletedFalse(ids, companyId);
        if (products.size() != ids.stream().distinct().count()) {
            throw new IllegalArgumentException("One or more products were not found in the authenticated company");
        }
        for (Product product : products) {
            product.setStatus(status);
            product.setUpdatedBy(currentUserId);
            product.setUpdatedAt(LocalDateTime.now());
        }
        productRepository.saveAll(products);
    }

    /**
     * Checks if a product response has valid price, stock and tax
     */
    private boolean isProductCompleteForWarehouse(ProductResponse product) {
        // Price must exist and be > 0 (BigDecimal comparison)
        boolean hasPrice = product.getPrice() != null &&
                product.getPrice().getPrice() != null &&
                product.getPrice().getPrice().compareTo(java.math.BigDecimal.ZERO) > 0;

        // Stock must exist (any non-null object)
        boolean hasStock = product.getStock() != null;

        // Tax must exist and have tax rate > 0 (BigDecimal comparison)
        boolean hasTax = product.getTax() != null &&
                product.getTax().getTaxRate() != null &&
                product.getTax().getTaxRate().compareTo(java.math.BigDecimal.ZERO) > 0;

        return hasPrice && hasStock && hasTax;
    }
}

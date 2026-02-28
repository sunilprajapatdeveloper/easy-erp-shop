package nextpos.app.nextpos.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.BrandRepository;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.ProductStockRepository;
import nextpos.app.nextpos.repository.ProductTaxRepository;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.helper.BarcodeHelper;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.interf.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductTaxRepository productTaxRepository;
    private final BarcodeHelper barcodeHelper;
    private final MediaService mediaService;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

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
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }

        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findById(request.getProductUnitId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            product.setProductUnit(unit);
        }

        if (request.getSalesUnitId() != null) {
            ProductUnit salesUnit = productUnitRepository.findById(request.getSalesUnitId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            product.setSalesUnit(salesUnit);
        }

        if (request.getPurchaseUnitId() != null) {
            ProductUnit purchaseUnit = productUnitRepository.findById(request.getPurchaseUnitId())
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
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), user.getCompanyId());

        // Save and return response
        return ProductResponse.fromEntity(productRepository.save(product), mediaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(id, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // Fetch media for THIS product
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        // Return DTO with included image list
        return ProductResponse.fromEntity(product, mediaResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductByCode(String code) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

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
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

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
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

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
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

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
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }
        if (request.getProductUnitId() != null) {
            ProductUnit unit = productUnitRepository.findById(request.getProductUnitId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product unit not found: " + request.getProductUnitId()));
            product.setProductUnit(unit);
        }
        if (request.getSalesUnitId() != null) {
            ProductUnit salesUnit = productUnitRepository.findById(request.getSalesUnitId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Sales unit not found: " + request.getSalesUnitId()));
            product.setSalesUnit(salesUnit);
        }
        if (request.getPurchaseUnitId() != null) {
            ProductUnit purchaseUnit = productUnitRepository.findById(request.getPurchaseUnitId())
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

        product.setUpdatedBy(currentUserId);
        product.setUpdatedAt(LocalDateTime.now());

        // Fetch media for THIS product
        List<MediaResponse> mediaResponse = getProductImagesFromMedia(product.getId(), companyId);

        return ProductResponse.fromEntity(productRepository.save(product), mediaResponse);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

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
            boolean includeTax) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

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
            // Multi-warehouse mode — using groupingBy to avoid computeIfAbsent warnings
            if (includePrice) {
                Map<Long, List<ProductPriceResponse>> groupedPrices = productPriceRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .collect(Collectors.groupingBy(
                                price -> price.getProduct().getId(),
                                Collectors.mapping(ProductPriceResponse::fromEntity, Collectors.toList())));
                multiPriceMap.putAll(groupedPrices);
            }
            if (includeStock) {
                Map<Long, List<ProductStockResponse>> groupedStocks = productStockRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .collect(Collectors.groupingBy(
                                stock -> stock.getProduct().getId(),
                                Collectors.mapping(ProductStockResponse::fromEntity, Collectors.toList())));
                multiStockMap.putAll(groupedStocks);
            }
            if (includeTax) {
                Map<Long, List<ProductTaxResponse>> groupedTaxes = productTaxRepository
                        .findAllByProductIdInAndCompanyId(productIds, companyId)
                        .stream()
                        .collect(Collectors.groupingBy(
                                tax -> tax.getProduct().getId(),
                                Collectors.mapping(ProductTaxResponse::fromEntity, Collectors.toList())));
                multiTaxMap.putAll(groupedTaxes);
            }
        }

        // 3. Map products to response DTOs
        return products.stream().map(product -> {
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
    }

    private List<MediaResponse> getProductImagesFromMedia(Long productId, Long companyId) {
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities(
                "PRODUCT",
                Collections.singletonList(productId));

        List<MediaResponse> media = mediaMap.get(productId);

        return (media != null && !media.isEmpty()) ? media : Collections.emptyList();
    }
}
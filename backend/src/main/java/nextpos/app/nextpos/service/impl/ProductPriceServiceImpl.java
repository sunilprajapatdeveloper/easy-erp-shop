package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductPriceRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductPriceRequest;
import nextpos.app.nextpos.model.dto.response.ProductPriceResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductPrice;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductPriceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository; // used by UserContext

    @Override
    public ProductPriceResponse createProductPrice(CreateProductPriceRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        // validate product
        Product product = productRepository.findById(request.getProductId())
                .filter(p -> companyId.equals(p.getCompanyId()) && !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));

        // optional warehouse
        Warehouse warehouse = null;
        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findByIdAndCompanyIdAndIsDeletedFalse(request.getWarehouseId(), companyId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Warehouse not found with id: " + request.getWarehouseId()));
        }

        // currency
        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Currency not found with id: " + request.getCurrencyId()));

        String channel = request.getChannel() != null ? request.getChannel().trim() : null;

        // uniqueness check
        boolean exists;
        if (warehouse != null) {
            exists = productPriceRepository.existsByProductIdAndWarehouseIdAndChannelAndCompanyId(
                    product.getId(), warehouse.getId(), channel, companyId);
        } else {
            exists = productPriceRepository.existsByProductIdAndWarehouseIsNullAndChannelAndCompanyId(
                    product.getId(), channel, companyId);
        }
        if (exists) {
            throw new IllegalArgumentException("Price already exists for this product/warehouse/channel");
        }

        ProductPrice entity = ProductPrice.builder()
                .product(product)
                .warehouse(warehouse)
                .priceList(request.getPriceList() != null ? request.getPriceList().trim() : "DEFAULT")
                .channel(channel)
                .customerGroup(request.getCustomerGroup())
                .price(request.getPrice())
                .cost(request.getCost())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .currency(currency)
                .isActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE)
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .minQuantity(request.getMinQuantity() != null ? request.getMinQuantity() : 1)
                .maxQuantity(request.getMaxQuantity())
                .companyId(companyId)
                .createdBy(currentUserId)
                .createdAt(LocalDateTime.now())
                .build();

        ProductPrice saved = productPriceRepository.save(entity);
        return ProductPriceResponse.fromEntity(saved);
    }

    @Override
    public ProductPriceResponse updateProductPrice(Long priceId, UpdateProductPriceRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId();

        ProductPrice existing = productPriceRepository.findByIdAndCompanyId(priceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductPrice not found with id: " + priceId));

        // validate + update product
        if (request.getProductId() != null && !request.getProductId().equals(existing.getProduct().getId())) {
            Product newProduct = productRepository.findById(request.getProductId())
                    .filter(p -> companyId.equals(p.getCompanyId()) && !Boolean.TRUE.equals(p.getIsDeleted()))
                    .orElseThrow(
                            () -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));
            existing.setProduct(newProduct);
        }

        // validate + update warehouse
        if (request.getWarehouseId() != null) {
            if (existing.getWarehouse() == null || !request.getWarehouseId().equals(existing.getWarehouse().getId())) {
                Warehouse newWarehouse = warehouseRepository
                        .findByIdAndCompanyIdAndIsDeletedFalse(request.getWarehouseId(), companyId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Warehouse not found with id: " + request.getWarehouseId()));
                existing.setWarehouse(newWarehouse);
            }
        }

        // currency
        if (request.getCurrencyId() != null &&
                (existing.getCurrency() == null || !request.getCurrencyId().equals(existing.getCurrency().getId()))) {
            Currency newCurrency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Currency not found with id: " + request.getCurrencyId()));
            existing.setCurrency(newCurrency);
        }

        // update simple fields
        if (request.getPriceList() != null) {
            existing.setPriceList(request.getPriceList().trim());
        }
        if (request.getChannel() != null) {
            existing.setChannel(request.getChannel().trim());
        }
        if (request.getCustomerGroup() != null) {
            existing.setCustomerGroup(request.getCustomerGroup());
        }
        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }
        if (request.getCost() != null) {
            existing.setCost(request.getCost());
        }
        if (request.getMinPrice() != null) {
            existing.setMinPrice(request.getMinPrice());
        }
        if (request.getMaxPrice() != null) {
            existing.setMaxPrice(request.getMaxPrice());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }
        if (request.getValidFrom() != null) {
            existing.setValidFrom(request.getValidFrom());
        }
        if (request.getValidTo() != null) {
            existing.setValidTo(request.getValidTo());
        }
        if (request.getMinQuantity() != null) {
            existing.setMinQuantity(request.getMinQuantity());
        }
        if (request.getMaxQuantity() != null) {
            existing.setMaxQuantity(request.getMaxQuantity());
        }

        existing.setUpdatedBy(currentUserId);
        existing.setUpdatedAt(LocalDateTime.now());

        ProductPrice saved = productPriceRepository.save(existing);
        return ProductPriceResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPriceResponse getProductPriceById(Long priceId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        ProductPrice entity = productPriceRepository.findByIdAndCompanyId(priceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductPrice not found with id: " + priceId));
        return ProductPriceResponse.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPriceResponse> findEffectivePrice(Long productId, Long warehouseId, String channel) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        String ch = channel != null ? channel.trim() : null;

        if (warehouseId != null) {
            Optional<ProductPrice> exact = productPriceRepository
                    .findByProductIdAndWarehouseIdAndChannelAndCompanyId(productId, warehouseId, ch, companyId);
            if (exact.isPresent()) {
                return Optional.of(ProductPriceResponse.fromEntity(exact.get()));
            }
        }

        Optional<ProductPrice> global = productPriceRepository
                .findByProductIdAndWarehouseIsNullAndChannelAndCompanyId(productId, ch, companyId);
        return global.map(ProductPriceResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPriceResponse getByProductAndWarehouse(Long productId, Long warehouseId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        ProductPrice price = productPriceRepository
                .findByProductIdAndWarehouseIdAndCompanyId(productId, warehouseId, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProductPrice not found for productId=" + productId + " and warehouseId=" + warehouseId));
        return ProductPriceResponse.fromEntity(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceResponse> listPricesByProduct(Long productId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        return productPriceRepository.findAllByProductIdAndCompanyId(productId, companyId)
                .stream()
                .map(ProductPriceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceResponse> listPricesByWarehouse(Long warehouseId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        return productPriceRepository.findAllByWarehouseIdAndCompanyId(warehouseId, companyId)
                .stream()
                .map(ProductPriceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceResponse> listAllPrices() {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();

        return productPriceRepository.findAllByCompanyId(companyId)
                .stream()
                .map(ProductPriceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductPrice(Long priceId) {
        User user = UserContext.getAuthenticatedUser(userRepository);
        Long companyId = user.getCompanyId();
        Long currentUserId = user.getId(); // if needed for auditing

        ProductPrice existing = productPriceRepository.findByIdAndCompanyId(priceId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductPrice not found with id: " + priceId));
        productPriceRepository.delete(existing);
    }
}

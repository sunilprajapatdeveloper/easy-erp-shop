package nextpos.app.nextpos.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductStockRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateProductStockRequest;
import nextpos.app.nextpos.model.dto.response.ProductStockResponse;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.ProductStock;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.ProductStockRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStockServiceImpl implements ProductStockService {

    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    /**
     * Create a new product stock entry.
     */
    @Override
    @Transactional
    public ProductStockResponse createProductStock(CreateProductStockRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(request.getProductId(), companyId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));

        Warehouse warehouse = warehouseRepository
                .findByIdAndCompanyIdAndIsDeletedFalse(request.getWarehouseId(), companyId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        if (productStockRepository.existsByProductIdAndWarehouseIdAndCompanyId(product.getId(), warehouse.getId(),
                companyId)) {
            throw new IllegalArgumentException("ProductStock already exists for productId=" + product.getId()
                    + " and warehouseId=" + warehouse.getId());
        }

        ProductStock stock = ProductStock.builder()
                .product(product)
                .warehouse(warehouse)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .reservedQuantity(request.getReservedQuantity() != null ? request.getReservedQuantity() : 0)
                .inTransitQuantity(request.getInTransitQuantity() != null ? request.getInTransitQuantity() : 0)
                .committedQuantity(request.getCommittedQuantity() != null ? request.getCommittedQuantity() : 0)
                .minStockLevel(request.getMinStockLevel())
                .maxStockLevel(request.getMaxStockLevel())
                .reorderLevel(request.getReorderLevel())
                .stockAlert(false)
                .averageCost(request.getAverageCost())
                .lastCountDate(request.getLastCountDate())
                .nextCountDate(request.getNextCountDate())
                .companyId(companyId)
                .createdBy(currentUserId)
                .createdAt(LocalDateTime.now())
                .build();

        ProductStock saved = productStockRepository.save(stock);
        return ProductStockResponse.fromEntity(saved);
    }

    /**
     * Update an existing product stock entry.
     */
    @Override
    @Transactional
    public ProductStockResponse updateProductStock(UpdateProductStockRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        ProductStock stock = productStockRepository.findByIdAndCompanyId(request.getId(), companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductStock not found with id: " + request.getId()));

        // Update product if changed
        if (request.getProductId() != null && !request.getProductId().equals(stock.getProduct().getId())) {
            Product product = productRepository.findByIdAndCompanyIdAndIsDeletedFalse(request.getProductId(), companyId)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));
            stock.setProduct(product);
        }

        // Update warehouse if changed
        if (request.getWarehouseId() != null && !request.getWarehouseId().equals(stock.getWarehouse().getId())) {
            Warehouse warehouse = warehouseRepository
                    .findByIdAndCompanyIdAndIsDeletedFalse(request.getWarehouseId(), companyId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Warehouse not found with id: " + request.getWarehouseId()));
            if (productStockRepository.existsByProductIdAndWarehouseIdAndCompanyId(stock.getProduct().getId(),
                    warehouse.getId(), companyId)) {
                throw new IllegalArgumentException(
                        "ProductStock already exists for productId=" + stock.getProduct().getId()
                                + " and warehouseId=" + warehouse.getId());
            }
            stock.setWarehouse(warehouse);
        }

        // Quantities
        if (request.getQuantity() != null) {
            if (request.getQuantity() < 0)
                throw new IllegalArgumentException("Quantity cannot be negative");
            stock.setQuantity(request.getQuantity());
        }
        if (request.getReservedQuantity() != null) {
            if (request.getReservedQuantity() < 0)
                throw new IllegalArgumentException("Reserved quantity cannot be negative");
            stock.setReservedQuantity(request.getReservedQuantity());
        }
        if (request.getInTransitQuantity() != null) {
            if (request.getInTransitQuantity() < 0)
                throw new IllegalArgumentException("In-transit quantity cannot be negative");
            stock.setInTransitQuantity(request.getInTransitQuantity());
        }
        if (request.getCommittedQuantity() != null) {
            if (request.getCommittedQuantity() < 0)
                throw new IllegalArgumentException("Committed quantity cannot be negative");
            stock.setCommittedQuantity(request.getCommittedQuantity());
        }

        // Levels and costs
        if (request.getMinStockLevel() != null)
            stock.setMinStockLevel(request.getMinStockLevel());
        if (request.getMaxStockLevel() != null)
            stock.setMaxStockLevel(request.getMaxStockLevel());
        if (request.getReorderLevel() != null)
            stock.setReorderLevel(request.getReorderLevel());
        if (request.getAverageCost() != null)
            stock.setAverageCost(request.getAverageCost());
        if (request.getLastCountDate() != null)
            stock.setLastCountDate(request.getLastCountDate());
        if (request.getNextCountDate() != null)
            stock.setNextCountDate(request.getNextCountDate());

        stock.setUpdatedBy(currentUserId);
        stock.setUpdatedAt(LocalDateTime.now());

        ProductStock saved = productStockRepository.save(stock);
        return ProductStockResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockResponse getProductStockById(Long id) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductStock stock = productStockRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductStock not found with id: " + id));
        return ProductStockResponse.fromEntity(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockResponse getByProductAndWarehouse(Long productId, Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductStock stock = productStockRepository
                .findByProductIdAndWarehouseIdAndCompanyId(productId, warehouseId, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProductStock not found for productId=" + productId + " and warehouseId=" + warehouseId));
        return ProductStockResponse.fromEntity(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductStockResponse> listStocksByCompany() {
        Long companyId = UserContext.getCurrentCompanyId();

        return productStockRepository.findAllByCompanyId(companyId)
                .stream()
                .map(ProductStockResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductStockResponse adjustStock(Long productId, Long warehouseId, int delta) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long currentUserId = UserContext.getCurrentUserId();

        ProductStock stock = productStockRepository
                .findByProductIdAndWarehouseIdAndCompanyId(productId, warehouseId, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProductStock not found for productId=" + productId + " and warehouseId=" + warehouseId));

        int newQty = (stock.getQuantity() != null ? stock.getQuantity() : 0) + delta;
        if (newQty < 0)
            throw new IllegalArgumentException("Stock adjustment would make quantity negative");

        stock.setQuantity(newQty);
        stock.setUpdatedBy(currentUserId);
        stock.setUpdatedAt(LocalDateTime.now());

        ProductStock saved = productStockRepository.save(stock);
        return ProductStockResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteProductStock(Long id) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductStock stock = productStockRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("ProductStock not found with id: " + id));

        productStockRepository.delete(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public int getStock(Long productId, Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();

        ProductStock stock = productStockRepository
                .findByProductIdAndWarehouseIdAndCompanyId(productId, warehouseId, companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProductStock not found for productId=" + productId + " and warehouseId=" + warehouseId));
        return stock.getQuantity() != null ? stock.getQuantity() : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductStockResponse> listStocksByProduct(Long productId) {
        Long companyId = UserContext.getCurrentCompanyId();
        List<ProductStock> stocks = productStockRepository.findAllByProductIdAndCompanyId(productId, companyId);
        return stocks.stream()
                .map(ProductStockResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
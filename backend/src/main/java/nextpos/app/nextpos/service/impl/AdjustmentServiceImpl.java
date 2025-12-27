package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateAdjustmentRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentResponse;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Product;
import nextpos.app.nextpos.model.entity.Adjustment;
import nextpos.app.nextpos.model.entity.AdjustmentProduct;
import nextpos.app.nextpos.model.enums.StockEffect;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.AdjustmentService;
import nextpos.app.nextpos.service.interf.ProductStockService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdjustmentServiceImpl implements AdjustmentService {

        private final AdjustmentRepository adjustmentRepository;
        private final ProductRepository productRepository;
        private final WarehouseRepository warehouseRepository;
        private final UserRepository userRepository;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public AdjustmentResponse createAdjustment(CreateAdjustmentRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                Adjustment adjustment = Adjustment.builder()
                                .warehouse(warehouse)
                                .date(request.getDate())
                                .note(request.getNote())
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .companyId(user.getCompanyId())
                                .build();

                List<AdjustmentProduct> products = request.getProducts().stream()
                                .map(p -> {
                                        Product product = productRepository.findById(p.getProductId())
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Product not found with ID: "
                                                                                        + p.getProductId()));

                                        int currentQty = productStockService.getStock(user.getCompanyId(),
                                                        product.getId(),
                                                        warehouse.getId());
                                        int adjustedQty = p.getAdjustedQty();
                                        StockEffect stockEffect = p.getStockEffect();

                                        int delta = stockEffect == StockEffect.ADD ? adjustedQty : -adjustedQty;

                                        if (currentQty + delta < 0) {
                                                throw new RuntimeException("Insufficient stock for product ID: "
                                                                + p.getProductId());
                                        }

                                        productStockService.adjustStock(user.getCompanyId(), user.getId(),
                                                        product.getId(),
                                                        warehouse.getId(), delta);

                                        return AdjustmentProduct.builder()
                                                        .adjustment(adjustment)
                                                        .productId(p.getProductId())
                                                        .currentQty(currentQty)
                                                        .adjustedQty(adjustedQty)
                                                        .stockEffect(stockEffect)
                                                        .createdBy(user.getId())
                                                        .createdAt(LocalDateTime.now())
                                                        .companyId(user.getCompanyId())
                                                        .build();
                                })
                                .collect(Collectors.toList());

                adjustment.setProducts(products);
                Adjustment saved = adjustmentRepository.save(adjustment);

                return buildAdjustmentResponse(saved);
        }

        @Override
        public AdjustmentResponse getAdjustmentById(Long id) {
                Adjustment adj = adjustmentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Adjustment not found"));
                return buildAdjustmentResponse(adj);
        }

        @Override
        public List<AdjustmentResponse> getMyAdjustments() {
                User user = UserContext.getAuthenticatedUser(userRepository);
                Long userId = user.getId();

                List<Adjustment> adjustments = adjustmentRepository.findByCreatedBy(userId);
                return adjustments.stream()
                                .map(this::buildAdjustmentResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<AdjustmentResponse> getAllAdjustments() {
                User user = UserContext.getAuthenticatedUser(userRepository);
                Long companyId = user.getCompanyId();

                List<Adjustment> adjustments = adjustmentRepository.findByCompanyId(companyId);
                return adjustments.stream()
                                .map(this::buildAdjustmentResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public AdjustmentResponse updateAdjustment(Long id, CreateAdjustmentRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Adjustment adjustment = adjustmentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Adjustment not found"));

                // Revert stock from old adjustment products
                if (adjustment.getProducts() != null) {
                        for (AdjustmentProduct oldProduct : adjustment.getProducts()) {
                                int revertDelta = oldProduct.getStockEffect() == StockEffect.ADD
                                                ? -oldProduct.getAdjustedQty()
                                                : oldProduct.getAdjustedQty();

                                productStockService.adjustStock(user.getCompanyId(), user.getId(),
                                                oldProduct.getProductId(),
                                                adjustment.getWarehouse().getId(), revertDelta);
                        }
                }

                // Clear old products
                adjustment.getProducts().clear();

                // Build new list of updated products
                List<AdjustmentProduct> updatedProducts = request.getProducts().stream()
                                .map(p -> {
                                        Product product = productRepository.findById(p.getProductId())
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Product not found with ID: "
                                                                                        + p.getProductId()));

                                        int currentQty = productStockService.getStock(user.getCompanyId(),
                                                        product.getId(),
                                                        adjustment.getWarehouse().getId());
                                        int adjustedQty = p.getAdjustedQty();
                                        StockEffect stockEffect = p.getStockEffect();

                                        int delta = stockEffect == StockEffect.ADD ? adjustedQty : -adjustedQty;

                                        if (currentQty + delta < 0) {
                                                throw new RuntimeException("Insufficient stock for product ID: "
                                                                + p.getProductId());
                                        }

                                        productStockService.adjustStock(user.getCompanyId(), user.getId(),
                                                        product.getId(),
                                                        adjustment.getWarehouse().getId(), delta);

                                        return AdjustmentProduct.builder()
                                                        .adjustment(adjustment)
                                                        .productId(p.getProductId())
                                                        .currentQty(currentQty)
                                                        .adjustedQty(adjustedQty)
                                                        .stockEffect(stockEffect)
                                                        .updatedBy(user.getId())
                                                        .updatedAt(LocalDateTime.now())
                                                        .build();
                                })
                                .collect(Collectors.toList());

                adjustment.getProducts().addAll(updatedProducts);

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        adjustment.setWarehouse(warehouse);
                }

                adjustment.setNote(request.getNote());
                adjustment.setDate(request.getDate());
                adjustment.setUpdatedBy(user.getId());
                adjustment.setUpdatedAt(LocalDateTime.now());

                return buildAdjustmentResponse(adjustmentRepository.save(adjustment));
        }

        @Override
        @Transactional
        public void deleteAdjustment(Long id) {
                Adjustment adjustment = adjustmentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Adjustment not found"));

                // Revert stock before deletion
                if (adjustment.getProducts() != null) {
                        for (AdjustmentProduct tp : adjustment.getProducts()) {
                                int revertDelta = tp.getStockEffect() == StockEffect.ADD ? -tp.getAdjustedQty()
                                                : tp.getAdjustedQty();
                                productStockService.adjustStock(adjustment.getCompanyId(), tp.getCreatedBy(),
                                                tp.getProductId(),
                                                adjustment.getWarehouse().getId(), revertDelta);
                        }
                }

                adjustmentRepository.delete(adjustment);
        }

        private AdjustmentResponse buildAdjustmentResponse(Adjustment adj) {
                Warehouse warehouse = adj.getWarehouse();

                AdjustmentResponse.WarehouseSummary warehouseSummary = AdjustmentResponse.WarehouseSummary.builder()
                                .id(warehouse.getId())
                                .name(warehouse.getName())
                                .build();

                List<AdjustmentResponse.ProductDetail> productDetails = adj.getProducts().stream().map(p -> {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        return AdjustmentResponse.ProductDetail.builder()
                                        .id(product.getId())
                                        .code(product.getCode())
                                        .name(product.getName())
                                        .currentQty(p.getCurrentQty())
                                        .adjustedQty(p.getAdjustedQty())
                                        .stockEffect(p.getStockEffect())
                                        .build();
                }).collect(Collectors.toList());

                return AdjustmentResponse.builder()
                                .id(adj.getId())
                                .warehouse(warehouseSummary)
                                .date(adj.getDate())
                                .note(adj.getNote())
                                .createdBy(adj.getCreatedBy())
                                .createdAt(adj.getCreatedAt())
                                .updatedBy(adj.getUpdatedBy())
                                .updatedAt(adj.getUpdatedAt())
                                .companyId(adj.getCompanyId())
                                .products(productDetails)
                                .build();
        }
}

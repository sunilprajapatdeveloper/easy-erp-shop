package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateTransferRequest;
import nextpos.app.nextpos.model.dto.response.TransferResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ProductStockService;
import nextpos.app.nextpos.service.interf.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

        private final TransferRepository transferRepository;
        private final ProductRepository productRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductStockService productStockService;

        @Override
        @Transactional
        public TransferResponse createTransfer(CreateTransferRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Warehouse from = warehouseRepository.findById(request.getFromWarehouse())
                                .orElseThrow(() -> new RuntimeException("From warehouse not found"));

                Warehouse to = warehouseRepository.findById(request.getToWarehouse())
                                .orElseThrow(() -> new RuntimeException("To warehouse not found"));

                ShipmentStatus status = request.getStatus();

                Transfer transfer = new Transfer();
                transfer.setFromWarehouse(from);
                transfer.setToWarehouse(to);
                transfer.setDate(request.getDate());
                transfer.setNote(request.getNote());
                transfer.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                transfer.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                transfer.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));
                transfer.setStatus(status);
                transfer.setCreatedBy(currentUserId);
                transfer.setCreatedAt(LocalDateTime.now());
                transfer.setCompanyId(currentCompanyId);

                List<TransferProduct> products = request.getProducts().stream().map(p -> {
                        Product product = productRepository.findById(p.getProductId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        int currentStock = productStockService.getStock(product.getId(), from.getId());
                        int transferredQty = Optional.ofNullable(p.getTransferredQty()).orElse(0);
                        int newStock = currentStock - transferredQty;

                        if (newStock < 0) {
                                throw new RuntimeException("Insufficient stock for product ID: " + p.getProductId());
                        }

                        productStockService.adjustStock(product.getId(), from.getId(), -transferredQty);

                        TransferProduct tp = new TransferProduct();
                        tp.setTransfer(transfer);
                        tp.setProductId(p.getProductId());
                        tp.setProductCode(p.getProductCode());
                        tp.setProductUnitCost(Optional.ofNullable(p.getProductUnitCost()).orElse(BigDecimal.ZERO));
                        tp.setProductStock(currentStock);
                        tp.setTransferredQty(transferredQty);
                        tp.setProductDiscount(Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO));
                        tp.setProductTax(Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO));
                        tp.setSubTotal(Optional.ofNullable(p.getSubTotal()).orElse(BigDecimal.ZERO));
                        tp.setCreatedBy(currentUserId);
                        tp.setCreatedAt(LocalDateTime.now());
                        tp.setCompanyId(currentCompanyId);
                        return tp;
                }).collect(Collectors.toList());

                transfer.setProducts(products);

                BigDecimal totalSubTotal = products.stream()
                                .map(TransferProduct::getSubTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal orderTaxPercent = Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO);
                BigDecimal discountPercent = Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO);
                BigDecimal shippingCost = Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO);

                BigDecimal taxAmount = totalSubTotal.multiply(orderTaxPercent)
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                BigDecimal discountAmount = totalSubTotal.multiply(discountPercent)
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                BigDecimal grandTotal = totalSubTotal
                                .add(taxAmount)
                                .add(shippingCost)
                                .subtract(discountAmount)
                                .setScale(2, RoundingMode.HALF_UP);

                transfer.setGrandTotal(grandTotal);

                Transfer saved = transferRepository.save(transfer);
                return new TransferResponse(saved);
        }

        @Override
        public TransferResponse getTransferById(Long id) {
                Transfer transfer = transferRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Transfer not found"));
                return new TransferResponse(transfer);
        }

        @Override
        public List<TransferResponse> getMyTransfer() {
                Long currentUserId = UserContext.getCurrentUserId();
                List<Transfer> transfers = transferRepository.findByCreatedBy(currentUserId);
                return transfers.stream().map(TransferResponse::new).collect(Collectors.toList());
        }

        @Override
        public List<TransferResponse> getAllTransfer() {
                Long currentCompanyId = UserContext.getCurrentCompanyId();
                List<Transfer> transfers = transferRepository.findByCompanyId(currentCompanyId);
                return transfers.stream().map(TransferResponse::new).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public TransferResponse updateTransfer(Long id, CreateTransferRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Transfer transfer = transferRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Transfer not found"));

                // Revert stock from existing products
                for (TransferProduct tp : transfer.getProducts()) {
                        productStockService.adjustStock(tp.getProductId(), transfer.getFromWarehouse().getId(),
                                        tp.getTransferredQty());
                }

                transfer.getProducts().clear();

                Warehouse from = warehouseRepository.findById(request.getFromWarehouse())
                                .orElseThrow(() -> new RuntimeException("From warehouse not found"));

                Warehouse to = warehouseRepository.findById(request.getToWarehouse())
                                .orElseThrow(() -> new RuntimeException("To warehouse not found"));

                List<TransferProduct> updatedProducts = request.getProducts().stream().map(p -> {
                        int transferredQty = Optional.ofNullable(p.getTransferredQty()).orElse(0);
                        int currentStock = productStockService.getStock(p.getProductId(), from.getId());

                        if (currentStock < transferredQty) {
                                throw new RuntimeException("Insufficient stock for product ID: " + p.getProductId());
                        }

                        productStockService.adjustStock(p.getProductId(), from.getId(), -transferredQty);

                        TransferProduct tp = new TransferProduct();
                        tp.setTransfer(transfer);
                        tp.setProductId(p.getProductId());
                        tp.setProductCode(p.getProductCode());
                        tp.setProductUnitCost(Optional.ofNullable(p.getProductUnitCost()).orElse(BigDecimal.ZERO));
                        tp.setProductStock(currentStock);
                        tp.setTransferredQty(transferredQty);
                        tp.setProductDiscount(Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO));
                        tp.setProductTax(Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO));
                        tp.setSubTotal(Optional.ofNullable(p.getSubTotal()).orElse(BigDecimal.ZERO));
                        tp.setUpdatedBy(currentUserId);
                        tp.setUpdatedAt(LocalDateTime.now());
                        tp.setCompanyId(currentCompanyId);
                        return tp;
                }).collect(Collectors.toList());

                transfer.getProducts().addAll(updatedProducts);

                transfer.setFromWarehouse(from);
                transfer.setToWarehouse(to);
                transfer.setDate(request.getDate());
                transfer.setNote(request.getNote());
                transfer.setStatus(request.getStatus());
                transfer.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                transfer.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                transfer.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));
                transfer.setUpdatedBy(currentUserId);
                transfer.setUpdatedAt(LocalDateTime.now());

                BigDecimal totalSubTotal = updatedProducts.stream()
                                .map(TransferProduct::getSubTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal taxAmount = totalSubTotal.multiply(transfer.getOrderTax())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                BigDecimal discountAmount = totalSubTotal.multiply(transfer.getDiscount())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                BigDecimal grandTotal = totalSubTotal
                                .add(taxAmount)
                                .add(transfer.getShippingCost())
                                .subtract(discountAmount)
                                .setScale(2, RoundingMode.HALF_UP);

                transfer.setGrandTotal(grandTotal);

                Transfer saved = transferRepository.save(transfer);
                return new TransferResponse(saved);
        }

        @Override
        @Transactional
        public void deleteTransfer(Long id) {
                Transfer transfer = transferRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Transfer not found"));

                for (TransferProduct tp : transfer.getProducts()) {
                        productStockService.adjustStock(tp.getProductId(), transfer.getFromWarehouse().getId(),
                                        tp.getTransferredQty());
                }

                transferRepository.delete(transfer);
        }
}

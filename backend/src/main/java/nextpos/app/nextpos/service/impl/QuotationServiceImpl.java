package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateQuotationRequest;
import nextpos.app.nextpos.model.dto.response.QuotationResponse;
import nextpos.app.nextpos.model.dto.response.QuotationResponse.ProductDetail;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.QuotationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

        private final QuotationRepository quotationRepository;
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final WarehouseAccessService warehouseAccessService;

        @Override
        @Transactional
        public QuotationResponse createQuotation(CreateQuotationRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();

                Customer customer = customerRepository.findByIdAndCompanyId(request.getCustomerId(), companyId)
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());

                ShipmentStatus status = request.getStatus();

                Quotation quotation = new Quotation();
                quotation.setCustomerId(customer.getId());
                quotation.setWarehouseId(warehouse.getId());
                quotation.setNote(request.getNote());
                quotation.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                quotation.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                quotation.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));
                quotation.setStatus(status);
                quotation.setCreatedBy(currentUserId);
                quotation.setCompanyId(companyId);

                List<QuotationProduct> products = request.getProducts().stream().map(p -> {
                        QuotationProduct qp = new QuotationProduct();

                        Product product = productRepository
                                        .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), companyId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        qp.setQuotation(quotation);
                        qp.setProduct(product);
                        qp.setProductCode(p.getProductCode());
                        qp.setProductUnitCost(Optional.ofNullable(p.getProductUnitCost()).orElse(BigDecimal.ZERO));
                        qp.setProductStock(Optional.ofNullable(p.getProductStock()).orElse(0));
                        qp.setQuantity(Optional.ofNullable(p.getQuantity()).orElse(0));
                        qp.setProductDiscount(Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO));
                        qp.setProductTax(Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO));
                        qp.setSubTotal(Optional.ofNullable(p.getSubTotal()).orElse(BigDecimal.ZERO));
                        return qp;
                }).collect(Collectors.toList());

                quotation.setProducts(products);
                Quotation saved = quotationRepository.save(quotation);

                List<ProductDetail> productDetails = saved.getProducts().stream()
                                .map(ProductDetail::new)
                                .collect(Collectors.toList());

                return new QuotationResponse(saved, productDetails);
        }

        @Override
        public QuotationResponse getQuotationById(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                Quotation quotation = quotationRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));
                warehouseAccessService.requireAccessible(quotation.getWarehouseId());

                List<ProductDetail> productDetails = quotation.getProducts().stream()
                                .map(ProductDetail::new)
                                .collect(Collectors.toList());

                return new QuotationResponse(quotation, productDetails);
        }

        @Override
        @Transactional
        public QuotationResponse updateQuotation(Long id, CreateQuotationRequest request) {
                Long companyId = UserContext.getCurrentCompanyId();
                Quotation quotation = quotationRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));
                warehouseAccessService.requireAccessible(quotation.getWarehouseId());

                quotation.getProducts().clear();

                List<QuotationProduct> updatedProducts = request.getProducts().stream().map(p -> {
                        QuotationProduct qp = new QuotationProduct();

                        Product product = productRepository
                                        .findByIdAndCompanyIdAndIsDeletedFalse(p.getProductId(), companyId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found with ID: " + p.getProductId()));

                        qp.setQuotation(quotation);
                        qp.setProduct(product);
                        qp.setProductCode(p.getProductCode());
                        qp.setProductUnitCost(Optional.ofNullable(p.getProductUnitCost()).orElse(BigDecimal.ZERO));
                        qp.setProductStock(Optional.ofNullable(p.getProductStock()).orElse(0));
                        qp.setQuantity(Optional.ofNullable(p.getQuantity()).orElse(0));
                        qp.setProductDiscount(Optional.ofNullable(p.getProductDiscount()).orElse(BigDecimal.ZERO));
                        qp.setProductTax(Optional.ofNullable(p.getProductTax()).orElse(BigDecimal.ZERO));
                        qp.setSubTotal(Optional.ofNullable(p.getSubTotal()).orElse(BigDecimal.ZERO));
                        return qp;
                }).collect(Collectors.toList());

                ShipmentStatus status = request.getStatus();

                quotation.setProducts(updatedProducts);
                quotation.setNote(request.getNote());
                quotation.setStatus(status);
                quotation.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                quotation.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                quotation.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));

                if (request.getCustomerId() != null) {
                        Customer customer = customerRepository.findByIdAndCompanyId(request.getCustomerId(), companyId)
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                        quotation.setCustomerId(customer.getId());
                }

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                        quotation.setWarehouseId(warehouse.getId());
                }

                Long currentUserId = UserContext.getCurrentUserId();
                quotation.setUpdatedBy(currentUserId);

                Quotation saved = quotationRepository.save(quotation);

                List<ProductDetail> productDetails = saved.getProducts().stream()
                                .map(ProductDetail::new)
                                .collect(Collectors.toList());

                return new QuotationResponse(saved, productDetails);
        }

        @Override
        @Transactional
        public void deleteQuotation(Long id) {
                Long companyId = UserContext.getCurrentCompanyId();
                Quotation quotation = quotationRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));
                warehouseAccessService.requireAccessible(quotation.getWarehouseId());
                quotationRepository.delete(quotation);
        }
}

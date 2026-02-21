package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateQuotationRequest;
import nextpos.app.nextpos.model.dto.response.QuotationResponse;
import nextpos.app.nextpos.model.dto.response.QuotationResponse.ProductDetail;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.QuotationService;
import org.springframework.security.core.context.SecurityContextHolder;
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
        private final UserRepository userRepository;
        private final CustomerRepository customerRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;

        @Override
        @Transactional
        public QuotationResponse createQuotation(CreateQuotationRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

                ShipmentStatus status = request.getStatus();

                Quotation quotation = new Quotation();
                quotation.setCustomerId(customer.getId());
                quotation.setWarehouseId(warehouse.getId());
                quotation.setNote(request.getNote());
                quotation.setOrderTax(Optional.ofNullable(request.getOrderTax()).orElse(BigDecimal.ZERO));
                quotation.setDiscount(Optional.ofNullable(request.getDiscount()).orElse(BigDecimal.ZERO));
                quotation.setShippingCost(Optional.ofNullable(request.getShippingCost()).orElse(BigDecimal.ZERO));
                quotation.setStatus(status);
                quotation.setCreatedBy(user.getId());

                List<QuotationProduct> products = request.getProducts().stream().map(p -> {
                        QuotationProduct qp = new QuotationProduct();

                        Product product = productRepository.findById(p.getProductId())
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
                Quotation quotation = quotationRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));

                List<ProductDetail> productDetails = quotation.getProducts().stream()
                                .map(ProductDetail::new)
                                .collect(Collectors.toList());

                return new QuotationResponse(quotation, productDetails);
        }

        @Override
        @Transactional
        public QuotationResponse updateQuotation(Long id, CreateQuotationRequest request) {
                Quotation quotation = quotationRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));

                quotation.getProducts().clear();

                List<QuotationProduct> updatedProducts = request.getProducts().stream().map(p -> {
                        QuotationProduct qp = new QuotationProduct();

                        Product product = productRepository.findById(qp.getProduct().getId())
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
                        Customer customer = customerRepository.findById(request.getCustomerId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                        quotation.setCustomerId(customer.getId());
                }

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException("Warehouse not found"));
                        quotation.setWarehouseId(warehouse.getId());
                }

                User user = UserContext.getAuthenticatedUser(userRepository);

                quotation.setUpdatedBy(user.getId());

                Quotation saved = quotationRepository.save(quotation);

                List<ProductDetail> productDetails = saved.getProducts().stream()
                                .map(ProductDetail::new)
                                .collect(Collectors.toList());

                return new QuotationResponse(saved, productDetails);
        }

        @Override
        @Transactional
        public void deleteQuotation(Long id, Long deletedByUserId) {
                Quotation quotation = quotationRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Quotation not found"));
                quotationRepository.delete(quotation);
        }
}

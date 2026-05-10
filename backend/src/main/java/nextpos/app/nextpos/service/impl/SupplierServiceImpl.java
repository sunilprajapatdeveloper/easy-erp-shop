package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSupplierRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSupplierRequest;
import nextpos.app.nextpos.model.dto.response.SupplierResponse;
import nextpos.app.nextpos.model.entity.Supplier;
import nextpos.app.nextpos.repository.SupplierRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.SupplierService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

        private final SupplierRepository supplierRepository;

        @Override
        @Transactional
        public SupplierResponse createSupplier(CreateSupplierRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Supplier supplier = Supplier.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .phone(request.getPhone())
                                .country(request.getCountry())
                                .city(request.getCity())
                                .address(request.getAddress())
                                .taxNumber(request.getTaxNumber())
                                .createdBy(currentUserId)
                                .createdAt(LocalDateTime.now())
                                .companyId(currentCompanyId)
                                .build();

                Supplier saved = supplierRepository.save(supplier);
                return new SupplierResponse(saved);
        }

        @Override
        public SupplierResponse getSupplierById(Long id) {
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

                // Ensure supplier belongs to the user's company
                if (!supplier.getCompanyId().equals(currentCompanyId)) {
                        throw new RuntimeException("Access denied to supplier with id: " + id);
                }

                return new SupplierResponse(supplier);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SupplierResponse> getMySuppliers() {
                Long currentUserId = UserContext.getCurrentUserId();

                List<Supplier> suppliers = supplierRepository.findByCreatedBy(currentUserId);

                return suppliers.stream()
                                .map(SupplierResponse::new)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<SupplierResponse> getAllSuppliers() {
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                List<Supplier> suppliers = supplierRepository.findByCompanyId(currentCompanyId);

                return suppliers.stream()
                                .map(SupplierResponse::new)
                                .toList();
        }

        @Override
        @Transactional
        public SupplierResponse updateSupplier(Long id, UpdateSupplierRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

                // Ensure supplier belongs to the user's company
                if (!supplier.getCompanyId().equals(currentCompanyId)) {
                        throw new RuntimeException("Access denied to supplier with id: " + id);
                }

                // Update only provided fields
                if (request.getName() != null)
                        supplier.setName(request.getName());
                if (request.getEmail() != null)
                        supplier.setEmail(request.getEmail());
                if (request.getPhone() != null)
                        supplier.setPhone(request.getPhone());
                if (request.getCountry() != null)
                        supplier.setCountry(request.getCountry());
                if (request.getCity() != null)
                        supplier.setCity(request.getCity());
                if (request.getAddress() != null)
                        supplier.setAddress(request.getAddress());
                if (request.getTaxNumber() != null)
                        supplier.setTaxNumber(request.getTaxNumber());

                supplier.setUpdatedBy(currentUserId);
                supplier.setUpdatedAt(LocalDateTime.now());
                supplier.setCompanyId(currentCompanyId);

                Supplier updated = supplierRepository.save(supplier);
                return new SupplierResponse(updated);
        }

        @Override
        @Transactional
        public void deleteSupplier(Long id) {
                Long currentCompanyId = UserContext.getCurrentCompanyId();

                Supplier supplier = supplierRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

                // Ensure supplier belongs to the user's company
                if (!supplier.getCompanyId().equals(currentCompanyId)) {
                        throw new RuntimeException("Access denied to supplier with id: " + id);
                }

                supplierRepository.delete(supplier);
        }
}

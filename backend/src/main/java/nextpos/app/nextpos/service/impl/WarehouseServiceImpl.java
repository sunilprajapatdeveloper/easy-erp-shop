package nextpos.app.nextpos.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.WarehouseService;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

        private final WarehouseRepository warehouseRepository;
        private final CurrencyRepository currencyRepository;
        private final WarehouseAccessService warehouseAccessService;

        @Override
        @Transactional
        public WarehouseResponse createWarehouse(CreateWarehouseRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();

                // Check uniqueness of warehouse name within the company
                warehouseRepository.findByNameAndCompanyIdAndIsDeletedFalse(request.getName(), companyId)
                                .ifPresent(w -> {
                                        throw new RuntimeException("Warehouse with name '" + request.getName()
                                                        + "' already exists in this company");
                                });

                Currency currency = currencyRepository.findById(request.getCurrencyId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Currency not found with id " + request.getCurrencyId()));

                Warehouse warehouse = Warehouse.builder()
                                .name(request.getName())
                                .phone(request.getPhone())
                                .email(request.getEmail())
                                .addressLine1(request.getAddressLine1())
                                .addressLine2(request.getAddressLine2())
                                .city(request.getCity())
                                .state(request.getState())
                                .country(request.getCountry())
                                .zipCode(request.getZipCode())
                                .headquarter(Boolean.TRUE.equals(request.getHeadquarter()))
                                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                                .active(request.getActive() != null ? request.getActive() : true)
                                .applyTax(request.getApplyTax() != null ? request.getApplyTax() : true)
                                .applyTds(request.getApplyTds() != null ? request.getApplyTds() : false)
                                .trackInventory(request.getTrackInventory() != null ? request.getTrackInventory()
                                                : true)
                                .invoicePrefix(request.getInvoicePrefix())
                                .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                                .currency(currency)
                                .companyId(companyId)
                                .createdBy(currentUserId)
                                .build();

                // If this warehouse is marked as default, ensure no other default exists for
                // the company
                if (warehouse.isDefault()) {
                        warehouseRepository.findByCompanyIdAndIsDefaultTrueAndIsDeletedFalse(companyId)
                                        .ifPresent(existingDefault -> {
                                                existingDefault.setDefault(false);
                                                warehouseRepository.save(existingDefault);
                                        });
                }

                Warehouse saved = warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] created by user [{}] in company [{}]",
                                saved.getId(), currentUserId, companyId);

                return new WarehouseResponse(saved);
        }

        @Override
        public WarehouseResponse getWarehouseById(Long id) {
                Warehouse warehouse = warehouseAccessService.requireAccessible(id);

                return new WarehouseResponse(warehouse);
        }

        @Override
        public List<WarehouseResponse> getAllWarehouses() {
                return warehouseAccessService.accessibleWarehouses()
                                .stream()
                                .map(WarehouseResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<WarehouseResponse> findAllByCreatedBy(Long userId) {
                Long companyId = UserContext.getCurrentCompanyId();

                return warehouseRepository
                                .findAllByCreatedByAndCompanyIdAndIsDeletedFalse(userId, companyId)
                                .stream()
                                .filter(warehouse -> UserContext.canAccessWarehouse(warehouse.getId())
                                                || UserContext.getAuthenticatedUser().getAuthorities().stream()
                                                                .anyMatch(a -> "ROLE_COMPANY_OWNER".equals(a.getAuthority())))
                                .map(WarehouseResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public WarehouseResponse updateWarehouse(Long id, UpdateWarehouseRequest request) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();

                Warehouse warehouse = warehouseAccessService.requireAccessible(id);

                // If name is being updated, check uniqueness
                if (request.getName() != null && !request.getName().equals(warehouse.getName())) {
                        warehouseRepository.findByNameAndCompanyIdAndIsDeletedFalse(request.getName(), companyId)
                                        .ifPresent(w -> {
                                                throw new RuntimeException("Warehouse with name '" + request.getName()
                                                                + "' already exists in this company");
                                        });
                        warehouse.setName(request.getName());
                }

                // Partial updates
                if (request.getPhone() != null)
                        warehouse.setPhone(request.getPhone());
                if (request.getEmail() != null)
                        warehouse.setEmail(request.getEmail());
                if (request.getAddressLine1() != null)
                        warehouse.setAddressLine1(request.getAddressLine1());
                if (request.getAddressLine2() != null)
                        warehouse.setAddressLine2(request.getAddressLine2());
                if (request.getCity() != null)
                        warehouse.setCity(request.getCity());
                if (request.getState() != null)
                        warehouse.setState(request.getState());
                if (request.getCountry() != null)
                        warehouse.setCountry(request.getCountry());
                if (request.getZipCode() != null)
                        warehouse.setZipCode(request.getZipCode());
                if (request.getHeadquarter() != null)
                        warehouse.setHeadquarter(request.getHeadquarter());
                if (request.getIsDefault() != null) {
                        boolean newDefault = request.getIsDefault();
                        if (newDefault && !warehouse.isDefault()) {
                                // unset any existing default
                                warehouseRepository.findByCompanyIdAndIsDefaultTrueAndIsDeletedFalse(companyId)
                                                .ifPresent(existingDefault -> {
                                                        existingDefault.setDefault(false);
                                                        warehouseRepository.save(existingDefault);
                                                });
                        }
                        warehouse.setDefault(newDefault);
                }
                if (request.getActive() != null)
                        warehouse.setActive(request.getActive());
                if (request.getApplyTax() != null)
                        warehouse.setApplyTax(request.getApplyTax());
                if (request.getApplyTds() != null)
                        warehouse.setApplyTds(request.getApplyTds());
                if (request.getTrackInventory() != null)
                        warehouse.setTrackInventory(request.getTrackInventory());
                if (request.getInvoicePrefix() != null)
                        warehouse.setInvoicePrefix(request.getInvoicePrefix());
                if (request.getTimezone() != null)
                        warehouse.setTimezone(request.getTimezone());

                if (request.getCurrencyId() != null) {
                        Currency currency = currencyRepository.findById(request.getCurrencyId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Currency not found with id " + request.getCurrencyId()));
                        warehouse.setCurrency(currency);
                }

                warehouse.setUpdatedBy(currentUserId);

                Warehouse saved = warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] updated by user [{}] in company [{}]",
                                saved.getId(), currentUserId, companyId);

                return new WarehouseResponse(saved);
        }

        @Override
        @Transactional
        public void deleteWarehouse(Long id) {
                Long currentUserId = UserContext.getCurrentUserId();
                Long companyId = UserContext.getCurrentCompanyId();

                Warehouse warehouse = warehouseAccessService.requireAccessible(id);

                warehouse.setDeleted(true);
                warehouse.setUpdatedBy(currentUserId);

                warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] soft-deleted by user [{}] in company [{}]",
                                id, currentUserId, companyId);
        }
}

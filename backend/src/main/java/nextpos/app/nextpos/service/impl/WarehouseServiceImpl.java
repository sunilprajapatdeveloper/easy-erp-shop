package nextpos.app.nextpos.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateWarehouseRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateWarehouseRequest;
import nextpos.app.nextpos.model.dto.response.WarehouseResponse;
import nextpos.app.nextpos.model.entity.Currency;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.WarehouseService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

        private final WarehouseRepository warehouseRepository;
        private final UserRepository userRepository;
        private final CurrencyRepository currencyRepository;

        @Override
        @Transactional
        public WarehouseResponse createWarehouse(CreateWarehouseRequest request) {
                User createdBy = UserContext.getAuthenticatedUser(userRepository);

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
                                .active(Boolean.TRUE.equals(request.getActive()))
                                .applyTax(Boolean.TRUE.equals(request.getApplyTax()))
                                .applyTds(Boolean.TRUE.equals(request.getApplyTds()))
                                .trackInventory(Boolean.TRUE.equals(request.getTrackInventory()))
                                .invoicePrefix(request.getInvoicePrefix())
                                .timezone(request.getTimezone())
                                .currency(currency)
                                .companyId(request.getCompanyId() != null ? request.getCompanyId()
                                                : createdBy.getCompanyId())
                                .createdBy(createdBy.getId())
                                .build();

                Warehouse saved = warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] created by user [{}] in company [{}]",
                                saved.getId(), createdBy.getId(), saved.getCompanyId());

                return new WarehouseResponse(saved);
        }

        @Override
        public WarehouseResponse getWarehouseById(Long id) {
                User currentUser = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository
                                .findByIdAndCompanyIdAndIsDeletedFalse(id, currentUser.getCompanyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id " + id));

                return new WarehouseResponse(warehouse);
        }

        @Override
        public List<WarehouseResponse> getAllWarehouses() {
                User currentUser = UserContext.getAuthenticatedUser(userRepository);

                return warehouseRepository.findAllByCompanyIdAndIsDeletedFalse(currentUser.getCompanyId())
                                .stream()
                                .map(WarehouseResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        public List<WarehouseResponse> findAllByCreatedBy(Long userId) {
                User currentUser = UserContext.getAuthenticatedUser(userRepository);

                return warehouseRepository
                                .findAllByCreatedByAndCompanyIdAndIsDeletedFalse(userId, currentUser.getCompanyId())
                                .stream()
                                .map(WarehouseResponse::new)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public WarehouseResponse updateWarehouse(Long id, UpdateWarehouseRequest request) {
                User updatedBy = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository
                                .findByIdAndCompanyIdAndIsDeletedFalse(id, updatedBy.getCompanyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id " + id));

                // Partial updates
                if (request.getName() != null)
                        warehouse.setName(request.getName());
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
                if (request.getIsDefault() != null)
                        warehouse.setDefault(request.getIsDefault());
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

                warehouse.setUpdatedBy(updatedBy.getId());

                Warehouse saved = warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] updated by user [{}] in company [{}]",
                                saved.getId(), updatedBy.getId(), saved.getCompanyId());

                return new WarehouseResponse(saved);
        }

        @Override
        @Transactional
        public void deleteWarehouse(Long id) {
                User currentUser = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository
                                .findByIdAndCompanyIdAndIsDeletedFalse(id, currentUser.getCompanyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id " + id));

                warehouse.setDeleted(true);
                warehouse.setUpdatedBy(currentUser.getId());

                warehouseRepository.save(warehouse);
                log.info("Warehouse [{}] soft-deleted by user [{}] in company [{}]",
                                id, currentUser.getId(), warehouse.getCompanyId());
        }
}

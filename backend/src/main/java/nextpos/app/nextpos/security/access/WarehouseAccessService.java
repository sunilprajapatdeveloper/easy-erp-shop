package nextpos.app.nextpos.security.access;

import java.util.List;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.exception.ResourceNotFoundException;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/** Authoritative warehouse ownership and assignment boundary. */
@Service("warehouseAccess")
@RequiredArgsConstructor
public class WarehouseAccessService {

    private static final String COMPANY_OWNER = "ROLE_COMPANY_OWNER";
    private final WarehouseRepository warehouseRepository;

    public Warehouse requireAccessible(Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();
        Warehouse warehouse = warehouseRepository
                .findByIdAndCompanyIdAndIsDeletedFalse(warehouseId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        requireAssignment(warehouseId);
        return warehouse;
    }

    public void requireAssignment(Long warehouseId) {
        boolean owner = UserContext.getAuthenticatedUser().getAuthorities().stream()
                .anyMatch(authority -> COMPANY_OWNER.equals(authority.getAuthority()));
        if (!owner && !UserContext.canAccessWarehouse(warehouseId)) {
            throw new AccessDeniedException("User is not assigned to this warehouse");
        }
    }

    public List<Warehouse> accessibleWarehouses() {
        Long companyId = UserContext.getCurrentCompanyId();
        List<Warehouse> tenantWarehouses = warehouseRepository.findAllByCompanyIdAndIsDeletedFalse(companyId);
        boolean owner = UserContext.getAuthenticatedUser().getAuthorities().stream()
                .anyMatch(authority -> COMPANY_OWNER.equals(authority.getAuthority()));
        if (owner) {
            return tenantWarehouses;
        }
        return tenantWarehouses.stream()
                .filter(warehouse -> UserContext.canAccessWarehouse(warehouse.getId()))
                .toList();
    }
}

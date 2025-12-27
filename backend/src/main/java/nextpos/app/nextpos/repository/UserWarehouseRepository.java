package nextpos.app.nextpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nextpos.app.nextpos.model.entity.UserWarehouse;

@Repository
public interface UserWarehouseRepository extends JpaRepository<UserWarehouse, Long> {

    // Get all warehouse mappings for a user
    List<UserWarehouse> findByUserId(Long userId);

    // Get all user mappings for a warehouse
    List<UserWarehouse> findByWarehouseId(Long warehouseId);

    // Check if mapping exists
    Optional<UserWarehouse> findByUserIdAndWarehouseId(Long userId, Long warehouseId);

    // Delete mappings by userId
    void deleteByUserId(Long userId);

    // Delete mappings by warehouseId
    void deleteByWarehouseId(Long warehouseId);
}

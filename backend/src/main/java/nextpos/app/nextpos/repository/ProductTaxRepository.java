package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.ProductTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTaxRepository extends JpaRepository<ProductTax, Long> {

        Optional<ProductTax> findByIdAndCompanyId(Long id, Long companyId);

        List<ProductTax> findAllByProductIdAndCompanyId(Long productId, Long companyId);

        List<ProductTax> findAllByWarehouseIdAndCompanyId(Long warehouseId, Long companyId);

        List<ProductTax> findAllByCompanyId(Long companyId);

        boolean existsByProductIdAndWarehouseIdAndTaxCodeAndCompanyId(
                        Long productId, Long warehouseId, String taxCode, Long companyId);

        boolean existsByProductIdAndWarehouseIsNullAndTaxCodeAndCompanyId(
                        Long productId, String taxCode, Long companyId);

        Optional<ProductTax> findByProductIdAndWarehouseIdAndCompanyId(
                        Long productId,
                        Long warehouseId,
                        Long companyId);

        List<ProductTax> findAllByProductIdInAndWarehouseIdAndCompanyId(
                        List<Long> productIds, Long warehouseId, Long companyId);

        List<ProductTax> findAllByProductIdInAndCompanyId(
                        List<Long> productIds, Long companyId);
}

package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByIdAndIsDeletedFalse(Long id);

    Page<Company> findAllByIsDeletedFalse(Pageable pageable);
}
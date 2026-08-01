package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    Optional<Expenses> findByIdAndCompanyId(Long id, Long companyId);
    
}

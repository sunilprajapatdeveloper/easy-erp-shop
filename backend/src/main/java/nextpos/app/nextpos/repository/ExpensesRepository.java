package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    
}
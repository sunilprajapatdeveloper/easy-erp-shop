package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateExpensesRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExpensesRequest;
import nextpos.app.nextpos.model.dto.response.ExpensesResponse;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.entity.Expenses;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.ExpensesRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ExpensesService;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl implements ExpensesService {

        private final ExpensesRepository expensesRepository;
        private final WarehouseRepository warehouseRepository;
        private final CategoryRepository categoryRepository;
        private final WarehouseAccessService warehouseAccessService;

        @Override
        @Transactional
        public ExpensesResponse createExpenses(CreateExpensesRequest request) {
                Long companyId = UserContext.getCurrentCompanyId();
                Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());

                Category category = categoryRepository.findByIdAndCompanyId(request.getCategoryId(), companyId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Category not found with ID: " + request.getCategoryId()));

                Expenses expenses = Expenses.builder()
                                .warehouse(warehouse)
                                .category(category)
                                .date(request.getDate())
                                .amount(request.getAmount())
                                .details(request.getDetails())
                                .createdBy(UserContext.getCurrentUserId())
                                .createdAt(LocalDateTime.now())
                                .companyId(companyId)
                                .build();

                return new ExpensesResponse(expensesRepository.save(expenses));
        }

        @Override
        public ExpensesResponse getExpensesById(Long id) {
                Expenses expenses = expensesRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));
                warehouseAccessService.requireAssignment(expenses.getWarehouse().getId());
                return new ExpensesResponse(expenses);
        }

        @Override
        @Transactional
        public ExpensesResponse updateExpenses(Long id, UpdateExpensesRequest request) {
                Long companyId = UserContext.getCurrentCompanyId();
                Expenses expenses = expensesRepository.findByIdAndCompanyId(id, companyId)
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));
                warehouseAccessService.requireAssignment(expenses.getWarehouse().getId());

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());
                        expenses.setWarehouse(warehouse);
                }

                if (request.getCategoryId() != null) {
                        Category category = categoryRepository.findByIdAndCompanyId(request.getCategoryId(), companyId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Category not found with ID: " + request.getCategoryId()));
                        expenses.setCategory(category);
                }

                if (request.getDate() != null) {
                        expenses.setDate(request.getDate());
                }

                if (request.getAmount() != null) {
                        expenses.setAmount(request.getAmount());
                }

                expenses.setDetails(request.getDetails());
                expenses.setUpdatedBy(UserContext.getCurrentUserId());
                expenses.setUpdatedAt(LocalDateTime.now());

                return new ExpensesResponse(expensesRepository.save(expenses));
        }

        @Override
        @Transactional
        public void deleteExpenses(Long id) {
                Expenses expenses = expensesRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));
                warehouseAccessService.requireAssignment(expenses.getWarehouse().getId());

                // Ensure the expense belongs to the user's company
                if (!expenses.getCompanyId().equals(UserContext.getCurrentCompanyId())) {
                        throw new SecurityException("You cannot delete expenses from another company");
                }

                expensesRepository.delete(expenses);
        }
}

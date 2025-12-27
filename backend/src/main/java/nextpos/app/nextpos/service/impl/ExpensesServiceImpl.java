package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateExpensesRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExpensesRequest;
import nextpos.app.nextpos.model.dto.response.ExpensesResponse;
import nextpos.app.nextpos.model.entity.Category;
import nextpos.app.nextpos.model.entity.Expenses;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.ExpensesRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.ExpensesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl implements ExpensesService {

        private final ExpensesRepository expensesRepository;
        private final WarehouseRepository warehouseRepository;
        private final CategoryRepository categoryRepository;
        private final UserRepository userRepository;

        @Override
        @Transactional
        public ExpensesResponse createExpenses(CreateExpensesRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Warehouse not found with ID: " + request.getWarehouseId()));

                Category category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Category not found with ID: " + request.getCategoryId()));

                Expenses expenses = Expenses.builder()
                                .warehouse(warehouse)
                                .category(category)
                                .date(request.getDate())
                                .amount(request.getAmount())
                                .details(request.getDetails())
                                .createdBy(user.getId())
                                .createdAt(LocalDateTime.now())
                                .companyId(user.getCompanyId())
                                .build();

                return new ExpensesResponse(expensesRepository.save(expenses));
        }

        @Override
        public ExpensesResponse getExpensesById(Long id) {
                Expenses expenses = expensesRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));
                return new ExpensesResponse(expenses);
        }

        @Override
        @Transactional
        public ExpensesResponse updateExpenses(Long id, UpdateExpensesRequest request) {
                User user = UserContext.getAuthenticatedUser(userRepository);

                Expenses expenses = expensesRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));

                if (request.getWarehouseId() != null) {
                        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Warehouse not found with ID: " + request.getWarehouseId()));
                        expenses.setWarehouse(warehouse);
                }

                if (request.getCategoryId() != null) {
                        Category category = categoryRepository.findById(request.getCategoryId())
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
                expenses.setUpdatedBy(user.getId());
                expenses.setUpdatedAt(LocalDateTime.now());

                return new ExpensesResponse(expensesRepository.save(expenses));
        }

        @Override
        @Transactional
        public void deleteExpenses(Long id, Long deletedByUserId) {
                Expenses expenses = expensesRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Expenses not found with ID: " + id));

                expensesRepository.delete(expenses);
        }
}

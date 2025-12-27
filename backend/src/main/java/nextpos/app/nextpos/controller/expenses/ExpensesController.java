package nextpos.app.nextpos.controller.expenses;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateExpensesRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExpensesRequest;
import nextpos.app.nextpos.model.dto.response.ExpensesResponse;
import nextpos.app.nextpos.service.interf.ExpensesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;

    @PostMapping
    public ResponseEntity<ExpensesResponse> createExpenses(@Valid @RequestBody CreateExpensesRequest request) {
        return new ResponseEntity<>(expensesService.createExpenses(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpensesResponse> getExpenses(@PathVariable Long id) {
        return ResponseEntity.ok(expensesService.getExpensesById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpensesResponse> updateExpenses(@PathVariable Long id,
            @Valid @RequestBody UpdateExpensesRequest request) {
        return ResponseEntity.ok(expensesService.updateExpenses(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable Long id,
            @RequestParam("deletedBy") Long deletedByUserId) {
        expensesService.deleteExpenses(id, deletedByUserId);
        return ResponseEntity.noContent().build();
    }
}

package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateExpensesRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateExpensesRequest;
import nextpos.app.nextpos.model.dto.response.ExpensesResponse;

public interface ExpensesService {
    ExpensesResponse createExpenses(CreateExpensesRequest request);

    ExpensesResponse getExpensesById(Long id);

    ExpensesResponse updateExpenses(Long id, UpdateExpensesRequest request);

    void deleteExpenses(Long id, Long deletedByUserId);
}
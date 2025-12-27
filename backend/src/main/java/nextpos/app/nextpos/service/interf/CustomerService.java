package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateCustomerRequest;
import nextpos.app.nextpos.model.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse getCustomerByEmail(String email);
    CustomerResponse getCustomerByPhone(String phone);
    List<CustomerResponse> findAllByCreatedBy(Long userId);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(Long id, CreateCustomerRequest request);
    void deleteCustomer(Long id);
}
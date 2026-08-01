package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCustomerRequest;
import nextpos.app.nextpos.model.dto.response.CustomerResponse;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setCreatedBy(UserContext.getCurrentUserId());
        customer.setCompanyId(UserContext.getCurrentCompanyId());

        return new CustomerResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .map(CustomerResponse::new)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        return customerRepository.findByEmailAndCompanyId(email, UserContext.getCurrentCompanyId())
                .map(CustomerResponse::new)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phone) {
        return customerRepository.findByPhoneAndCompanyId(phone, UserContext.getCurrentCompanyId())
                .map(CustomerResponse::new)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));
    }

    @Override
    public List<CustomerResponse> findAllByCreatedBy(Long userId) {
        return customerRepository.findAllByCreatedBy(userId).stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAllByCompanyId(UserContext.getCurrentCompanyId()).stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = customerRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setUpdatedBy(UserContext.getCurrentUserId());

        return new CustomerResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdAndCompanyId(id, UserContext.getCurrentCompanyId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }
}

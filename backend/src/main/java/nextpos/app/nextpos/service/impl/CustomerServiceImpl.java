package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCustomerRequest;
import nextpos.app.nextpos.model.dto.response.CustomerResponse;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        User user = UserContext.getAuthenticatedUser(userRepository);

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setCreatedBy(user.getId());

        return new CustomerResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::new)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(CustomerResponse::new)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
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
        return customerRepository.findAll().stream()
                .map(CustomerResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = UserContext.getAuthenticatedUser(userRepository);

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setUpdatedBy(user.getId());

        return new CustomerResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
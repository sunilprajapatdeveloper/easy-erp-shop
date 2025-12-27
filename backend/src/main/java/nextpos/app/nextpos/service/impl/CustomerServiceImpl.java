package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCustomerRequest;
import nextpos.app.nextpos.model.dto.response.CustomerResponse;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.CustomerRepository;
import nextpos.app.nextpos.repository.UserRepository;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setCreatedBy(createdBy.getId());

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

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User updatedBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCountry(request.getCountry());
        customer.setCity(request.getCity());
        customer.setUpdatedBy(updatedBy.getId());

        return new CustomerResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
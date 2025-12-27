package nextpos.app.nextpos.controller.people;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateCustomerRequest;
import nextpos.app.nextpos.model.dto.response.CustomerResponse;
import nextpos.app.nextpos.service.interf.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerResponse> getCustomerByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(customerService.getCustomerByPhone(phone));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomerResponse>> getCustomersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.findAllByCreatedBy(userId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
            @Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
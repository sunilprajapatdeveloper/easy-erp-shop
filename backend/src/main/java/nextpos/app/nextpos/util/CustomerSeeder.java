package nextpos.app.nextpos.util;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.Customer;
import nextpos.app.nextpos.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(3) // Run after RoleSeeder
@RequiredArgsConstructor
public class CustomerSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        Long defaultCompanyId = 1L;
        Long defaultCreatedBy = 1L;
        LocalDateTime now = LocalDateTime.now();

        List<Customer> defaultCustomers = List.of(
                Customer.builder()
                        .name("Regular")
                        .email("regular@default.com")
                        .phone("0000000000")
                        .country("DefaultCountry")
                        .city("DefaultCity")
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .build(),

                Customer.builder()
                        .name("Walk-in Customer")
                        .email("walkin@default.com")
                        .phone("0000000001")
                        .country("DefaultCountry")
                        .city("DefaultCity")
                        .companyId(defaultCompanyId)
                        .createdBy(defaultCreatedBy)
                        .createdAt(now)
                        .build());

        for (Customer customer : defaultCustomers) {
            if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
                customerRepository.save(customer);
                System.out.println("Seeded customer: " + customer.getName());
            }
        }
    }
}


package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryIntegrationTest{
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Test
    void shouldStart() {
        System.out.println("Container running " + postgreSQLContainer.isRunning());
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void debeEncontrarClientePorEmail(){
        Customer customer = new Customer();
        customer.setFirstName("Andrea");
        customer.setLastName("Perez");
        customer.setEmail("andrea@test.com");
        customer.setStatus(CustomerStatus.ACTIVE);

        customerRepository.save(customer);

        Optional<Customer> result = customerRepository.findByEmail("andrea@test.com");

        assertTrue(result.isPresent());
        assertEquals("andrea@test.com", result.get().getEmail());
    }
}

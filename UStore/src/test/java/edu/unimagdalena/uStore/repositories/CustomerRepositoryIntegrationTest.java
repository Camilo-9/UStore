
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
@ActiveProfiles("test")
class CustomerRepositoryIntegrationTest{
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

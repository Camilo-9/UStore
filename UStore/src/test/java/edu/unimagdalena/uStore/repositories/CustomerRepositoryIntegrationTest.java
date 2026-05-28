
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

class CustomerRepositoryIntegrationTest extends AbstractRepositoryIT{
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

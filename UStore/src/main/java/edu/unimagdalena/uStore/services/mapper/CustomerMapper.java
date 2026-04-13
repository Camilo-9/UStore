
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.api.dto.response.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper{
    public CustomerResponse toResponse(Customer customer){
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setStatus(customer.getStatus().name());

        return response;
    }
}

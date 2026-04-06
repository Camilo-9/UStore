
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CreateCustomerRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateCustomerRequest;
import edu.unimagdalena.uStore.api.dto.response.CustomerResponse;
import java.util.List;

public interface CustomerService{
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse findById(Long id);
    List<CustomerResponse> findAll();
    CustomerResponse update(Long id, UpdateCustomerRequest request);
}

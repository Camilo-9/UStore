
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.repositories.CustomerRepository;
import edu.unimagdalena.uStore.api.dto.request.CreateCustomerRequest;
import edu.unimagdalena.uStore.api.dto.response.CustomerResponse;
import edu.unimagdalena.uStore.api.dto.request.UpdateCustomerRequest;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.services.mapper.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Customer getOrThrow(Long id){
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Cliente con id: "+ id+ "no encontrado."));
    }

    @Override
    public CustomerResponse create(CreateCustomerRequest request){
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Ya existe un cliente con el email: "+ request.getEmail());
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id){
        return customerMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll(){
        return customerRepository.findAll().stream().map(customerMapper::toResponse).toList();
    }

    @Override
    public CustomerResponse update(Long id, UpdateCustomerRequest request){
        Customer customer = getOrThrow(id);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());

        return customerMapper.toResponse(customerRepository.save(customer));
    }
}


package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.services.CustomerService;
import edu.unimagdalena.uStore.api.dto.request.CreateCustomerRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateCustomerRequest;
import edu.unimagdalena.uStore.api.dto.response.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController{
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
                                                   @RequestBody
                                                   @Valid
                                                   CreateCustomerRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(
                                                    @PathVariable
                                                    Long id){

        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(){
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
                                                   @PathVariable
                                                   Long id,

                                                   @RequestBody
                                                   @Valid
                                                   UpdateCustomerRequest request){

        return ResponseEntity.ok(customerService.update(id, request));
    }
}

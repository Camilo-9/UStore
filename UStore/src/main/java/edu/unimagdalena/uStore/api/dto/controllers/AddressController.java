
package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.services.AddressService;
import edu.unimagdalena.uStore.api.dto.request.CreateAddressRequest;
import edu.unimagdalena.uStore.api.dto.response.AddressResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
public class AddressController{
    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@PathVariable
                                                  Long customerId,

                                                  @RequestBody
                                                  @Valid
                                                  CreateAddressRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(customerId, request));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getByCustomer(@PathVariable
                                                               Long customerId){

        return ResponseEntity.ok(addressService.findByCustomerId(customerId));
    }
}


package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CreateAddressRequest;
import edu.unimagdalena.uStore.api.dto.response.AddressResponse;
import java.util.List;

public interface AddressService{
    AddressResponse create(Long customerId, CreateAddressRequest request);
    List<AddressResponse> findByCustomerId(Long customerId);
}

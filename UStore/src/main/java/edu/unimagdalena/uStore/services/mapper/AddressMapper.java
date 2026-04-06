
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.entities.Address;
import edu.unimagdalena.uStore.api.dto.response.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper{
    public AddressResponse toResponse(Address address){
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());

        return response;
    }
}


package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.entities.Address;
import edu.unimagdalena.uStore.repositories.AddressRepository;
import edu.unimagdalena.uStore.api.dto.request.CreateAddressRequest;
import edu.unimagdalena.uStore.api.dto.response.AddressResponse;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.services.mapper.AddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService{
    private final AddressRepository addressRepository;
    private final CustomerServiceImpl customerService;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, CustomerServiceImpl customerService,
                              AddressMapper addressMapper){
        this.addressRepository = addressRepository;
        this.customerService = customerService;
        this.addressMapper = addressMapper;
    }

    public Address getOrThrow(Long addressId, Long customerId){
        if(!addressRepository.existsByIdAndCustomerId(addressId, customerId)){
            throw new ResourceNotFoundException("Dirección con id: "+ addressId+ " no encontrada para el cliente: "+ customerId);
        }

        return addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(
               "Dirección con id: "+ addressId+ "no encontrada."));
    }

    @Override
    public AddressResponse create(Long customerId, CreateAddressRequest request){
        Customer customer = customerService.getOrThrow(customerId);

        if(customer.getStatus() != CustomerStatus.ACTIVE){
            throw new BusinessException("No se puede agregar una dirección a un cliente inactivo.");
        }

        Address address = new Address();
        address.setCustomer(customer);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry() != null ? request.getCountry():"Colombia");

        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> findByCustomerId(Long customerId){
        customerService.getOrThrow(customerId);

        return addressRepository.findByCustomerId(customerId).stream().map(addressMapper::toResponse).toList();
    }
}

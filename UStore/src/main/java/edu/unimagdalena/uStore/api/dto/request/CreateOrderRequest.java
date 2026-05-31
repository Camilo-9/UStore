
package edu.unimagdalena.uStore.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderRequest{
    @NotNull
    private Long customerId;

    @NotNull
    private Long addressId;

    @NotEmpty(message = "El pedido debe contener al menos un ítem")
    @Valid
    private List<CreateOrderItemRequest> items;

    public Long getCustomerId(){
        return customerId;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public Long getAddressId(){
        return addressId;
    }

    public void setAddressId(Long addressId){
        this.addressId = addressId;
    }

    public List<CreateOrderItemRequest> getItems(){
        return items;
    }

    public void setItems(List<CreateOrderItemRequest> items){
        this.items = items;
    }
}

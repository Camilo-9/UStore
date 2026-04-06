
package edu.unimagdalena.uStore.api.dto.request;

import java.util.List;

public class CreateOrderRequest{
    private Long customerId;
    private Long addressId;
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

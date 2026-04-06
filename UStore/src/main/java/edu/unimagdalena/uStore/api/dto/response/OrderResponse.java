
package edu.unimagdalena.uStore.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse{
    private Long id;
    private Long customerId;
    private String customerName;
    private Long addressId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getCustomerId(){
        return customerId;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public Long getAddressId(){
        return addressId;
    }

    public void setAddressId(Long addressId){
        this.addressId = addressId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public BigDecimal getTotal(){
        return total;
    }

    public void setTotal(BigDecimal total){
        this.total = total;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems(){
        return items;
    }

    public void setItems(List<OrderItemResponse> items){
        this.items = items;
    }
}


package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.entities.Order;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.api.dto.response.OrderItemResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper{
    public OrderResponse toResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getFirstName()+ " "+ order.getCustomer().getLastName());
        response.setAddressId(order.getAddress().getId());
        response.setStatus(order.getStatus().name());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());
            itemResponse.setSubtotal(item.getSubtotal());

            return itemResponse;}).toList());

        return response;
    }
}

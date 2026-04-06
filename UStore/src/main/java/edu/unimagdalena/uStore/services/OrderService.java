
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.api.dto.request.CancelOrderRequest;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService{
    OrderResponse create(CreateOrderRequest request);
    OrderResponse findById(Long id);
    List<OrderResponse> findByFilters(Long customerId, OrderStatus status, LocalDateTime from, LocalDateTime to,
                                      BigDecimal minTotal, BigDecimal maxTotal);
    OrderResponse pay(Long id);
    OrderResponse ship(Long id);
    OrderResponse deliver(Long id);
    OrderResponse cancel(Long id, CancelOrderRequest request);
}

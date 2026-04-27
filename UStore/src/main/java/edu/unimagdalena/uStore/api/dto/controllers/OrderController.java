
package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.api.dto.request.CancelOrderRequest;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController{
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody
                                                @Valid
                                                CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable("id")
                                                 Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable("id")
                                             Long id){
        return ResponseEntity.ok(orderService.pay(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> ship(@PathVariable("id")
                                              Long id){
        return ResponseEntity.ok(orderService.ship(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(@PathVariable("id")
                                                 Long id){
        return ResponseEntity.ok(orderService.deliver(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable("id") Long id,
                                                @RequestBody
                                                @Valid
                                                CancelOrderRequest request){
        return ResponseEntity.ok(orderService.cancel(id, request));
    }
}

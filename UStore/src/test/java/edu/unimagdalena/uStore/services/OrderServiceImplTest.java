
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.*;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.repositories.OrderStatusHistoryRepository;
import edu.unimagdalena.uStore.enums.*;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderItemRequest;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.api.dto.request.CancelOrderRequest;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.api.dto.response.OrderItemResponse;
import edu.unimagdalena.uStore.services.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest{
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private AddressServiceImpl addressService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderStatusHistoryRepository historyRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void noDebeCrearPedidoSinItems(){
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);
        request.setAddressId(1L);
        request.setItems(List.of());

        Customer customer = new Customer();
        customer.setStatus(CustomerStatus.ACTIVE);

        when(customerService.getOrThrow(1L)).thenReturn(customer);
        when(addressService.getOrThrow(any(), any())).thenReturn(new Address());

        assertThrows(BusinessException.class, () -> orderService.create(request));
    }

    @Test
    void noDebePermitirCantidadCero(){
        CreateOrderItemRequest item = new CreateOrderItemRequest();
        item.setQuantity(0);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(item));

        Customer customer = new Customer();
        customer.setStatus(CustomerStatus.ACTIVE);

        when(customerService.getOrThrow(any())).thenReturn(customer);
        when(addressService.getOrThrow(any(), any())).thenReturn(new Address());

        assertThrows(BusinessException.class, () -> orderService.create(request));
    }

    @Test
    void debeCalcularSubtotalYTotalCorrectamente(){
        Customer customer = new Customer();
        customer.setStatus(CustomerStatus.ACTIVE);

        when(customerService.getOrThrow(any())).thenReturn(customer);
        when(addressService.getOrThrow(any(), any())).thenReturn(new Address());

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setActive(true);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(50));
        product2.setActive(true);

        when(productService.getOrThrow(1L)).thenReturn(product1);
        when(productService.getOrThrow(2L)).thenReturn(product2);

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        when(orderMapper.toResponse(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);

            OrderResponse response = new OrderResponse();
            response.setTotal(o.getTotal());

            List<OrderItemResponse> items = o.getItems().stream().map(i -> {
                OrderItemResponse orderItemResponse = new OrderItemResponse();
                orderItemResponse.setSubtotal(i.getUnitPrice().multiply(BigDecimal.valueOf(
                                              i.getQuantity())));

                return orderItemResponse;
            }).toList();

            response.setItems(items);

            return response;
        });

        CreateOrderItemRequest item1 = new CreateOrderItemRequest();
        item1.setProductId(1L);
        item1.setQuantity(2);

        CreateOrderItemRequest item2 = new CreateOrderItemRequest();
        item2.setProductId(2L);
        item2.setQuantity(3);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);
        request.setAddressId(1L);
        request.setItems(List.of(item1, item2));

        OrderResponse response = orderService.create(request);

        assertEquals(0, response.getItems().get(0).getSubtotal().compareTo(BigDecimal
                                                                                    .valueOf(200)));

        assertEquals(0, response.getItems().get(1).getSubtotal().compareTo(BigDecimal
                                                                                    .valueOf(150)));

        assertEquals(0, response.getTotal().compareTo(BigDecimal.valueOf(350)));
    }

    @Test
    void debeRechazarPagoPorStockInsuficiente(){
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Product product = new Product();
        product.setId(1L);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(5);

        order.setItems(List.of(item));

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        assertThrows(BusinessException.class, () -> orderService.pay(1L));
    }

    @Test
    void debeDescontarStockAlPagar(){
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Product product = new Product();
        product.setId(1L);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        order.setItems(List.of(item));

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toResponse(any())).thenReturn(new OrderResponse());

        orderService.pay(1L);

        assertEquals(7, inventory.getAvailableStock());
    }

    @Test
    void debeRevertirStockAlCancelarPedidoPagado(){
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);

        Product product = new Product();
        product.setId(1L);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        order.setItems(List.of(item));

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(5);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toResponse(any())).thenReturn(new OrderResponse());

        orderService.cancel(1L, new CancelOrderRequest());

        assertEquals(8, inventory.getAvailableStock());
    }

    @Test
    void noDebePermitirPagarPedidoNoCreado(){
        Order order = new Order();
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.pay(1L));
    }
}


package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Order;
import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.OrderItem;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.repositories.CustomerRepository;
import edu.unimagdalena.uStore.repositories.ProductRepository;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.enums.OrderStatus;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderItemRequest;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.api.dto.request.CancelOrderRequest;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.api.dto.response.OrderItemResponse;
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
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void noDebeCrearPedidoSinItems(){
        CreateOrderRequest request = new CreateOrderRequest();

        request.setItems(List.of());

        assertThrows(BusinessException.class, () -> {orderService.create(request);});
    }

    @Test
    void noDebePermitirCantidadCero(){
        CreateOrderItemRequest item = new CreateOrderItemRequest();

        item.setQuantity(0);

        CreateOrderRequest request = new CreateOrderRequest();

        request.setItems(List.of(item));

        assertThrows(BusinessException.class, () -> {orderService.create(request);});
    }

    @Test
    void debeCalcularSubtotalYTotalCorrectamente(){
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(50));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        CreateOrderItemRequest item1 = new CreateOrderItemRequest();
        item1.setProductId(1L);
        item1.setQuantity(2);

        CreateOrderItemRequest item2 = new CreateOrderItemRequest();
        item2.setProductId(2L);
        item2.setQuantity(3);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(item1, item2));

        OrderResponse response = orderService.create(request);

        List<OrderItemResponse> items = response.getItems();

        // Subtotales
        assertEquals(BigDecimal.valueOf(200), items.get(0).getSubtotal());
        assertEquals(BigDecimal.valueOf(150), items.get(1).getSubtotal());

        // Total
        assertEquals(BigDecimal.valueOf(350), response.getTotal());
    }

    @Test
    void debeRechazarPagoPorStockInsuficiente(){
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(1);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(5);

        order.setItems(List.of(item));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        assertThrows(BusinessException.class, () -> {orderService.pay(1L);});
    }

    @Test
    void debeDescontarStockAlPagar(){
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        order.setItems(List.of(item));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

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

        // Stock después de haber pagado (8 - 3)
        Inventory inventory = new Inventory();
        inventory.setAvailableStock(5);

        order.setItems(List.of(item));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        orderService.cancel(1L, new CancelOrderRequest());

        assertEquals(8, inventory.getAvailableStock());
    }

    @Test
    void noDebePermitirPagarPedidoNoCreado(){
        Order order = new Order();
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> {orderService.pay(1L);});
    }
}

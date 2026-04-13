
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CancelOrderRequest;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderItemRequest;
import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.entities.*;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import edu.unimagdalena.uStore.enums.OrderStatus;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.repositories.OrderStatusHistoryRepository;
import edu.unimagdalena.uStore.services.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final CustomerServiceImpl customerService;
    private final AddressServiceImpl addressService;
    private final ProductServiceImpl productService;
    private final InventoryRepository inventoryRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerServiceImpl customerService,
                            AddressServiceImpl addressService, ProductServiceImpl productService,
                            InventoryRepository inventoryRepository,
                            OrderStatusHistoryRepository historyRepository, OrderMapper orderMapper){
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.addressService = addressService;
        this.productService = productService;
        this.inventoryRepository = inventoryRepository;
        this.historyRepository = historyRepository;
        this.orderMapper = orderMapper;
    }

    private void recordHistory(Order order, OrderStatus status, String notes){
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(status);
        history.setNotes(notes);
        historyRepository.save(history);
    }

    public Order getOrThrow(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
               "Pedido con id: "+ id+ " no encontrado"));
    }

    @Override
    public OrderResponse create(CreateOrderRequest request){
        Customer customer = customerService.getOrThrow(request.getCustomerId());

        if(customer.getStatus() != CustomerStatus.ACTIVE){
            throw new BusinessException("El cliente no está activo.");
        }

        Address address = addressService.getOrThrow(request.getAddressId(), request.getCustomerId());

        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new BusinessException("El pedido debe contener al menos un ítem.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);

        BigDecimal total = BigDecimal.ZERO;

        for(CreateOrderItemRequest itemRequest:request.getItems()){
            if(itemRequest.getQuantity() <= 0){
                throw new BusinessException("La cantidad debe ser mayor a cero.");
            }

            Product product = productService.getOrThrow(itemRequest.getProductId());

            if(!product.getActive()){
                throw new BusinessException("El producto "+ product.getName()+ "no está activo.");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getPrice());

            order.getItems().add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotal(total);
        Order saved = orderRepository.save(order);
        recordHistory(saved, OrderStatus.CREATED, "Pedido creado.");

        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id){
        return orderMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByFilters(Long customerId, OrderStatus status, LocalDateTime from,
                                             LocalDateTime to, BigDecimal minTotal, BigDecimal maxTotal){

        return orderRepository.findByFilters(customerId, status, from, to, minTotal, maxTotal).stream()
               .map(orderMapper::toResponse).toList();
    }

    @Override
    public OrderResponse pay(Long id){
        Order order = getOrThrow(id);

        if(order.getStatus() != OrderStatus.CREATED){
            throw new BusinessException("Solo un pedido en estado CREATED puede pagarse.");
        }

        for(OrderItem item:order.getItems()){
            Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                                  .orElseThrow(() -> new ResourceNotFoundException(
                                  "Inventario no encontrado para el producto: "+ item.getProduct().getName()));

            if(inventory.getAvailableStock() < item.getQuantity()){
                throw new BusinessException("Stock insuficiente para el producto: "+
                                            item.getProduct().getName());
            }
        }

        for(OrderItem item:order.getItems()){
            Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId()).get();
            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            inventoryRepository.save(inventory);
        }

        order.setStatus(OrderStatus.PAID);
        Order saved = orderRepository.save(order);
        recordHistory(saved, OrderStatus.PAID, "Pedido pagado.");

        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse ship(Long id){
        Order order = getOrThrow(id);

        if(order.getStatus() != OrderStatus.PAID){
            throw new BusinessException("Solo un pedido en estado PAID puede despacharse.");
        }

        order.setStatus(OrderStatus.SHIPPED);
        Order saved = orderRepository.save(order);
        recordHistory(saved, OrderStatus.SHIPPED, "Pedido despachado.");

        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse deliver(Long id){
        Order order = getOrThrow(id);

        if(order.getStatus() != OrderStatus.SHIPPED){
            throw new BusinessException("Solo un pedido en estado SHIPPED puede marcarse como entregado.");
        }

        order.setStatus(OrderStatus.DELIVERED);
        Order saved = orderRepository.save(order);
        recordHistory(saved, OrderStatus.DELIVERED, "Pedido entregado.");

        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse cancel(Long id, CancelOrderRequest request){
        Order order = getOrThrow(id);

        if(order.getStatus() == OrderStatus.DELIVERED){
            throw new BusinessException("Un pedido entregado no puede cancelarse.");
        }

        if(order.getStatus() == OrderStatus.SHIPPED){
            throw new BusinessException("Un pedido despachado no puede cancelarse.");
        }

        if(order.getStatus() == OrderStatus.CANCELLED){
            throw new BusinessException("El pedido ya está cancelado.");
        }

        if(order.getStatus() == OrderStatus.PAID){
            for(OrderItem item:order.getItems()){
                Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getId())
                                      .orElseThrow(() -> new ResourceNotFoundException(
                                      "Inventario no encontrado para el producto: "+
                                      item.getProduct().getName()));
                inventory.setAvailableStock(inventory.getAvailableStock() + item.getQuantity());
                inventoryRepository.save(inventory);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        recordHistory(saved, OrderStatus.CANCELLED,
                      request.getNotes() != null ? request.getNotes():"Pedido cancelado.");

        return orderMapper.toResponse(saved);
    }
}

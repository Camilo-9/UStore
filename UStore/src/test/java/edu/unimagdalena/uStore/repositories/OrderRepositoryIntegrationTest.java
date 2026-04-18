
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.*;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import edu.unimagdalena.uStore.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Transactional
@Rollback
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryIntegrationTest{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void debeBuscarPedidosPorCliente(){
        Customer customer = new Customer();
        customer.setFirstName("Camilo");
        customer.setLastName("Alvarez");
        customer.setEmail("camilo@test.com");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer = customerRepository.save(customer);

        Address address = new Address();
        address.setStreet("12");
        address.setCity("Santa Marta");
        address.setCustomer(customer);

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(500));
        order.setCustomer(customer);
        order.setAddress(address);

        orderRepository.save(order);

        List<Order> result = orderRepository.findByCustomerId(customer.getId());

        assertFalse(result.isEmpty());
    }

    @Test
    void debeBuscarPedidosConFiltros(){
        Customer customer = new Customer();
        customer.setFirstName("Camilo");
        customer.setLastName("Pineda");
        customer.setEmail("Camilop@test.com");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer = customerRepository.save(customer);

        Address address = new Address();
        address.setStreet("19");
        address.setCity("Medellin");
        address.setCustomer(customer);

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setCustomer(customer);
        order.setAddress(address);

        orderRepository.save(order);

        List<Order> result = orderRepository.findByFilters(
                customer.getId(),
                OrderStatus.CREATED,
           null,
             null,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(150)
        );

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void debeObtenerProductosMasVendidos(){
        LocalDateTime from = LocalDateTime.now().minusDays(30);
        LocalDateTime to = LocalDateTime.now();

        Category category = new Category();
        category.setName("Juegos");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setSku("SKU-1");
        product.setName("Dominó");
        product.setPrice(BigDecimal.valueOf(50));
        product.setCategory(category);
        product = productRepository.save(product);

        Customer customer = new Customer();
        customer.setFirstName("Pedro");
        customer.setLastName("Rodriguez");
        customer.setEmail("pedro@test.com");
        customer = customerRepository.save(customer);

        Address address = new Address();
        address.setStreet("Calle 1");
        address.setCity("Santa Marta");
        address.setCustomer(customer);
        address = addressRepository.save(address);

        Order order = new Order();
        order.setStatus(OrderStatus.DELIVERED);
        order.setCreatedAt(LocalDateTime.now().minusDays(5));
        order.setCustomer(customer);
        order.setAddress(address);

        order = orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(4);
        item.setUnitPrice(BigDecimal.valueOf(50));

        orderItemRepository.save(item);

        List<Object[]> result = orderRepository.findBestSellingProducts(
                OrderStatus.DELIVERED, from, to
        );

        assertFalse(result.isEmpty());

        Object[] row = result.get(0);

        assertEquals("Dominó", row[0]);
        assertEquals(4L, row[1]);
    }
    
    @Test
    void debeObtenerIngresosMensuales(){
        List<Object[]> result = orderRepository.findMonthlyIncome();

        assertNotNull(result);
    }

    @Test
    void debeObtenerTopClientes(){
        List<Object[]> result = orderRepository.findTopCustomers();

        assertNotNull(result);
    }
}

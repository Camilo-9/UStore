
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.*;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import edu.unimagdalena.uStore.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryIntegrationTest{
    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Test
    void containerShouldStart() {
        System.out.println("Container running " + postgres.isRunning());
    }

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
        address = addressRepository.save(address);

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
        address = addressRepository.save(address);

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setCustomer(customer);
        order.setAddress(address);

        orderRepository.save(order);

        List<Order> result = orderRepository.findByFilters(
                customer.getId(),
                OrderStatus.CREATED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
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
        category.setName("Bebidas");
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setSku("56ds");
        product1.setName("Gaseosa");
        product1.setPrice(BigDecimal.valueOf(110));
        product1.setCategory(category);
        product1 = productRepository.save(product1);

        Product product2 = new Product();
        product2.setSku("465f");
        product2.setName("Té frío");
        product2.setPrice(BigDecimal.valueOf(105));
        product2.setCategory(category);
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setSku("hg45");
        product3.setName("Botella de agua");
        product3.setPrice(BigDecimal.valueOf(100));
        product3.setCategory(category);
        product3 = productRepository.save(product3);

        Product product4 = new Product();
        product4.setSku("564a");
        product4.setName("Bebida energizante");
        product4.setPrice(BigDecimal.valueOf(120));
        product4.setCategory(category);
        product4 = productRepository.save(product4);

        Customer customer1 = new Customer();
        customer1.setFirstName("Juan");
        customer1.setLastName("Perez");
        customer1.setEmail("juan@test.com");
        customer1 = customerRepository.save(customer1);

        Address address1 = new Address();
        address1.setStreet("25");
        address1.setCity("Santa Marta");
        address1.setCustomer(customer1);
        address1 = addressRepository.save(address1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Ryan");
        customer2.setLastName("Peña");
        customer2.setEmail("ryanp@test.com");
        customer2 = customerRepository.save(customer2);

        Address address2 = new Address();
        address2.setStreet("2");
        address2.setCity("Santa Marta");
        address2.setCustomer(customer2);
        address2 = addressRepository.save(address2);

        Order order1 = new Order();
        order1.setStatus(OrderStatus.SHIPPED);
        order1.setTotal(BigDecimal.valueOf(740));
        order1.setCreatedAt(LocalDateTime.now().minusDays(15));
        order1.setCustomer(customer2);
        order1.setAddress(address2);
        order1 = orderRepository.save(order1);

        //Order 2 creada para validar también la búsqueda por fecha.
        Order order2 = new Order();
        order2.setStatus(OrderStatus.SHIPPED);
        order2.setTotal(BigDecimal.valueOf(975));
        order2.setCreatedAt(LocalDateTime.now().minusDays(40));
        order2.setCustomer(customer1);
        order2.setAddress(address1);
        order2 = orderRepository.save(order2);

        OrderItem item1 = new OrderItem();
        item1.setQuantity(6);
        item1.setUnitPrice(BigDecimal.valueOf(110));
        item1.setOrder(order2);
        item1.setProduct(product1);

        OrderItem item2 = new OrderItem();
        item2.setQuantity(3);
        item2.setUnitPrice(BigDecimal.valueOf(105));
        item2.setOrder(order2);
        item2.setProduct(product2);

        OrderItem item3 = new OrderItem();
        item3.setQuantity(5);
        item3.setUnitPrice(BigDecimal.valueOf(100));
        item3.setOrder(order1);
        item3.setProduct(product3);

        OrderItem item4 = new OrderItem();
        item4.setQuantity(2);
        item4.setUnitPrice(BigDecimal.valueOf(120));
        item4.setOrder(order1);
        item4.setProduct(product4);

        orderItemRepository.save(item1);
        orderItemRepository.save(item2);
        orderItemRepository.save(item3);
        orderItemRepository.save(item4);

        List<Object[]> result = orderRepository.findBestSellingProducts(from, to);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        Object[] primero = result.get(0);

        assertEquals(product3.getId(), primero[0]);
        assertEquals("Botella de agua", primero[1]);
        assertEquals(5L, primero[2]);

        assertEquals(2, result.size());
    }

    @Test
    void debeObtenerIngresosMensuales(){
        Customer customer = new Customer();
        customer.setFirstName("José");
        customer.setLastName("Perea");
        customer.setEmail("jose@gmail.com");
        customer = customerRepository.save(customer);

        Address address = new Address();
        address.setStreet("29e");
        address.setCity("Santa Marta");
        address.setCustomer(customer);
        address = addressRepository.save(address);

        Order order1 = new Order();
        order1.setStatus(OrderStatus.PAID);
        order1.setTotal(BigDecimal.valueOf(100));
        order1.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 0));
        order1.setCustomer(customer);
        order1.setAddress(address);

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setStatus(OrderStatus.PAID);
        order2.setTotal(BigDecimal.valueOf(200));
        order2.setCreatedAt(LocalDateTime.of(2026, 1, 16, 9, 55));
        order2.setCustomer(customer);
        order2.setAddress(address);

        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setStatus(OrderStatus.PAID);
        order3.setTotal(BigDecimal.valueOf(450));
        order3.setCreatedAt(LocalDateTime.of(2026, 2, 10, 9, 31));
        order3.setCustomer(customer);
        order3.setAddress(address);

        orderRepository.save(order3);

        List<Object[]> result = orderRepository.findMonthlyIncome();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        Object[] enero = result.get(0);
        BigDecimal ingresosEnero = (BigDecimal) enero[2];

        assertEquals(2026, enero[0]);
        assertEquals(1, enero[1]);
        assertEquals(0, ingresosEnero.compareTo(BigDecimal.valueOf(300)));

        Object[] febrero = result.get(1);
        BigDecimal ingresosFebrero = (BigDecimal) febrero[2];

        assertEquals(2026, febrero[0]);
        assertEquals(2, febrero[1]);
        assertEquals(0, ingresosFebrero.compareTo(BigDecimal.valueOf(450)));

        assertEquals(2, result.size());
    }

    @Test
    void debeObtenerTopClientes(){
        Customer customer1 = new Customer();
        customer1.setFirstName("Camilo");
        customer1.setLastName("Perez");
        customer1.setEmail("camilo@gmail.com");
        customer1 = customerRepository.save(customer1);

        Address address1 = new Address();
        address1.setStreet("42");
        address1.setCity("Santa Marta");
        address1.setCustomer(customer1);
        address1 = addressRepository.save(address1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Ana");
        customer2.setLastName("Gomez");
        customer2.setEmail("ana@gmail.com");
        customer2 = customerRepository.save(customer2);

        Address address2 = new Address();
        address2.setStreet("28");
        address2.setCity("Santa Marta");
        address2.setCustomer(customer2);
        address2 = addressRepository.save(address2);

        Order order1 = new Order();
        order1.setStatus(OrderStatus.DELIVERED);
        order1.setTotal(BigDecimal.valueOf(300));
        order1.setCustomer(customer1);
        order1.setAddress(address1);

        Order order2 = new Order();
        order2.setStatus(OrderStatus.DELIVERED);
        order2.setTotal(BigDecimal.valueOf(200));
        order2.setCustomer(customer1);
        order2.setAddress(address1);

        orderRepository.save(order1);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setStatus(OrderStatus.DELIVERED);
        order3.setTotal(BigDecimal.valueOf(50));
        order3.setCustomer(customer2);
        order3.setAddress(address2);

        orderRepository.save(order3);

        //Order4 creado para validar que un pedido cancelado no se debe tener en cuenta
        Order order4 = new Order();
        order4.setStatus(OrderStatus.CANCELLED);
        order4.setTotal(BigDecimal.valueOf(1000));
        order4.setCustomer(customer2);
        order4.setAddress(address2);

        orderRepository.save(order4);

        List<Object[]> result = orderRepository.findTopCustomers();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        Object[] mejorCliente = result.get(0);
        BigDecimal mejorClienteTotalGastado = (BigDecimal) mejorCliente[3];

        assertEquals(customer1.getId(), mejorCliente[0]);
        assertEquals("Camilo", mejorCliente[1]);
        assertEquals("Perez", mejorCliente[2]);
        assertEquals(0, mejorClienteTotalGastado.compareTo(BigDecimal.valueOf(500)));

        Object[] segundoMejorCliente = result.get(1);
        BigDecimal segundoMejorClienteTotalGastado = (BigDecimal) segundoMejorCliente[3];

        assertEquals(customer2.getId(), segundoMejorCliente[0]);
        assertEquals("Ana", segundoMejorCliente[1]);
        assertEquals("Gomez", segundoMejorCliente[2]);
        assertEquals(0, segundoMejorClienteTotalGastado.compareTo(BigDecimal.valueOf(50)));

        assertEquals(2, result.size());
    }
}

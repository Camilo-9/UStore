
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.*;
import edu.unimagdalena.uStore.enums.CustomerStatus;
import edu.unimagdalena.uStore.enums.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class OrderRepositoryIntegrationTest{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void debeBuscarPedidosPorCliente(){
        Customer customer = new Customer();
        customer.setFirstName("Camilo");
        customer.setLastName("Alvarez");
        customer.setEmail("camilo@test.com");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer = customerRepository.save(customer);

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(500));
        order.setCustomer(customer);

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

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.valueOf(100));
        order.setCustomer(customer);

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

        List<Object[]> result = orderRepository.findBestSellingProducts(from, to);

        assertNotNull(result);
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

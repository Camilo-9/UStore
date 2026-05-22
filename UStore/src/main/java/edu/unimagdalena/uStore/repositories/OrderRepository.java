
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Order;
import edu.unimagdalena.uStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomerId(Long customerId);

    @Query("""
           SELECT o
           FROM Order o
           WHERE (:customerId IS NULL OR o.customer.id = :customerId)
           AND (:status IS NULL OR o.status = :status)
           AND (CAST(:from AS timestamp) IS NULL OR o.createdAt >= :from)
           AND (CAST(:to AS timestamp) IS NULL OR o.createdAt <= :to)
           AND (:minTotal IS NULL OR o.total >= :minTotal)
           AND (:maxTotal IS NULL OR o.total <= :maxTotal)
           """)
    List<Order> findByFilters(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("minTotal") BigDecimal minTotal,
            @Param("maxTotal") BigDecimal maxTotal
    );

    @Query("""
           SELECT oi.product.id, oi.product.name, SUM(oi.quantity)
           FROM OrderItem oi
           WHERE oi.order.createdAt BETWEEN :from AND :to
             AND oi.order.status != 'CANCELLED'
             AND oi.order.status != 'CREATED'
           GROUP BY oi.product.id, oi.product.name
           ORDER BY SUM(oi.quantity) DESC
           """)
    List<Object[]> findBestSellingProducts(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
           SELECT YEAR(o.createdAt), MONTH(o.createdAt), SUM(o.total)
           FROM Order o
           WHERE o.status != 'CANCELLED'
             AND o.status != 'CREATED'
           GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)
           ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)
           """)
    List<Object[]> findMonthlyIncome();

    @Query("""
           SELECT o.customer.id, o.customer.firstName, o.customer.lastName, SUM(o.total) AS totalSpent
           FROM Order o
           WHERE o.status = 'DELIVERED'
           GROUP BY o.customer.id, o.customer.firstName, o.customer.lastName
           ORDER BY totalSpent DESC
           """)
    List<Object[]> findTopCustomers();
}

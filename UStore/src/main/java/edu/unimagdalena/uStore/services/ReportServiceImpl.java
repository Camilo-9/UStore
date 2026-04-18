
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.enums.OrderStatus;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.api.dto.response.BestSellingProductResponse;
import edu.unimagdalena.uStore.api.dto.response.LowStockProductResponse;
import edu.unimagdalena.uStore.api.dto.response.MonthlyIncomeResponse;
import edu.unimagdalena.uStore.api.dto.response.TopCustomerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService{
    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    public ReportServiceImpl(OrderRepository orderRepository, InventoryRepository inventoryRepository){
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<BestSellingProductResponse> findBestSellingProducts(OrderStatus orderStatus,
                                                                    LocalDateTime from, LocalDateTime to){
        return orderRepository.findBestSellingProducts(OrderStatus.DELIVERED, from, to).stream().map(row -> {
                    BestSellingProductResponse response = new BestSellingProductResponse();
                    response.setProductName((String) row[1]);
                    response.setTotalSold((Long) row[2]);

                    return response;}).toList();
    }

    @Override
    public List<MonthlyIncomeResponse> findMonthlyIncome(){
        return orderRepository.findMonthlyIncome().stream().map(row -> {
                    MonthlyIncomeResponse response = new MonthlyIncomeResponse();
                    response.setYear((Integer) row[0]);
                    response.setMonth((Integer) row[1]);
                    response.setTotalIncome((BigDecimal) row[2]);

                    return response;}).toList();
    }

    @Override
    public List<TopCustomerResponse> findTopCustomers(){
        return orderRepository.findTopCustomers().stream().map(row -> {
                    TopCustomerResponse response = new TopCustomerResponse();
                    response.setCustomerId((Long) row[0]);
                    response.setFirstName((String) row[1]);
                    response.setLastName((String) row[2]);
                    response.setTotalSpent((BigDecimal) row[3]);

                    return response;}).toList();
    }

    @Override
    public List<LowStockProductResponse> findLowStockProducts(){
        return inventoryRepository.findLowStockInventories().stream().map(inventory -> {
                    Product product = inventory.getProduct();
                    LowStockProductResponse response = new LowStockProductResponse();
                    response.setProductId(product.getId());
                    response.setSku(product.getSku());
                    response.setProductName(product.getName());
                    response.setAvailableStock(inventory.getAvailableStock());
                    response.setMinimumStock(inventory.getMinimumStock());

                    return response;}).toList();
    }
}

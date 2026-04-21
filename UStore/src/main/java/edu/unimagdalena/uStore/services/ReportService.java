
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.response.BestSellingProductResponse;
import edu.unimagdalena.uStore.api.dto.response.LowStockProductResponse;
import edu.unimagdalena.uStore.api.dto.response.MonthlyIncomeResponse;
import edu.unimagdalena.uStore.api.dto.response.TopCustomerResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService{
    List<BestSellingProductResponse> findBestSellingProducts(LocalDateTime from, LocalDateTime to);
    List<MonthlyIncomeResponse> findMonthlyIncome();
    List<TopCustomerResponse> findTopCustomers();
    List<LowStockProductResponse> findLowStockProducts();
}

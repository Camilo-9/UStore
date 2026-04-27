
package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.services.ReportService;
import edu.unimagdalena.uStore.api.dto.response.BestSellingProductResponse;
import edu.unimagdalena.uStore.api.dto.response.LowStockProductResponse;
import edu.unimagdalena.uStore.api.dto.response.MonthlyIncomeResponse;
import edu.unimagdalena.uStore.api.dto.response.TopCustomerResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController{
    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<BestSellingProductResponse>> bestSellingProducts(
                                                            @RequestParam("startDate")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime startDate,

                                                            @RequestParam("endDate")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime endDate){

        return ResponseEntity.ok(reportService.findBestSellingProducts(startDate, endDate));
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<List<MonthlyIncomeResponse>> monthlyIncome(){
        return ResponseEntity.ok(reportService.findMonthlyIncome());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerResponse>> topCustomers(){
        return ResponseEntity.ok(reportService.findTopCustomers());
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<List<LowStockProductResponse>> lowStockProducts(){
        return ResponseEntity.ok(reportService.findLowStockProducts());
    }
}

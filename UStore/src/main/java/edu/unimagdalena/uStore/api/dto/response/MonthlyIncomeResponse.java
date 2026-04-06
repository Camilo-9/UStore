
package edu.unimagdalena.uStore.api.dto.response;

import java.math.BigDecimal;

public class MonthlyIncomeResponse{
    private Integer year;
    private Integer month;
    private BigDecimal totalIncome;

    public Integer getYear(){
        return year;
    }

    public void setYear(Integer year){
        this.year = year;
    }

    public Integer getMonth(){
        return month;
    }

    public void setMonth(Integer month){
        this.month = month;
    }

    public BigDecimal getTotalIncome(){
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome){
        this.totalIncome = totalIncome;
    }
}

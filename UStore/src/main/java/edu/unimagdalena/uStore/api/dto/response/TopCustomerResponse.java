
package edu.unimagdalena.uStore.api.dto.response;

import java.math.BigDecimal;

public class TopCustomerResponse{
    private Long customerId;
    private String firstName;
    private String lastName;
    private BigDecimal totalSpent;

    public Long getCustomerId(){
        return customerId;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public BigDecimal getTotalSpent(){
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent){
        this.totalSpent = totalSpent;
    }
}

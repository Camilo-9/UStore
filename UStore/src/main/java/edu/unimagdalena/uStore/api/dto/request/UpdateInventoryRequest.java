
package edu.unimagdalena.uStore.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateInventoryRequest{
    @NotNull
    @Min(0)
    private Integer availableStock;

    @NotNull
    @Min(0)
    private Integer minimumStock;

    public Integer getAvailableStock(){
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock){
        this.availableStock = availableStock;
    }

    public Integer getMinimumStock(){
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock){
        this.minimumStock = minimumStock;
    }
}


package edu.unimagdalena.uStore.api.dto.request;

public class UpdateInventoryRequest{
    private Integer availableStock;
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

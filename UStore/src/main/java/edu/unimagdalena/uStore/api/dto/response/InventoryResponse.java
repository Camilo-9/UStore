
package edu.unimagdalena.uStore.api.dto.response;

public class InventoryResponse{
    private Long id;
    private Integer availableStock;
    private Integer minimumStock;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

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

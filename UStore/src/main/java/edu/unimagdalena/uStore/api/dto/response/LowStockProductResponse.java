
package edu.unimagdalena.uStore.api.dto.response;

public class LowStockProductResponse{
    private Long productId;
    private String sku;
    private String productName;
    private Integer availableStock;
    private Integer minimumStock;

    public Long getProductId(){
        return productId;
    }

    public void setProductId(Long productId){
        this.productId = productId;
    }

    public String getSku(){
        return sku;
    }

    public void setSku(String sku){
        this.sku = sku;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
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

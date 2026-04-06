
package edu.unimagdalena.uStore.api.dto.response;

public class BestSellingProductResponse{
    private Long productId;
    private String productName;
    private Long totalSold;

    public Long getProductId(){
        return productId;
    }

    public void setProductId(Long productId){
        this.productId = productId;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public Long getTotalSold(){
        return totalSold;
    }

    public void setTotalSold(Long totalSold){
        this.totalSold = totalSold;
    }
}

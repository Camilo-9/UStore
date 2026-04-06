
package edu.unimagdalena.uStore.api.dto.request;

import java.math.BigDecimal;

public class UpdateProductRequest{
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public Long getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(Long categoryId){
        this.categoryId = categoryId;
    }
}


package edu.unimagdalena.uStore.api.dto.response;

import java.math.BigDecimal;

public class ProductResponse{
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
    private CategoryResponse category;
    private InventoryResponse inventory;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getSku(){
        return sku;
    }

    public void setSku(String sku){
        this.sku = sku;
    }

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

    public Boolean getActive(){
        return active;
    }

    public void setActive(Boolean active){
        this.active = active;
    }

    public CategoryResponse getCategory(){
        return category;
    }

    public void setCategory(CategoryResponse category){
        this.category = category;
    }

    public InventoryResponse getInventory(){
        return inventory;
    }

    public void setInventory(InventoryResponse inventory){
        this.inventory = inventory;
    }
}

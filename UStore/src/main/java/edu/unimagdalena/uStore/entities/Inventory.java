
package edu.unimagdalena.uStore.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
public class Inventory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;

    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @PrePersist
    private void prePersist(){
        this.updatedAt = LocalDateTime.now();

        if(this.availableStock == null){
            this.availableStock = 0;
        }

        if(this.minimumStock == null){
            this.minimumStock = 0;
        }
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

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

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product = product;
    }
}

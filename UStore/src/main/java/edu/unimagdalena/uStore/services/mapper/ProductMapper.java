
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.api.dto.response.InventoryResponse;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper{
    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper){
        this.categoryMapper = categoryMapper;
    }

    public ProductResponse toResponse(Product product, Inventory inventory){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setActive(product.getActive());
        response.setCategory(categoryMapper.toResponse(product.getCategory()));

        if(inventory != null){
            InventoryResponse inventoryResponse = new InventoryResponse();
            inventoryResponse.setId(inventory.getId());
            inventoryResponse.setAvailableStock(inventory.getAvailableStock());
            inventoryResponse.setMinimumStock(inventory.getMinimumStock());
            response.setInventory(inventoryResponse);
        }

        return response;
    }
}


package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.response.InventoryResponse;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;
    private final ProductServiceImpl productService;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, @Lazy ProductServiceImpl productService){
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
    }

    private InventoryResponse toResponse(Inventory inventory){
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setAvailableStock(inventory.getAvailableStock());
        response.setMinimumStock(inventory.getMinimumStock());

        return response;
    }

    public Inventory getOrThrow(Long productId){
        return inventoryRepository.findByProductId(productId).orElseThrow(() -> new ResourceNotFoundException(
               "Inventario no encontrado para el producto con id: "+ productId));
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse findByProductId(Long productId){
        productService.getOrThrow(productId);
        Inventory inventory = getOrThrow(productId);

        return toResponse(inventory);
    }

    @Override
    public InventoryResponse update(Long productId, UpdateInventoryRequest request){
        if(request.getAvailableStock() < 0){
            throw new BusinessException("El stock disponible no puede ser negativo");
        }

        if(request.getMinimumStock() < 0){
            throw new BusinessException("El stock mínimo no puede ser negativo");
        }

        Product product = productService.getOrThrow(productId);

        Inventory inventory = inventoryRepository.findByProductId(productId).orElseGet(() -> {
                              Inventory newInventory = new Inventory();
                              newInventory.setProduct(product);

                              return newInventory;
                });

        inventory.setAvailableStock(request.getAvailableStock());
        inventory.setMinimumStock(request.getMinimumStock());

        return toResponse(inventoryRepository.save(inventory));
    }
}

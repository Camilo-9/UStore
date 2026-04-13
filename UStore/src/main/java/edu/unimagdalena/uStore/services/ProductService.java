
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CreateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import java.util.List;

public interface ProductService{
    ProductResponse create(CreateProductRequest request);
    ProductResponse findById(Long id);
    List<ProductResponse> findAll();
    ProductResponse update(Long id, UpdateProductRequest request);
    ProductResponse updateInventory(Long id, UpdateInventoryRequest request);
}

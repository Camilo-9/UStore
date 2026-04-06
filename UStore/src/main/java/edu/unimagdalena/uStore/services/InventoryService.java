
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.response.InventoryResponse;

public interface InventoryService{
    InventoryResponse findByProductId(Long productId);
    InventoryResponse update(Long productId, UpdateInventoryRequest request);
}

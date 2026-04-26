
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest{
    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void noDebePermitirStockNegativo(){
        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(-1);
        request.setMinimumStock(5);

        assertThrows(BusinessException.class, () -> inventoryService.update(1L, request));
    }

    @Test
    void noDebePermitirStockMinimoNegativo(){
        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(10);
        request.setMinimumStock(-5);

        assertThrows(BusinessException.class, () -> inventoryService.update(1L, request));
    }

    @Test
    void noDebeActualizarInventarioSiProductoNoExiste(){
        when(productService.getOrThrow(1L))
        .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(10);
        request.setMinimumStock(2);

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.update(1L, request));
    }

    @Test
    void debeCrearInventarioSiNoExiste(){
        Product product = new Product();
        product.setId(1L);

        when(productService.getOrThrow(1L)).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation ->
                                                                        invocation.getArgument(0));

        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(10);
        request.setMinimumStock(2);

        inventoryService.update(1L, request);

        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void debeActualizarInventarioCorrectamente(){
        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(5);
        inventory.setMinimumStock(1);

        when(productService.getOrThrow(1L)).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation ->
                                                                        invocation.getArgument(0));

        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(20);
        request.setMinimumStock(10);

        inventoryService.update(1L, request);

        assertEquals(20, inventory.getAvailableStock());
        assertEquals(10, inventory.getMinimumStock());
    }

    @Test
    void debeGuardarCambiosEnInventario(){
        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();

        when(productService.getOrThrow(1L)).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation ->
                                                             invocation.getArgument(0));

        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(10);
        request.setMinimumStock(2);

        inventoryService.update(1L, request);

        verify(inventoryRepository).save(inventory);
    }
}

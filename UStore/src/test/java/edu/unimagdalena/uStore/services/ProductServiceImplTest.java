
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.repositories.ProductRepository;
import edu.unimagdalena.uStore.repositories.CategoryRepository;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.api.dto.request.CreateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import edu.unimagdalena.uStore.api.dto.response.InventoryResponse;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.services.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest{
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryServiceImpl inventoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void noDebeCrearProductoSinCategoriaValida(){
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setPrice(BigDecimal.valueOf(100));
        createProductRequest.setCategoryId(1L);
        createProductRequest.setSku("fd415");

        when(productRepository.existsBySku("fd415")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.create(createProductRequest));
    }

    @Test
    void noDebePermitirSkuDuplicado(){
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setSku("Abc123");
        createProductRequest.setPrice(BigDecimal.valueOf(100));
        createProductRequest.setCategoryId(1L);

        when(productRepository.existsBySku("Abc123")).thenReturn(true);

        assertThrows(ConflictException.class, () -> productService.create(createProductRequest));
    }

    @Test
    void debeCrearProductoCorrectamente(){
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setSku("fd45d");
        createProductRequest.setPrice(BigDecimal.valueOf(100));
        createProductRequest.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        when(productRepository.existsBySku("fd45d")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> inv.getArgument(0));

        when(productMapper.toResponse(any(Product.class), any(Inventory.class))).thenAnswer(inv -> {
             Product product = inv.getArgument(0);
             ProductResponse productResponse = new ProductResponse();
             productResponse.setSku(product.getSku());
             productResponse.setPrice(product.getPrice());

             return productResponse;
        });

        ProductResponse response = productService.create(createProductRequest);

        assertEquals("fd45d", response.getSku());
        assertEquals(0, response.getPrice().compareTo(BigDecimal.valueOf(100)));
    }

    @Test
    void debeActualizarInventarioCorrectamente(){
        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);
        inventory.setMinimumStock(2);

        UpdateInventoryRequest updateInventoryRequest = new UpdateInventoryRequest();
        updateInventoryRequest.setAvailableStock(20);
        updateInventoryRequest.setMinimumStock(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryService.update(eq(1L), any(UpdateInventoryRequest.class))).thenReturn(
             new InventoryResponse());
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(productMapper.toResponse(any(Product.class), any(Inventory.class))).thenReturn(
             new ProductResponse());

        productService.updateInventory(1L, updateInventoryRequest);

        verify(inventoryService).update(1L, updateInventoryRequest);
    }

    @Test
    void debeGuardarProducto(){
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setSku("hg564m");
        createProductRequest.setPrice(BigDecimal.valueOf(100));
        createProductRequest.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productMapper.toResponse(any(Product.class), any(Inventory.class))).thenReturn(
             new ProductResponse());

        productService.create(createProductRequest);

        verify(productRepository).save(any(Product.class));
        verify(inventoryRepository).save(any(Inventory.class));
    }
}


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
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
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

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void noDebeCrearProductoConPrecioCero(){
        CreateProductRequest request = new CreateProductRequest();
        request.setPrice(BigDecimal.ZERO);

        assertThrows(BusinessException.class, () -> {productService.create(request);});
    }

    @Test
    void noDebeCrearProductoSinCategoriaValida(){
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {productService.create(request);});
    }

    @Test
    void noDebePermitirSkuDuplicado(){
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("ABC123");

        when(productRepository.findBySku("ABC123")).thenReturn(Optional.of(new Product()));

        assertThrows(ConflictException.class, () -> {productService.create(request);});
    }

    @Test
    void debeCrearProductoCorrectamente(){
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("ABC123");
        request.setPrice(BigDecimal.valueOf(100));
        request.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        when(productRepository.findBySku("ABC123")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productService.create(request);

        assertEquals("ABC123", response.getSku());
    }

    @Test
    void debeActualizarInventarioCorrectamente(){
        Product product = new Product();
        product.setId(1L);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);
        inventory.setMinimumStock(2);

        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setAvailableStock(20);
        request.setMinimumStock(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        productService.updateInventory(1L, request);

        assertEquals(20, inventory.getAvailableStock());
        assertEquals(5, inventory.getMinimumStock());
    }

    @Test
    void debeGuardarProducto(){
        CreateProductRequest request = new CreateProductRequest();
        request.setSku("ABC123");
        request.setPrice(BigDecimal.valueOf(100));
        request.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.create(request);

        verify(productRepository).save(any(Product.class));
    }
}


package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class InventoryRepositoryIntegrationTest extends AbstractRepositoryIT{
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void debeEncontrarProductosConBajoStock(){
        Category category = new Category();
        category.setName("Dulces");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setSku("6f5ds");
        product.setName("Menta");
        product.setPrice(BigDecimal.valueOf(10));
        product.setActive(true);
        product.setCategory(category);
        product = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(2);
        inventory.setMinimumStock(5);
        inventory.setProduct(product);

        inventoryRepository.save(inventory);

        List<Inventory> result = inventoryRepository.findLowStockInventories();

        assertFalse(result.isEmpty());
    }

    @Test
    void noDebeRetornarProductosConStockSuficiente(){
        Category category = new Category();
        category.setName("Balones");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setSku("h5g4j");
        product.setName("Balon de futbol");
        product.setPrice(BigDecimal.valueOf(100));
        product.setActive(true);
        product.setCategory(category);
        product = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);
        inventory.setMinimumStock(5);
        inventory.setProduct(product);

        inventoryRepository.save(inventory);

        List<Inventory> result = inventoryRepository.findLowStockInventories();

        assertTrue(result.isEmpty());
    }

    @Test
    void debeEncontrarInventarioPorProducto(){
        Category category = new Category();
        category.setName("Ropa");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setSku("fg35h");
        product.setName("Zapato deportivo");
        product.setPrice(BigDecimal.valueOf(50));
        product.setActive(true);
        product.setCategory(category);
        product = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);
        inventory.setMinimumStock(5);
        inventory.setProduct(product);

        inventoryRepository.save(inventory);

        Optional<Inventory> result = inventoryRepository.findByProductId(product.getId());

        assertTrue(result.isPresent());
    }
}

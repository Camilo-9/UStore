
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class InventoryRepositoryIntegrationTest{
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void debeEncontrarProductosConBajoStock(){
        Product product = new Product();
        product.setSku("LOW-STOCK");
        product.setName("Menta");
        product.setPrice(BigDecimal.valueOf(10));
        product.setActive(true);
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
        Product product = new Product();
        product.setSku("OK-STOCK");
        product.setName("Balon de futbol");
        product.setPrice(BigDecimal.valueOf(100));
        product.setActive(true);
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
        Product product = new Product();
        product.setSku("INV-1");
        product.setName("Zapato deportivo");
        product.setPrice(BigDecimal.valueOf(50));
        product.setActive(true);
        product = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setAvailableStock(10);
        inventory.setMinimumStock(5);
        inventory.setProduct(product);

        inventoryRepository.save(inventory);

        Optional<Inventory> result = inventoryRepository.findByProductId(product.getId());

        assertTrue(result.isPresent());
    }

    @AfterEach
    void limpiar(){
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
    }
}

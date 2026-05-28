
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.math.BigDecimal;
import java.util.List;

class ProductRepositoryIntegrationTest extends AbstractRepositoryIT{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void debeBuscarProductosPorCategoria(){
        Category category = new Category();
        category.setName("Ropa");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setSku("fg89fd");
        product.setName("Camisa Polo");
        product.setPrice(BigDecimal.valueOf(1500));
        product.setActive(true);
        product.setCategory(category);

        productRepository.save(product);

        List<Product> result = productRepository.findByActiveTrueAndCategoryId(category.getId());

        assertFalse(result.isEmpty());
    }
}

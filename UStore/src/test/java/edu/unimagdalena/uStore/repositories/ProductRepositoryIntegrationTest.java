
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@Transactional
@Rollback
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIntegrationTest{
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

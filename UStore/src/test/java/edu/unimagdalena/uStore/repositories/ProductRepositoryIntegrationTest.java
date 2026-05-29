
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryIntegrationTest {
    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Test
    void containerShouldStart(){
        System.out.println("Container running " + postgres.isRunning());
    }

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

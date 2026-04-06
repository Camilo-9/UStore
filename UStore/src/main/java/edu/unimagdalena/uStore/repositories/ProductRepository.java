
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findBySku(String sku);
    boolean existsBySku(String sku);
    List<Product> findByActiveTrueAndCategoryId(Long categoryId);
}

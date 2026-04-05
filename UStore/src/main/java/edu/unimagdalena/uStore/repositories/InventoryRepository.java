
package edu.unimagdalena.uStore.repositories;

import edu.unimagdalena.uStore.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    Optional<Inventory> findByProductId(Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.availableStock < i.minimumStock")
    List<Inventory> findLowStockInventories();
}

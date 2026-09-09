package com.inventory.repository;

import com.inventory.enitity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    @Query("""
        SELECT inv FROM Inventory inv
        WHERE inv.productId = ?1
""")
    Optional<Inventory> findByProductId(Long productId);
}

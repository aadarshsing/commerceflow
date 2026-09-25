package com.inventory.repository;

import com.inventory.enitity.InventoryOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryOperationsRepository extends JpaRepository<InventoryOperations,Long> {


    Optional<InventoryOperations> findByIdempotencyKey(String idempotencyKey);

    Optional<InventoryOperations> findByOrderId(Long orderId);

    Optional<InventoryOperations> findByProductId(String productId);
}

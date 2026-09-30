package com.catalog.product.repository;

import com.catalog.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {

    boolean existsBySellerIdAndSku(Long sellerId, String sku);

    Optional<Product> findByIdempotencyKey(String idempotencyKey);

}

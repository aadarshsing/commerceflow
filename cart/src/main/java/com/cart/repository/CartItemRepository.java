package com.cart.repository;

import com.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem,Long> {

    @Query("""
            SELECT c FROM CartItem c
            WHERE c.productId =?1 AND c.cart.id =?2
            """)
    Optional<CartItem> findByProductIdAndCart(Long productId, Long cartId);

    @Query("""
            SELECT c FROM CartItem c
            WHERE c.id =?1 AND c.cart.id =?2
            """)
    Optional<CartItem> findByIdAndCart(Long id, Long cartId);
}

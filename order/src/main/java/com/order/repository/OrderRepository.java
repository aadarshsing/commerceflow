package com.order.repository;


import com.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    @Query("""
            SELECT O FROM Order O
            WHERE O.customerId = ?1
            """)
    Page<Order> findAllOrderByCustomerId(long customerId, Pageable pageable);


}

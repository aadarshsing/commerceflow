package com.payment.repository;


import com.payment.entity.Payment;
import com.payment.entity.enums.payment.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query("""
            SELECT pay FROM Payment pay
            WHERE pay.orderId = ?1
            """)
    Optional<List<Payment>> findByOrderId(Long orderId);

    @Query("""
            SELECT pay FROM Payment pay
            WHERE pay.customerId = ?1
            """)
    Page<Payment>findByCustomerId(Long customerId, Pageable pageable);

    @Query("""
            SELECT pay FROM Payment pay
            WHERE pay.orderId = ?1
            AND pay.status = ?2
            """)
    Optional<Payment> findByOrderIdAndPaymentStatus(Long orderId, PaymentStatus status);

    Optional<Payment> findByIdAndStatus(Long id,PaymentStatus status);

}

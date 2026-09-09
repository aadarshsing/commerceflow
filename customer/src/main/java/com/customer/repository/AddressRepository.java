package com.customer.repository;

import com.customer.entity.Address;
import com.customer.entity.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query("""
            SELECT A FROM Address A
            WHERE A.customer.id = ?1 AND A.type = ?2
            """)
    Optional<Address> findByCustomerAndType(Long customerId, AddressType type);
}

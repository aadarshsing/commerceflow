package com.shipment.repository;

import com.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {


    Optional<Shipment> findByCustomerId(Long customerId);

    Optional<Shipment> findByOrderId(Long customerId);
}

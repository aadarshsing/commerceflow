package com.order.service.client;

import com.order.dto.ResponseDto;
import jakarta.validation.Valid;
import org.commerceflow.dto.shipment.CreateShipmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipment")
public interface ShipmentFeignClient {

    @PostMapping("api/shipments")
    public ResponseEntity<ResponseDto> createShipment(@Valid @RequestBody CreateShipmentDto createShipmentDto);
}

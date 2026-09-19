package com.order.service.client;

import com.order.dto.order.ResponseDto;
import com.order.dto.shipment.CreateShipmentDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipment")
public interface ShipmentFeignClient {

    @PostMapping("api/shipments")
    public ResponseEntity<ResponseDto> createShipment(@Valid @RequestBody CreateShipmentDto createShipmentDto);
}

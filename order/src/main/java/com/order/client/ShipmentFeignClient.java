package com.order.client;

import com.order.config.feign.FeignRetryConfig;
import com.order.dto.ResponseDto;
import com.order.client.fallback.ShipmentFallBack;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.commerceflow.dto.shipment.CreateShipmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shipment"
        ,configuration = FeignRetryConfig.class
        ,fallback = ShipmentFallBack.class)
public interface ShipmentFeignClient {

    @PostMapping("api/shipments")
    public ResponseEntity<ResponseDto> createShipment(
            @NotBlank(message = "shipmentIdempotencyKey cannot be null, empty or blank")
            @RequestParam String shipmentIdempotencyKey,
            @Valid @RequestBody CreateShipmentDto createShipmentDto);
}

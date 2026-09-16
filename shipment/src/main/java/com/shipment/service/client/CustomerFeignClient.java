package com.shipment.service.client;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer")
public interface CustomerFeignClient {

    @GetMapping("api/customer/{customerId}")
    public ResponseEntity<Boolean> checkCustomerExist(@PathVariable @NotNull(message = "customerId cannot be null") Long customerId);

}

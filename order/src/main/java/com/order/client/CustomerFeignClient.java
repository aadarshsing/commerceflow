package com.order.client;

import com.order.client.fallback.CustomerFallBack;
import com.order.config.feign.FeignRetryConfig;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.customer.AddressResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer"
        ,configuration = FeignRetryConfig.class
        ,fallback = CustomerFallBack.class)
public interface CustomerFeignClient {

    @GetMapping("api/address/{addressId}")
    ResponseEntity<AddressResponseDto> getAddress(@NotNull(message = "AddressId cannot be null" )
                                                  @PathVariable Long addressId);
    @GetMapping("api/customer/{customerId}")
    public ResponseEntity<Boolean> checkCustomerExist(@PathVariable @NotNull(message = "customerId cannot be null") Long customerId);
}

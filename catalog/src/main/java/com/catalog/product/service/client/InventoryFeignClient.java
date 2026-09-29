package com.catalog.product.service.client;


import com.catalog.product.dto.inventory.CreateInventoryDto;
import com.catalog.product.dto.product.ResponseDto;
import com.catalog.product.service.client.fallback.InventoryFallBack;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "inventory",
        fallback = InventoryFallBack.class)
public interface InventoryFeignClient {

    @PostMapping("api/inventory")
    ResponseEntity<ResponseDto> createInventory(@Valid @RequestBody CreateInventoryDto createInventoryDto);
}

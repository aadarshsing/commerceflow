package com.catalog.product.service.client;


import com.catalog.product.dto.CreateInventoryDto;
import com.catalog.product.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory")
public interface InventoryFeignClient {

    @PostMapping("api/inventory")
    ResponseEntity<ResponseDto> createInventory(@Valid @RequestBody CreateInventoryDto createInventoryDto);
}

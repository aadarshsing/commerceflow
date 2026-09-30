package com.catalog.product.service.client.fallback;

import com.catalog.exception.ServiceUnavailableException;
import com.catalog.product.dto.inventory.CreateInventoryDto;
import com.catalog.product.dto.product.ResponseDto;
import com.catalog.product.service.client.InventoryFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryFallBack implements InventoryFeignClient {

    @Override
    public ResponseEntity<ResponseDto> createInventory(CreateInventoryDto createInventoryDto) {
        throw  new ServiceUnavailableException("Inventory service is currently unavailable");
    }
}

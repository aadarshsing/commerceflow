package com.catalog.product.service.client.fallback;

import com.catalog.exception.ServiceUnavailableException;
import com.catalog.product.dto.inventory.CreateInventoryDto;
import com.catalog.product.dto.product.ResponseDto;
import com.catalog.product.service.IproductService;
import com.catalog.product.service.client.InventoryFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryFallBack implements InventoryFeignClient {

    @Autowired
    IproductService iproductService;

    @Override
    public ResponseEntity<ResponseDto> createInventory(CreateInventoryDto createInventoryDto) {
        throw  new ServiceUnavailableException("Inventory service is currently unavailable");
    }
}

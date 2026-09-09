package com.inventory.controller;



import com.inventory.dto.CreateInventoryDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.dto.ResponseDto;
import com.inventory.dto.UpdateInventoryDto;
import com.inventory.service.IInventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class InventoryController {

    @Autowired
    IInventoryService inventoryService;

    @PostMapping("inventory")
    ResponseEntity<ResponseDto> createInventory(@Valid @RequestBody CreateInventoryDto createInventoryDto){
        inventoryService.createInventory(createInventoryDto);
        return  new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "Inventory is created Successfully"
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/inventories/{productId}")
    ResponseEntity<InventoryResponseDto> getInventory(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId){
        InventoryResponseDto inventoryResponseDto = inventoryService.getInventory(productId);
        return  ResponseEntity.ok(inventoryResponseDto);
    }

    @PutMapping("/inventories/{productId}/stock")
    ResponseEntity<InventoryResponseDto> updateStock(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryDto updateInventoryDto){
        InventoryResponseDto inventoryResponseDto = inventoryService.updateInventory(productId, updateInventoryDto);
        return ResponseEntity.ok(inventoryResponseDto);
    }

    @PutMapping("/inventories/{productId}/lowStockThreshold")
    ResponseEntity<InventoryResponseDto> updateLowStockThreshold(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId,
            @Min(
                    value = 1,
                    message = "Quantity must be greater than 0"
            )
            @RequestParam int lowStockThreshold){
        InventoryResponseDto inventoryResponseDto = inventoryService.updateInventoryLowStockThreshold(productId,lowStockThreshold);
        return  ResponseEntity.ok(inventoryResponseDto);
    }

    @PostMapping("/inventories/bulk")
    ResponseEntity<List<InventoryResponseDto>> createBulkInventories(@Valid @RequestBody List<CreateInventoryDto> createInventoryDtoList){

        List<InventoryResponseDto> inventoryResponseDtos = inventoryService.createBulkInventories(createInventoryDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryResponseDtos);
    }

}

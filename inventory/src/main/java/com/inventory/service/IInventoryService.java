package com.inventory.service;


import com.inventory.dto.CreateInventoryDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.dto.UpdateInventoryDto;

import java.util.List;

public interface IInventoryService {

    /***
     *
     * @param inventoryDto
     */
    void createInventory(CreateInventoryDto inventoryDto);

    /***
     *
     * @param productId
     * @return -- it return the inventory based on product Id
     */
    InventoryResponseDto getInventory(Long productId);

    /***
     *
     *
     * @param productId
     * @param updateInventoryDto
     * @return -- it return the updated inventory response object
     */
    InventoryResponseDto updateInventory(Long productId, UpdateInventoryDto updateInventoryDto);

    /**
     *
     * @param productId
     * @param lowStockThreshold
     * @return ; it returns the updated inventory response object
     */
    InventoryResponseDto updateInventoryLowStockThreshold(Long productId,int lowStockThreshold);

    /**
     *
     * @param createInventoryDtoList
     * @return - it returns list inventories response object
     */
    List<InventoryResponseDto> createBulkInventories(List<CreateInventoryDto> createInventoryDtoList);
}

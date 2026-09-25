package com.inventory.mapper;

import com.inventory.dto.inventory.UpdateInventoryDto;
import com.inventory.enitity.InventoryOperations;

public class InventoryOperationMapper {

    public static InventoryOperations updateInventoryDtoToInventoryOperation(UpdateInventoryDto updateInventoryDto,InventoryOperations inventoryOperations){

        inventoryOperations.setOperationType(updateInventoryDto.operation());
        inventoryOperations.setProductId(updateInventoryDto.OrderId());
        return inventoryOperations;
    }
}

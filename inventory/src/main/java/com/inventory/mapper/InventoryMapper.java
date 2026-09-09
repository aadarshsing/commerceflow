package com.inventory.mapper;


import com.inventory.dto.CreateInventoryDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.enitity.Inventory;
import com.inventory.enitity.enums.Status;

public class InventoryMapper {

    public static Inventory createDtoToInventoryMapper(Inventory inventory, CreateInventoryDto createInventoryDto){

        inventory.setAvailableQuantity(createInventoryDto.availableQuantity());
        if(createInventoryDto.availableQuantity() > createInventoryDto.lowStockThreshold()){
            inventory.setStatus(Status.ACTIVE);
        }
        else if(inventory.getAvailableQuantity() <= createInventoryDto.lowStockThreshold() && inventory.getAvailableQuantity() > 0){
            inventory.setStatus(Status.LOW_STOCK);
        }
        else if(inventory.getAvailableQuantity() == 0){
            inventory.setStatus(Status.SOLDOUT);
        }
        inventory.setLowStockThreshold(createInventoryDto.lowStockThreshold());
        inventory.setProductId(createInventoryDto.productId());
        return inventory;

    }

//    public static  Inventory updateDtoToInventoryMapper(Inventory inventory, UpdateInventoryDto updateInventoryDto){
//        int availableQuantity = inventory.getAvailableQuantity();
//        if(updateInventoryDto.operation().equals(InventoryOperation.ADD)){
//            inventory.setAvailableQuantity(availableQuantity + updateInventoryDto.quantity());
//            if(availableQuantity > inventory.getLowStockThreshold()){
//                inventory.setStatus(Status.ACTIVE);
//            }
//            else if(availableQuantity < inventory.getLowStockThreshold() && availableQuantity > 0){
//                inventory.setStatus(Status.LOW_STOCK);
//            }
//        }
//        else if(updateInventoryDto.operation().equals(InventoryOperation.REMOVE)){
//            if(availableQuantity < updateInventoryDto.quantity()){
//                throw new ResourceNotAvailableException("Inventory","Quantity",String.valueOf(availableQuantity));
//            }
//            availableQuantity -= updateInventoryDto.quantity();
//            inventory.setAvailableQuantity(availableQuantity);
//            if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
//                inventory.setStatus(Status.LOW_STOCK);
//            }
//            else if(availableQuantity == 0){
//                inventory.setStatus(Status.SOLDOUT);
//            }
//        }
//        else if(updateInventoryDto.operation().equals(InventoryOperation.RESERVE)){
//            if(availableQuantity < updateInventoryDto.quantity()){
//                throw new ResourceNotAvailableException("Inventory","Quantity",String.valueOf(availableQuantity));
//            }
//            availableQuantity -= updateInventoryDto.quantity();
//            inventory.setAvailableQuantity(availableQuantity);
//            inventory.setReservedQuantity(updateInventoryDto.quantity() + inventory.getReservedQuantity());
//            if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
//                inventory.setStatus(Status.LOW_STOCK);
//            }
//            else if(availableQuantity == 0){
//                inventory.setStatus(Status.SOLDOUT);
//            }
//        }
//        else if(updateInventoryDto.operation().equals(InventoryOperation.RELEASE)){
//            availableQuantity += updateInventoryDto.quantity();
//            int reserveQuantity = inventory.getReservedQuantity() - updateInventoryDto.quantity();
//            if(reserveQuantity < 0 ){
//                throw new ResourceNotAvailableException("Inventory","ReserveQuantity",String.valueOf(reserveQuantity));
//            }
//            inventory.setAvailableQuantity(availableQuantity);
//            if(availableQuantity > inventory.getLowStockThreshold()){
//                inventory.setStatus(Status.ACTIVE);
//            }
//            else if(availableQuantity < inventory.getLowStockThreshold() && availableQuantity > 0){
//                inventory.setStatus(Status.LOW_STOCK);
//            }
//        }
//        return  inventory;
//    }

    public static InventoryResponseDto inventoryToResponseDtoMapper(Inventory inventory){
        return new InventoryResponseDto(
                inventory.getProductId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getLowStockThreshold(),
                inventory.getStatus()
        );
    }
}

package com.inventory.service.implementation;

import com.inventory.dto.CreateInventoryDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.dto.ProductResponseDto;
import com.inventory.dto.UpdateInventoryDto;
import com.inventory.enitity.Inventory;
import com.inventory.enitity.enums.InventoryOperation;
import com.inventory.enitity.enums.ProductStatus;
import com.inventory.enitity.enums.Status;
import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotActiveException;
import com.inventory.exception.ResourceNotAvailableException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.mapper.InventoryMapper;
import com.inventory.repository.InventoryRepository;
import com.inventory.service.IInventoryService;
import com.inventory.service.client.ProductFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    InventoryRepository inventoryRepository;
    ProductFeignClient productFeignClient;

    @Override
    public void createInventory(CreateInventoryDto inventoryDto) {
        Optional<Inventory> inventory = inventoryRepository.findByProductId(inventoryDto.productId());
        if(inventory.isPresent()){
            throw new DuplicateResourceException("Inventory already exist for given product /n" +
                    "you can add stock");
        }
        ProductResponseDto productResponseDto = productFeignClient.getProductById(inventoryDto.productId()).getBody();
        if(productResponseDto == null){
            throw  new ResourceNotFoundException("Product","ProductId",inventoryDto.productId().toString());
        }
        if(productResponseDto.status().equals(ProductStatus.INACTIVE)){
            throw new ResourceNotActiveException("Product","productId",inventoryDto.productId().toString());
        }
        Inventory inventory1 = InventoryMapper.createDtoToInventoryMapper(new Inventory(),inventoryDto);
        inventoryRepository.save(inventory1);
    }

    @Override
    public InventoryResponseDto getInventory(Long productId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        if(inventory.isEmpty()){
            throw new ResourceNotFoundException("Inventory","productId",productId.toString());
        }
        return InventoryMapper.inventoryToResponseDtoMapper(inventory.get());
    }

    @Transactional
    @Override
    public InventoryResponseDto updateInventory(Long productId, UpdateInventoryDto updateInventoryDto) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory","productId",productId.toString())
        );
        int availableQuantity = inventory.getAvailableQuantity();
        if(updateInventoryDto.operation().equals(InventoryOperation.ADD)){
            availableQuantity = availableQuantity + updateInventoryDto.quantity();
            inventory.setAvailableQuantity(availableQuantity);
            if(availableQuantity > inventory.getLowStockThreshold()){
                inventory.setStatus(Status.ACTIVE);
            }
            else if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
                inventory.setStatus(Status.LOW_STOCK);
            }
        }
        else if(updateInventoryDto.operation().equals(InventoryOperation.REMOVE)){
            if(availableQuantity < updateInventoryDto.quantity()){
                throw new ResourceNotAvailableException("Inventory","Quantity",String.valueOf(availableQuantity));
            }
            availableQuantity -= updateInventoryDto.quantity();
            inventory.setAvailableQuantity(availableQuantity);
            if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
                inventory.setStatus(Status.LOW_STOCK);
            }
            else if(availableQuantity == 0){
                inventory.setStatus(Status.SOLDOUT);
            }
        }
        else if(updateInventoryDto.operation().equals(InventoryOperation.RESERVE)){
            if(availableQuantity < updateInventoryDto.quantity()){
                throw new ResourceNotAvailableException("Inventory","Quantity",String.valueOf(availableQuantity));
            }
            availableQuantity -= updateInventoryDto.quantity();
            inventory.setAvailableQuantity(availableQuantity);
            inventory.setReservedQuantity(updateInventoryDto.quantity() + inventory.getReservedQuantity());
            if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
                inventory.setStatus(Status.LOW_STOCK);
            }
            else if(availableQuantity == 0){
                inventory.setStatus(Status.SOLDOUT);
            }
        }
        else if(updateInventoryDto.operation().equals(InventoryOperation.RELEASE)){
            availableQuantity += updateInventoryDto.quantity();
            if(updateInventoryDto.quantity() > inventory.getReservedQuantity() ){
                throw new ResourceNotAvailableException("Inventory","ReserveQuantity",String.valueOf(inventory.getReservedQuantity()));
            }
            int reserveQuantity = inventory.getReservedQuantity() - updateInventoryDto.quantity();
            inventory.setAvailableQuantity(availableQuantity);
            inventory.setReservedQuantity(reserveQuantity);
            if(availableQuantity > inventory.getLowStockThreshold()){
                inventory.setStatus(Status.ACTIVE);
            }
            else if(availableQuantity <= inventory.getLowStockThreshold() && availableQuantity > 0){
                inventory.setStatus(Status.LOW_STOCK);
            }
        }

       inventory =  inventoryRepository.save(inventory);
        return InventoryMapper.inventoryToResponseDtoMapper(inventory);



    }

    @Override
    public InventoryResponseDto updateInventoryLowStockThreshold(Long productId, int lowStockThreshold) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory","productId",productId.toString())
        );
        inventory.setLowStockThreshold(lowStockThreshold);
        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.inventoryToResponseDtoMapper(inventory);

    }

    @Transactional
    @Override
    public List<InventoryResponseDto> createBulkInventories(List<CreateInventoryDto> createInventoryDtoList) {

        List<Inventory> inventories = createInventoryDtoList.stream().
                map(request -> {
                    Optional<Inventory> inventory = inventoryRepository.findByProductId(request.productId());
                    if(inventory.isPresent()){
                        throw new DuplicateResourceException("Inventory already exist for given product /n" +
                                "you can add stock");
                    }
                    Inventory inventorySave = InventoryMapper.createDtoToInventoryMapper(new Inventory(),request);
                    return inventorySave;

                }).toList();

        return inventoryRepository.saveAll(inventories)
                .stream()
                .map(InventoryMapper::inventoryToResponseDtoMapper)
                .toList();
    }


}

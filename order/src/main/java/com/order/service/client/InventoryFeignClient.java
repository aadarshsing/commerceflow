package com.order.service.client;



import com.order.dto.InventoryResponseDto;
import com.order.dto.ResponseDto;
import com.order.dto.UpdateInventoryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory")
public interface InventoryFeignClient {

    @PutMapping("api/inventories/{productId}/stock")
    ResponseEntity<InventoryResponseDto> updateStock(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryDto updateInventoryDto);
}

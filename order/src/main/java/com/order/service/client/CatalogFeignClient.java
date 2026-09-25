package com.order.service.client;

import com.order.service.client.fallback.CatalogFallBack;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.catalog.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog",fallback = CatalogFallBack.class)
public interface CatalogFeignClient {

    @GetMapping("api/product")
    ResponseEntity<ProductResponseDto> getProductById(@NotNull(message = "id cannot be null")
                                                      @RequestParam Long id);
}

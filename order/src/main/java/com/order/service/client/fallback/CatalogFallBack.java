package com.order.service.client.fallback;

import com.order.exception.ServiceUnavailableException;
import com.order.service.client.CatalogFeignClient;
import org.commerceflow.dto.catalog.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CatalogFallBack implements CatalogFeignClient {
    @Override
    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
        throw new ServiceUnavailableException(
                "Catalog Service is currently unavailable"
        );
    }
}

package com.catalog.product.controller;

import com.catalog.product.dto.product.CreateProductRequestDto;
import com.catalog.product.dto.product.ProductResponseDto;
import com.catalog.product.dto.product.ResponseDto;
import com.catalog.product.dto.product.UpdateProductRequestDto;
import com.catalog.product.entity.enums.ProductStatus;
import com.catalog.product.service.IproductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class ProductController {

    @Autowired
    IproductService iproductService;

    @PostMapping("/product")
    ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductRequestDto productRequestDto,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader("commerceflow-correlation-id") String correlationId){

        ProductResponseDto productResponseDto =iproductService.createProduct(productRequestDto,idempotencyKey,correlationId);
        return ResponseEntity.ok(
                productResponseDto
        );

    }
    @PostMapping("/product/bulk")
    ResponseEntity<ResponseDto> createBulkProduct(
            @Valid @RequestBody List<CreateProductRequestDto> createProductRequestDtoList,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader("commerceflow-correlation-id") String correlationId){
        iproductService.createProductInBulk(createProductRequestDtoList,idempotencyKey,correlationId);
        return ResponseEntity.ok(
                new ResponseDto(
                        HttpStatus.OK.toString(),
                        "Products are created Successfully"
                )
        );
    }

    @PutMapping("/product/{id}")
    ResponseEntity<ProductResponseDto> updateProduct(
            @Valid @RequestBody UpdateProductRequestDto productRequestDto,
            @NotNull(message = "id cannot be null")
            @PathVariable  Long id,
            @RequestHeader("commerceflow-correlation-id") String correlationId){

        ProductResponseDto productResponseDto = iproductService.updateProduct(productRequestDto,id, correlationId);
        return  new ResponseEntity<>(
                productResponseDto,
                HttpStatus.OK
        );
    }

    @GetMapping("/product")
    ResponseEntity<ProductResponseDto> getProductById(@NotNull(message = "id cannot be null")
                                                         @RequestParam Long id,@RequestHeader("commerceflow-correlation-id") String correlationId){
        ProductResponseDto productResponseDto = iproductService.getProductById(id,correlationId);
        return new ResponseEntity<>(
                productResponseDto,
                HttpStatus.OK
        );
    }

    @GetMapping("/products")
    ResponseEntity<Slice<ProductResponseDto>> listProduct(
            @RequestHeader("commerceflow-correlation-id") String correlationId,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int limit){

        Slice<ProductResponseDto> productResponseDtoPage = iproductService.listProduduct(correlationId,
                status,
                sellerId,
                categoryId,
                name,
                minPrice,
                maxPrice,
                cursor, limit);

        return  ResponseEntity.ok(productResponseDtoPage);
    }
}

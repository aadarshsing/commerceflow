package com.catalog.product.controller;

import com.catalog.product.dto.CreateProductRequestDto;
import com.catalog.product.dto.ProductResponseDto;
import com.catalog.product.dto.ResponseDto;
import com.catalog.product.dto.UpdateProductRequestDto;
import com.catalog.product.entity.enums.ProductStatus;
import com.catalog.product.service.IproductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/api")
@Validated
public class ProductController {

    @Autowired
    IproductService iproductService;

    @PostMapping("/product")
    ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody CreateProductRequestDto productRequestDto){

        iproductService.createProduct(productRequestDto);
        return ResponseEntity.ok(
                new ResponseDto(
                        HttpStatus.OK.toString(),
                        "Product is created Successfully"
                )
        );

    }

    @PutMapping("/product/{id}")
    ResponseEntity<ProductResponseDto> updateProduct(@Valid @RequestBody UpdateProductRequestDto productRequestDto,
                                                     @NotNull(message = "id cannot be null")
                                                     @PathVariable  Long id){

        ProductResponseDto productResponseDto = iproductService.updateProduct(productRequestDto,id);
        return  new ResponseEntity<>(
                productResponseDto,
                HttpStatus.OK
        );
    }

    @GetMapping("/product")
    ResponseEntity<ProductResponseDto> getProductById(@NotNull(message = "id cannot be null")
                                                         @RequestParam Long id){
        ProductResponseDto productResponseDto = iproductService.getProductById(id);
        return new ResponseEntity<>(
                productResponseDto,
                HttpStatus.OK
        );
    }

    @GetMapping("/products")
    ResponseEntity<Page<ProductResponseDto>> listProduct(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable){

        Page<ProductResponseDto> productResponseDtoPage = iproductService.listProduduct(
                status,
                sellerId,
                categoryId,
                name,
                minPrice,
                maxPrice,
                pageable);

        return  ResponseEntity.ok(productResponseDtoPage);
    }
}

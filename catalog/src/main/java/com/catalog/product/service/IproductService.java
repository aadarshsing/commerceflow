package com.catalog.product.service;

import com.catalog.product.dto.product.CreateProductRequestDto;
import com.catalog.product.dto.product.ProductResponseDto;
import com.catalog.product.dto.product.UpdateProductRequestDto;
import com.catalog.product.entity.enums.ProductStatus;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;
import java.util.List;

public interface IproductService {

    /**
     *
     * @param productRequestDto
     * @param idempotencyKey
     * @param correlationId
     * @return
     */
    ProductResponseDto createProduct(CreateProductRequestDto productRequestDto,String idempotencyKey,
                                     String correlationId);

    /**
     *
     * @param createProductRequestDtoList
     * @param idempotencyKey
     * @param correlationId
     */
    void createProductInBulk(List<CreateProductRequestDto> createProductRequestDtoList,String idempotencyKey,
                             String correlationId);
    /**
     *
     * @param productRequestDto
     * @param correlationId
     * @return -- it returns the updated product dto
     */
    ProductResponseDto updateProduct(UpdateProductRequestDto productRequestDto, Long id,
                                     String correlationId);

    /**
     *
     * @param id
     * @param correlationId
     * @return -- it returns the product Information based on given id
     */
    ProductResponseDto getProductById(Long id,String correlationId);

    /**
     *
     * @param correlationId
     * @param cursor
     * @param limit
     * @return -- it returns all the product based on the pagination and sorting which will provide through api from client side
     */
    Slice<ProductResponseDto> listProduduct(
            String correlationId,
            ProductStatus status,
            Long sellerId,Long categoryId,
            String name,
            BigDecimal minPrice,BigDecimal maxPrice,
            Long cursor,
            int limit);

    /**
     *
     * @param id
     * @param correlationId
     * @return : it returns true or false whether the product is deleted or not
     */
    boolean deleteProduct(Long id,String correlationId);
}


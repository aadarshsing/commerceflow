package com.catalog.product.service;

import com.catalog.product.dto.CreateProductRequestDto;
import com.catalog.product.dto.ProductResponseDto;
import com.catalog.product.dto.UpdateProductRequestDto;
import com.catalog.product.entity.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;
import java.util.List;

public interface IproductService {

    /**
     *
     * @param productRequestDto
     */
    void createProduct(CreateProductRequestDto productRequestDto);

    /**
     *
     * @param createProductRequestDtoList
     */
    void createProductInBulk(List<CreateProductRequestDto> createProductRequestDtoList);
    /**
     *
     * @param productRequestDto
     * @return -- it returns the updated product dto
     */
    ProductResponseDto updateProduct(UpdateProductRequestDto productRequestDto, Long id);

    /**
     *
     * @param id
     * @return -- it returns the product Information based on given id
     */
    ProductResponseDto getProductById(Long id);

    /**
     *
     * @param cursor
     * @param limit
     * @return -- it returns all the product based on the pagination and sorting which will provide through api from client side
     */
    Slice<ProductResponseDto> listProduduct(
            ProductStatus status,
            Long sellerId,Long categoryId,
            String name,
            BigDecimal minPrice,BigDecimal maxPrice,
            Long cursor,
            int limit);

    /**
     *
     * @param id
     * @return : it returns true or false whether the product is deleted or not
     */
    boolean deleteProduct(Long id);
}


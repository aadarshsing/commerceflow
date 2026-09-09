package com.catalog.product.mapper;


import com.catalog.product.dto.CreateProductRequestDto;
import com.catalog.product.dto.ProductResponseDto;
import com.catalog.product.dto.UpdateProductRequestDto;
import com.catalog.product.entity.Product;
import com.catalog.product.entity.enums.ProductStatus;

public class ProductMapper {

    public static Product productCreateDtoToEntity(CreateProductRequestDto productRequestDto, Product product){

        product.setName(productRequestDto.name());
        product.setPrice(productRequestDto.price());
        product.setSku(productRequestDto.sku());
        product.setDescription(productRequestDto.description());
        product.setStatus(ProductStatus.ACTIVE);

        return product;
    }

    public static Product productUpdateDtoToEntity(UpdateProductRequestDto productRequestDto, Product product) {

        product.setName(productRequestDto.name());
        product.setDescription(productRequestDto.description());
        product.setPrice(productRequestDto.price());
        product.setStatus(productRequestDto.status());

        return product;

    }

    public static ProductResponseDto productEntityTOResponseDto(Product product){

        return new ProductResponseDto(
                product.getId(),
                product.getSeller().getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getStatus()
        );
    }

}

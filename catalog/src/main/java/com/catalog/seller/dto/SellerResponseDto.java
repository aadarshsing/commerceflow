package com.catalog.seller.dto;

import com.catalog.seller.entity.enums.SellerStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SellerResponseDto(
        Long id,
        String  businessName,
        String email,
        String phone,
        SellerStatus status
) {
}

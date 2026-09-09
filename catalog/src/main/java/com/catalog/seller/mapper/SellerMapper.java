package com.catalog.seller.mapper;


import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.SellerResponseDto;
import com.catalog.seller.entity.Seller;
import com.catalog.seller.entity.enums.SellerStatus;

public class SellerMapper {


    public static Seller dtoToSellerMapper(CreateSellerRequestDto sellerRequestDto , Seller seller){

        seller.setBusinessName(sellerRequestDto.businessName());
        seller.setEmail(sellerRequestDto.email());
        seller.setPhone(sellerRequestDto.phone());
        seller.setStatus(SellerStatus.ACTIVE);

        return  seller;
    }

    public  static SellerResponseDto sellertoDtoMapper(Seller seller){
        return new SellerResponseDto(
                seller.getId(),
                seller.getBusinessName(),
                seller.getEmail(),
                seller.getPhone(),
                seller.getStatus()
        );

    }
}

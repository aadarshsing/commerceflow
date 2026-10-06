package com.catalog.seller.service;


import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.SellerResponseDto;

public interface IsellerService {
    /**
     *
     * @param sellerRequestDto
     * @param idempotencyKey
     * @param correlationId
     * @return
     *
     */
    SellerResponseDto createSeller(CreateSellerRequestDto sellerRequestDto,String idempotencyKey, String correlationId);

    /**
     *
     * @param email
     * @param correlationId
     * @return --it returns SellerInformation
     */
    SellerResponseDto fetchSeller(String email,String correlationId);

    /**
     *
     * @param sellerRequestDto
     * @param email
     * @param correlationId
     * @return -- it will update the seller information and return the same
     */
    SellerResponseDto updateSeller(CreateSellerRequestDto sellerRequestDto, String email,String correlationId);

    /**
     *
     * @param email
     * @param correlationId
     * @return -- it returns the boolean whether the seller is deleted or not
     */
    boolean deleteSeller(String email,String correlationId);



}

package com.catalog.seller.service;


import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.SellerResponseDto;

public interface IsellerService {
    /**
     *
     * @param sellerRequestDto
     */
    void createSeller(CreateSellerRequestDto sellerRequestDto);

    /**
     *
     * @param email
     * @return --it returns SellerInformation
     */
    SellerResponseDto fetchSeller(String email);

    /**
     *
     * @param sellerRequestDto
     * @param email
     * @return -- it will update the seller information and return the same
     */
    SellerResponseDto updateSeller(CreateSellerRequestDto sellerRequestDto, String email);

    /**
     *
     * @param email
     * @return -- it returns the boolean whether the seller is deleted or not
     */
    boolean deleteSeller(String email);



}

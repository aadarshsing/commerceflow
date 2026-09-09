package com.customer.service;


import com.customer.dto.CreateAddressRequestDto;

public interface IAddressService {

    /**
     *
     * @param addressRequestDto
     */
    void createAddress(CreateAddressRequestDto addressRequestDto);
}

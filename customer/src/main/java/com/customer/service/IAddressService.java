package com.customer.service;


import com.customer.dto.AddressResponseDto;
import com.customer.dto.CreateAddressRequestDto;

public interface IAddressService {

    /**
     *
     * @param addressRequestDto
     */
    void createAddress(CreateAddressRequestDto addressRequestDto);

    /**
     *
     * @param id
     * @return = address
     */
    AddressResponseDto getAddress(Long id);
}

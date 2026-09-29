package com.customer.service;


import com.customer.dto.address.AddressResponseDto;
import com.customer.dto.address.CreateAddressRequestDto;

public interface IAddressService {

    /**
     *
     * @param addressRequestDto
     * @param correlationId
     */
    void createAddress(CreateAddressRequestDto addressRequestDto,String correlationId);

    /**
     *
     * @param id
     * @param correlationId
     * @return = address
     */
    AddressResponseDto getAddress(Long id,String correlationId);
}

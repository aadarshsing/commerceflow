package com.customer.mapper;


import com.customer.dto.AddressResponseDto;
import com.customer.dto.CreateAddressRequestDto;
import com.customer.entity.Address;

public class AddressMapper {


    public  static AddressResponseDto addressEntityToDtoMapper(Address address){

        return new AddressResponseDto(
                address.getType(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getPhoneNumber(),
                address.getIsDefault()
        );
    }
    public static Address addressRequestDtoToAddressMapper(CreateAddressRequestDto createAddressRequestDto, Address address){
        address.setAddressLine1(createAddressRequestDto.addressLine1());
        address.setAddressLine2(createAddressRequestDto.addressLine2());
        address.setCity(createAddressRequestDto.city());
        address.setCountry(createAddressRequestDto.country());
        address.setState(createAddressRequestDto.state());
        address.setPhoneNumber(createAddressRequestDto.phoneNumber());
        address.setPostalCode(createAddressRequestDto.postalCode());
        address.setType(createAddressRequestDto.type());
        address.setIsDefault(createAddressRequestDto.isDefault());

        return  address;
    }
}

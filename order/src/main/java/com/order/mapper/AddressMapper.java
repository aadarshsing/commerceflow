package com.order.mapper;

import com.order.dto.order.OrderAddressResponseDto;
import com.order.entity.OrderAddress;

public class AddressMapper {

    public static OrderAddressResponseDto orderAddressToAddressResponseDtoMapper(OrderAddress orderAddress){

        return new OrderAddressResponseDto(
                orderAddress.getAddressLine1(),
                orderAddress.getAddressLine2(),
                orderAddress.getCity(),
                orderAddress.getState(),
                orderAddress.getCountry(),
                orderAddress.getPostalCode(),
                orderAddress.getPhoneNumber()
        );
    }

}

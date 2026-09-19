package com.customer.mapper;


import com.customer.dto.customer.CreateCustomerRequest;
import com.customer.dto.customer.CustomerResponseDto;
import com.customer.dto.customer.UpdateCustomerDto;
import com.customer.entity.Customer;
import com.customer.entity.enums.CustomerStatus;

public class CustomerMapper {


    public static CustomerResponseDto customerToDtoMapper(Customer customer){
        CustomerResponseDto createCustomerResponseDto = new CustomerResponseDto(
                customer.getName(), customer.getEmail(), customer.getPhoneNumber(), customer.getCustomerStatus(),
                customer.getAddresses().stream().map(AddressMapper::addressEntityToDtoMapper).toList());
        return createCustomerResponseDto;
    }

    public static  Customer dtoToCreateCustomerMapper(Customer customer, CreateCustomerRequest createCustomerRequest){
        customer.setName(createCustomerRequest.name());
        customer.setEmail(createCustomerRequest.email());
        customer.setPassword(createCustomerRequest.password());
        customer.setCustomerStatus(CustomerStatus.INITIATED);
        customer.setPhoneNumber(createCustomerRequest.phoneNumber());

        return  customer;
    }
    public static  Customer dtoToUpdateCustomerMapper(Customer customer, UpdateCustomerDto updateCustomerRequest){
        customer.setName(updateCustomerRequest.name());
        customer.setEmail(updateCustomerRequest.email());
        customer.setPassword(updateCustomerRequest.password());
        customer.setPhoneNumber(updateCustomerRequest.phoneNumber());

        return  customer;
    }

}

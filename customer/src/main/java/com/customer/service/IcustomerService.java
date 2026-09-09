package com.customer.service;


import com.customer.dto.CreateCustomerRequest;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.UpdateCustomerDto;

public interface IcustomerService {
    /**
     *
     * @param customerRequest - CustomerRequest  Object to create Customer
     */

    void createCustomer(CreateCustomerRequest customerRequest);

    /**
     *
     * @param email - email id of customer
     * @return @CustomerResponseDto object based on email
     */
     CustomerResponseDto fetchCustomer(String email);

    /**
     *
     * @param customerRequest
     * @param email
     * @return customer responseDto based on email and updated CustomerRequest object
     */
    CustomerResponseDto updateCustomer(UpdateCustomerDto customerRequest, String email);

    /**
     *
     * @param email
     */
    boolean deleteCustomer(String email);
}

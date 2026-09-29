package com.customer.service;


import com.customer.dto.ResponseDto;
import com.customer.dto.customer.CreateCustomerRequest;
import com.customer.dto.customer.CustomerResponseDto;
import com.customer.dto.customer.UpdateCustomerDto;

public interface IcustomerService {
    /**
     *
     * @param customerRequest - CustomerRequest  Object to create Customer
     * @param idempotencyKey
     * @param correlationId
     * @return
     */

    ResponseDto createCustomer(CreateCustomerRequest customerRequest, String idempotencyKey,
                               String correlationId);

    /**
     *
     * @param email         - email id of customer
     * @param correlationId
     * @return @CustomerResponseDto object based on email
     */
     CustomerResponseDto fetchCustomer(String email,String correlationId);

    /**
     *
     * @param id
     * @param correlationId
     * @return CustomerResponseDto based on id
     */
     CustomerResponseDto fetchCustomerById(Long id,String correlationId);
    /**
     *
     * @param customerRequest
     * @param email
     * @param correlationId
     * @return customer responseDto based on email and updated CustomerRequest object
     */
    CustomerResponseDto updateCustomer(UpdateCustomerDto customerRequest, String email,String correlationId);

    /**
     *
     * @param email
     * @param correlationId
     */
    boolean deleteCustomer(String email,String correlationId);

    /**
     *
     * @param customerId
     * @param correlationId
     * @return - boolean whether customer exists or not
     */
    Boolean checkCustomer(Long customerId,String correlationId);
}

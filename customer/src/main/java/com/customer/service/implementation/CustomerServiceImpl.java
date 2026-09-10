package com.customer.service.implementation;

import com.customer.dto.CreateCartDto;
import com.customer.dto.CreateCustomerRequest;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.UpdateCustomerDto;
import com.customer.entity.Customer;
import com.customer.exception.CustomerAlreadyExistException;
import com.customer.exception.DuplicateResourceException;
import com.customer.exception.ResourceNotFoundException;
import com.customer.mapper.CustomerMapper;
import com.customer.repository.CustomerRepository;
import com.customer.service.IcustomerService;
import com.customer.service.client.CartFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements IcustomerService {

    CustomerRepository customerRepository;
    CartFeignClient cartFeignClient;

    @Override
    public void createCustomer(CreateCustomerRequest customerRequest) {
        Optional<Customer> customerResponseDto = customerRepository.findByEmail(customerRequest.email());
        if(customerResponseDto.isPresent()){
            throw new CustomerAlreadyExistException("Customer Already registered with given email "
                    + customerRequest.email());
        }
        Customer customer = CustomerMapper.dtoToCreateCustomerMapper(new Customer(),customerRequest);
        customer = customerRepository.save(customer);

        try {
            CreateCartDto cartDto = new CreateCartDto(customer.getId());
            cartFeignClient.createCart(cartDto);
        } catch (Exception e) {
           customerRepository.deleteById(customer.getId());
           throw new IllegalStateException("cart cannot created so customer is also not created");
        }

    }

    @Override
    public CustomerResponseDto fetchCustomer(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        return CustomerMapper.customerToDtoMapper(customer.get());
    }

    @Override
    public CustomerResponseDto updateCustomer(UpdateCustomerDto customerRequest, String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        //later implement which fields should I allow to modify
        if(!Objects.equals(email, customerRequest.email())){
            Optional<Customer> duplicateCustomer = customerRepository.findByEmail(customerRequest.email());
            if(duplicateCustomer.isPresent()){
                throw new DuplicateResourceException("customer already Exist with updated email");
            }
        }
        Customer customerTOSave = CustomerMapper.dtoToUpdateCustomerMapper(customer.get(),customerRequest);
        customerTOSave = customerRepository.save(customerTOSave);
        return CustomerMapper.customerToDtoMapper(customerTOSave);
    }

    @Override
    public boolean deleteCustomer(String email) {

        Optional<Customer> customerResponseDto = customerRepository.findByEmail(email);
        if(customerResponseDto.isEmpty()){
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        customerRepository.deleteByEmail(email);
        return true;

    }

    @Override
    public Boolean checkCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()){
            return  Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }
}

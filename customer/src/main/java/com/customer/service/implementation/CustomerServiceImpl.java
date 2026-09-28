package com.customer.service.implementation;

import com.customer.dto.cart.CreateCartDto;
import com.customer.dto.customer.CreateCustomerRequest;
import com.customer.dto.customer.CustomerResponseDto;
import com.customer.dto.customer.UpdateCustomerDto;
import com.customer.entity.Customer;
import com.customer.entity.enums.CustomerStatus;
import com.customer.exception.CustomerAlreadyExistException;
import com.customer.exception.DuplicateResourceException;
import com.customer.exception.ResourceNotFoundException;
import com.customer.mapper.CustomerMapper;
import com.customer.repository.CustomerRepository;
import com.customer.service.IcustomerService;
import com.customer.service.client.CartFeignClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements IcustomerService {
    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final CartFeignClient cartFeignClient;

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
    public CustomerResponseDto fetchCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            throw new ResourceNotFoundException("Customer","CustomerId",id.toString());
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
        
        if (customer.isPresent()
                && customer.get().getCustomerStatus().equals(CustomerStatus.ACTIVE)) {

            logger.debug(
                    "Inside Customer Service: Customer verified successfully, customerId={}, status=ACTIVE",
                    customerId
            );

            return Boolean.TRUE;
        }
        logger.debug(
                "Inside Customer Service: Customer verification failed, customerId={}, customerExists={}, status={}",
                customerId,
                customer.isPresent(),
                customer.map(Customer::getCustomerStatus).orElse(null)
        );
        return Boolean.FALSE;
    }
}

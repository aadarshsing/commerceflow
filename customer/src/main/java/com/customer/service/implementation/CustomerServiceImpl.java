package com.customer.service.implementation;

import com.customer.dto.ResponseDto;
import com.customer.dto.customer.CreateCustomerRequest;
import com.customer.dto.customer.CustomerResponseDto;
import com.customer.dto.customer.UpdateCustomerDto;
import com.customer.entity.Customer;
import com.customer.entity.enums.CustomerStatus;
import com.customer.exception.DuplicateResourceException;
import com.customer.exception.ResourceNotFoundException;
import com.customer.mapper.CustomerMapper;
import com.customer.repository.CustomerRepository;
import com.customer.service.IcustomerService;
import com.customer.client.CartFeignClient;
import lombok.RequiredArgsConstructor;
import org.commerceflow.dto.cart.CreateCartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ResponseDto createCustomer(CreateCustomerRequest customerRequest, String idempotencyKey, String correlationId) {

        logger.info(
                "Inside Customer Service: Creating customer, email={}, correlationId={}, idempotencyKey={}",
                customerRequest.email(),
                correlationId,
                idempotencyKey
        );

        Optional<Customer> customerResponseDto = customerRepository.findByEmail(customerRequest.email());
        if(customerResponseDto.isPresent()){

            logger.info(
                    "Inside Customer Service: Customer already exists, email={}, correlationId={}",
                    customerRequest.email(),
                    correlationId
            );
            return new ResponseDto(
                    HttpStatus.FOUND.toString(),
                    "Customer Already Present for Given email: " + customerRequest.email()
            );
        }
        Optional<Customer> customerForIdempotent = customerRepository.findByIdempotencyKey(idempotencyKey);
        if (customerForIdempotent.isPresent()){
            logger.info(
                    "Inside Customer Service: Duplicate idempotency key detected, idempotencyKey={}, correlationId={}",
                    idempotencyKey,
                    correlationId
            );
            return new ResponseDto(
                    HttpStatus.FOUND.toString(),
                    "Customer Already Present for Given idempotencyKey: " + idempotencyKey
            );
        }
        Customer customer = CustomerMapper.dtoToCreateCustomerMapper(new Customer(),customerRequest);
        customer.setIdempotencyKey(idempotencyKey);
        customer = customerRepository.save(customer);

        logger.info(
                "Inside Customer Service: Customer created, customerId={}, correlationId={}",
                customer.getId(),
                correlationId
        );

        try {
            String idempotencyKeyForCart = "CREATE_CART_FOR_CUSTOMER_"+"CUSTOMER_ID_"+customer.getId();
            CreateCartDto cartDto = new CreateCartDto(customer.getId());
            cartFeignClient.createCart(cartDto, idempotencyKeyForCart);

            logger.info(
                    "Inside Customer Service: Cart created successfully, customerId={}, correlationId={}",
                    customer.getId(),
                    correlationId
            );
        } catch (Exception e) {

            logger.error(
                    "Inside Customer Service: Cart creation failed, customerId={}, correlationId={}, message={}",
                    customer.getId(),
                    correlationId,
                    e.getMessage(),
                    e
            );

           customerRepository.deleteById(customer.getId());
           throw new IllegalStateException("cart cannot created so customer is also not created");
        }
        return new ResponseDto(
                HttpStatus.CREATED.toString(),
                "Customer created Successfully"
        );

    }

    @Override
    public CustomerResponseDto fetchCustomer(String email, String correlationId) {
        logger.info(
                "Inside Customer Service: Fetching customer, email={}, correlationId={}",
                email,
                correlationId
        );
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            logger.warn(
                    "Inside Customer Service: Customer not found, email={}, correlationId={}",
                    email,
                    correlationId
            );
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        logger.info(
                "Inside Customer Service: Customer fetched successfully by Email, customerId={}, correlationId={}, email={}",
                customer.get().getId(),
                correlationId,
                email
        );
        return CustomerMapper.customerToDtoMapper(customer.get());
    }

    @Override
    public CustomerResponseDto fetchCustomerById(Long id, String correlationId) {
        logger.info(
                "Inside Customer Service: Fetching customer, customerId={}, correlationId={}",
                id,
                correlationId
        );
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            logger.warn(
                    "Inside Customer Service: Customer not found, customerId={}, correlationId={}",
                    id,
                    correlationId
            );
            throw new ResourceNotFoundException("Customer","CustomerId",id.toString());
        }
        logger.info(
                "Inside Customer Service: Customer fetched successfully, customerId={}, correlationId={}",
                customer.get().getId(),
                correlationId
        );
        return CustomerMapper.customerToDtoMapper(customer.get());
    }

    @Override
    public CustomerResponseDto updateCustomer(UpdateCustomerDto customerRequest, String email, String correlationId) {
        logger.info(
                "Inside Customer Service: Updating customer, email={}, correlationId={}",
                email,
                correlationId
        );
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            logger.warn(
                    "Inside Customer Service: Customer not found for update, email={}, correlationId={}",
                    email,
                    correlationId
            );
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        //later implement which fields should I allow to modify
        if(!Objects.equals(email, customerRequest.email())){
            Optional<Customer> duplicateCustomer = customerRepository.findByEmail(customerRequest.email());
            if(duplicateCustomer.isPresent()){
                logger.warn(
                        "Inside Customer Service: Email already exists, requestedEmail={}, correlationId={}",
                        customerRequest.email(),
                        correlationId
                );
                throw new DuplicateResourceException("customer already Exist with updated email");
            }
        }
        Customer customerToSave = CustomerMapper.dtoToUpdateCustomerMapper(customer.get(),customerRequest);
        customerToSave = customerRepository.save(customerToSave);

        logger.info(
                "Inside Customer Service: Customer updated successfully, customerId={}, correlationId={}",
                customerToSave.getId(),
                correlationId
        );

        return CustomerMapper.customerToDtoMapper(customerToSave);
    }

    @Override
    public boolean deleteCustomer(String email, String correlationId) {

        logger.info(
                "Inside Customer Service: Deleting customer, email={}, correlationId={}",
                email,
                correlationId
        );

        Optional<Customer> customerResponseDto = customerRepository.findByEmail(email);
        if(customerResponseDto.isEmpty()){
            logger.warn(
                    "Inside Customer Service: Customer not found with email, email={}, correlationId={}",
                    email,
                    correlationId
            );
            throw new ResourceNotFoundException("Customer","Email",email);
        }
        Long customerId = customerResponseDto.get().getId();
        try{
            cartFeignClient.deleteCart(customerResponseDto.get().getId());
            logger.info(
                    "Inside Customer Service: Cart deleted successfully, customerId={}, correlationId={}",
                    customerId,
                    correlationId
            );
        }
        catch (Exception e){
            logger.error(
                    "Inside Customer Service: Cart deletion failed, proceeding with customer deletion, customerId={}, correlationId={}, message={}",
                    customerId,
                    correlationId,
                    e.getMessage(),
                    e
            );
        }
        customerRepository.deleteByEmail(email);

        logger.info(
                "Inside Customer Service: Customer deleted successfully, customerId={}, correlationId={}",
                customerId,
                correlationId
        );

        return true;

    }

    @Override
    public Boolean checkCustomer(Long customerId, String correlationId) {
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

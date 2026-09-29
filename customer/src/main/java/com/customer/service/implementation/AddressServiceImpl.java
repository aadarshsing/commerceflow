package com.customer.service.implementation;

import com.customer.dto.address.AddressResponseDto;
import com.customer.dto.address.CreateAddressRequestDto;
import com.customer.entity.Address;
import com.customer.entity.Customer;
import com.customer.exception.ResourceNotFoundException;
import com.customer.mapper.AddressMapper;
import com.customer.repository.AddressRepository;
import com.customer.repository.CustomerRepository;
import com.customer.service.IAddressService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {
    private static  final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void createAddress(CreateAddressRequestDto addressRequestDto, String correlationId) {

        logger.info(
                "Inside Customer Service: Creating address, customerId={}, correlationId={}",
                addressRequestDto.customerId(),
                correlationId
        );
        Customer customer = customerRepository
                .findById(addressRequestDto.customerId())
                .orElseThrow(() -> {
                    logger.warn(
                            "Inside Customer Service: Customer not found while creating address, customerId={}, correlationId={}",
                            addressRequestDto.customerId(),
                            correlationId
                    );

                    return new ResourceNotFoundException(
                            "Customer",
                            "customerId",
                            addressRequestDto.customerId().toString()
                    );
                });

        Address address = AddressMapper.addressRequestDtoToAddressMapper(addressRequestDto,new Address());
        address.setCustomer(customer);
        addressRepository.save(address);

        logger.info(
                "Inside Customer Service: Address created successfully, customerId={}, addressId={}, correlationId={}",
                customer.getId(),
                address.getId(),
                correlationId
        );
    }

    @Override
    public AddressResponseDto getAddress(Long id, String correlationId) {
        logger.info(
                "Inside Customer Service: Fetching address, addressId={}, correlationId={}",
                id,
                correlationId
        );
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn(
                            "Inside Customer Service: Address not found, addressId={}, correlationId={}",
                            id,
                            correlationId
                    );

                    return new ResourceNotFoundException(
                            "Address",
                            "AddressId",
                            id.toString()
                    );
                });

        logger.info(
                "Inside Customer Service: Address fetched successfully, addressId={}, correlationId={}",
                id,
                correlationId
        );
        return AddressMapper.addressEntityToDtoMapper(address);
    }

}

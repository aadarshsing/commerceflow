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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements IAddressService {

    AddressRepository addressRepository;
    CustomerRepository customerRepository;

    @Override
    public void createAddress(CreateAddressRequestDto addressRequestDto) {

        Customer customer = customerRepository.findById(addressRequestDto.customerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Customer","customerId",addressRequestDto.customerId().toString())
        );
        Address address = AddressMapper.addressRequestDtoToAddressMapper(addressRequestDto,new Address());
        address.setCustomer(customer);
        addressRepository.save(address);
    }

    @Override
    public AddressResponseDto getAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Address","AddressId",id.toString())
        );
        return AddressMapper.addressEntityToDtoMapper(address);
    }

}

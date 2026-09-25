package com.order.client.fallback;

import com.order.exception.ServiceUnavailableException;
import com.order.client.CustomerFeignClient;
import org.commerceflow.dto.customer.AddressResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerFallBack implements CustomerFeignClient {
    @Override
    public ResponseEntity<AddressResponseDto> getAddress(Long addressId) {
        throw new ServiceUnavailableException(
                "Customer Service is currently unavailable"
        );
    }

    @Override
    public ResponseEntity<Boolean> checkCustomerExist(Long customerId) {
        throw new ServiceUnavailableException(
                "Customer Service is currently unavailable"
        );
    }
}

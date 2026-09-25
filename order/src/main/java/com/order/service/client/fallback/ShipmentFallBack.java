package com.order.service.client.fallback;

import com.order.dto.ResponseDto;
import com.order.exception.ServiceUnavailableException;
import com.order.service.client.ShipmentFeignClient;
import org.commerceflow.dto.shipment.CreateShipmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ShipmentFallBack implements ShipmentFeignClient {
    @Override
    public ResponseEntity<ResponseDto> createShipment(String shipmentIdempotencyKey, CreateShipmentDto createShipmentDto) {
        throw new ServiceUnavailableException(
                "Shipment Service is currently unavailable"
        );
    }
}

package com.shipment.service.implementation;

import com.shipment.dto.CreateShipmentDto;
import com.shipment.dto.OrderAddressResponseDto;
import com.shipment.dto.ResponseDto;
import com.shipment.dto.ShipmentResponseDto;
import com.shipment.entity.Shipment;
import com.shipment.entity.enums.ShipmentStatus;
import com.shipment.exception.ResourceNotFoundException;
import com.shipment.mapper.ShipmentMapper;
import com.shipment.repository.ShipmentRepository;
import com.shipment.service.IshipmentService;
import com.shipment.service.client.CustomerFeignClient;
import com.shipment.service.client.OrderFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements IshipmentService {

    CustomerFeignClient customerFeignClient;
    OrderFeignClient orderFeignClient;
    ShipmentRepository shipmentRepository;

    @Override
    public ResponseDto createShipment(CreateShipmentDto createShipmentDto, OrderAddressResponseDto orderAddressResponseDto) {
        Boolean isExist = customerFeignClient.checkCustomerExist(createShipmentDto.customerId()).getBody();
        if(Boolean.FALSE.equals(isExist)){
            throw new ResourceNotFoundException("Customer","customerId",createShipmentDto.customerId().toString());
        }
        isExist = orderFeignClient.checkOrder(createShipmentDto.orderId()).getBody();
        if(Boolean.FALSE.equals(isExist)){
            throw new ResourceNotFoundException("Order","orderId",createShipmentDto.orderId().toString());
        }
        Shipment shipment = ShipmentMapper.createShipmentDtoToShipmentMapper(new Shipment(),createShipmentDto);
        shipmentRepository.save(shipment);
        return new ResponseDto(
                HttpStatus.CREATED.toString(),
                shipment.getId().toString()
        );
    }

    @Override
    public ShipmentResponseDto getShipment(Long shipmentId) {
        return null;
    }

    @Override
    public ShipmentResponseDto getShipmentByCustomer(Long customerId) {
        return null;
    }

    @Override
    public ShipmentResponseDto getShipmentByOrder(Long orderId) {
        return null;
    }

    @Override
    public ShipmentResponseDto updateShipmentStatus(ShipmentStatus status) {
        return null;
    }

    @Override
    public ResponseDto cancelShipment(Long shipmentId) {
        return null;
    }
}

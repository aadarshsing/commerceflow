package com.shipment.mapper;

import com.shipment.dto.shipment.CreateShipmentDto;
import com.shipment.dto.order.OrderAddressResponseDto;
import com.shipment.dto.shipment.ShipmentAddressResponseDto;
import com.shipment.dto.shipment.ShipmentResponseDto;
import com.shipment.entity.Shipment;
import com.shipment.entity.ShippingAddress;
import com.shipment.entity.enums.ShipmentStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ShipmentMapper {

    public static Shipment createShipmentDtoToShipmentMapper(Shipment shipment, CreateShipmentDto createShipmentDto){
        shipment.setOrderId(createShipmentDto.orderId());
        shipment.setCustomerId(createShipmentDto.customerId());
        shipment.setTrackingNumber(generateTrackingNumber());
        shipment.setCarrier("ShipRocket");
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setEstimatedDeliveryDate(
                Instant.now().plus(15, ChronoUnit.DAYS)
        );
        return shipment;
    }
    public static String generateTrackingNumber() {
        return "CF" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
    public static ShippingAddress orderAddressToShippingAddressMapper(ShippingAddress shippingAddress, OrderAddressResponseDto orderAddressResponseDto){

        shippingAddress.setAddressLine1(orderAddressResponseDto.addressLine1());
        shippingAddress.setAddressLine2(orderAddressResponseDto.addressLine2());
        shippingAddress.setCity(orderAddressResponseDto.city());
        shippingAddress.setState(orderAddressResponseDto.state());
        shippingAddress.setCountry(orderAddressResponseDto.country());
        shippingAddress.setPostalCode(orderAddressResponseDto.postalCode());
        shippingAddress.setPhoneNumber(orderAddressResponseDto.phoneNumber());

        return shippingAddress;
    }

    public static ShipmentResponseDto shipmentToShipmentResponseDtoMapper (Shipment shipment){
        return new ShipmentResponseDto(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getCustomerId(),
                shipment.getTrackingNumber(),
                shipment.getCarrier(),
                shipment.getStatus(),
                shipmentAddressToDto(shipment.getShippingAddress()),
                shipment.getEstimatedDeliveryDate(),
                shipment.getShippedAt(),
                shipment.getDeliveredAt()
        );
    }
    public static ShipmentAddressResponseDto shipmentAddressToDto(ShippingAddress shippingAddress ){
        return new ShipmentAddressResponseDto(
                shippingAddress.getId(),
                shippingAddress.getAddressLine1(),
                shippingAddress.getAddressLine2(),
                shippingAddress.getCity(),
                shippingAddress.getState(),
                shippingAddress.getCountry(),
                shippingAddress.getPostalCode(),
                shippingAddress.getPhoneNumber()
        );
    }
}

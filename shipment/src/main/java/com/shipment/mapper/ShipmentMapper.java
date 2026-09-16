package com.shipment.mapper;

import com.shipment.dto.CreateShipmentDto;
import com.shipment.dto.OrderAddressResponseDto;
import com.shipment.dto.ShipmentResponseDto;
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
        shipment.setShippingAddress(orderAddressToShippingAddressMapper(new ShippingAddress(),createShipmentDto.shippingAddress()));
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

    public static ShipmentResponseDto shipmentToshipmentResponseDtoMapper (Shipment shipment){
        return new ShipmentResponseDto(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getCustomerId(),
                shipment.getTrackingNumber(),
                shipment.getCarrier(),
                shipment.getStatus(),
                shipment.getShippingAddress(),
                shipment.getEstimatedDeliveryDate(),
                shipment.getShippedAt(),
                shipment.getDeliveredAt()
        );
    }
}

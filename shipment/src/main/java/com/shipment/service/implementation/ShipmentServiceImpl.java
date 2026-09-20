package com.shipment.service.implementation;

import com.shipment.dto.notification.CreateNotificationDto;
import com.shipment.dto.shipment.CreateShipmentDto;
import com.shipment.dto.shipment.ResponseDto;
import com.shipment.dto.shipment.ShipmentResponseDto;
import com.shipment.entity.Shipment;
import com.shipment.entity.ShippingAddress;
import com.shipment.entity.enums.NotificationType;
import com.shipment.entity.enums.ShipmentStatus;
import com.shipment.exception.ResourceNotFoundException;
import com.shipment.mapper.ShipmentMapper;
import com.shipment.repository.ShipmentRepository;
import com.shipment.service.IshipmentService;
import com.shipment.service.client.CustomerFeignClient;
import com.shipment.service.client.NotificationFeignClient;
import com.shipment.service.client.OrderFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements IshipmentService {

    CustomerFeignClient customerFeignClient;
    OrderFeignClient orderFeignClient;
    NotificationFeignClient notificationFeignClient;
    ShipmentRepository shipmentRepository;

    @Override
    public ResponseDto createShipment(CreateShipmentDto createShipmentDto) {
        Boolean isExist = customerFeignClient.checkCustomerExist(createShipmentDto.customerId()).getBody();
        if(Boolean.FALSE.equals(isExist)){
            throw new ResourceNotFoundException("Customer","customerId",createShipmentDto.customerId().toString());
        }
        isExist = orderFeignClient.checkOrder(createShipmentDto.orderId()).getBody();
        if(Boolean.FALSE.equals(isExist)){
            throw new ResourceNotFoundException("Order","orderId",createShipmentDto.orderId().toString());
        }
        Shipment shipment = ShipmentMapper.createShipmentDtoToShipmentMapper(new Shipment(),createShipmentDto);
        ShippingAddress shippingAddress = ShipmentMapper.orderAddressToShippingAddressMapper(new ShippingAddress(),createShipmentDto.shippingAddress());
        shippingAddress.setShipment(shipment);

        shipment.setShippingAddress(shippingAddress);
        shipmentRepository.save(shipment);
        notificationFeignClient.createNotification(
                new CreateNotificationDto(
                        createShipmentDto.customerId(),
                        NotificationType.SHIPMENT_CREATED,
                        "POP UP",
                        "Order",
                        "Shipment Created Successfully",
                        createShipmentDto.orderId().toString()
                )
        );
        return new ResponseDto(
                HttpStatus.CREATED.toString(),
                shipment.getId().toString()
        );
    }

    @Override
    public ShipmentResponseDto getShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Shipment",
                        "shipmentId",
                        shipmentId.toString()
                )
        );
        return ShipmentMapper.shipmentToShipmentResponseDtoMapper(shipment);
    }

    @Override
    public ShipmentResponseDto getShipmentByCustomer(Long customerId) {
        Shipment shipment = shipmentRepository.findByCustomerId(customerId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Shipment",
                        "customerId",
                        customerId.toString()
                )
        );
        return ShipmentMapper.shipmentToShipmentResponseDtoMapper(shipment);
    }

    @Override
    public ShipmentResponseDto getShipmentByOrder(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Shipment",
                        "orderId",
                        orderId.toString()
                )
        );
        return ShipmentMapper.shipmentToShipmentResponseDtoMapper(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponseDto updateShipmentStatus(ShipmentStatus status, Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment",
                        "shipmentId",
                        shipmentId.toString()
                ));

        ShipmentStatus currentStatus = shipment.getStatus();

        boolean validTransition = false;

        if (currentStatus == ShipmentStatus.CREATED
                && status == ShipmentStatus.PACKED) {

            validTransition = true;

        } else if (currentStatus == ShipmentStatus.PACKED
                && status == ShipmentStatus.SHIPPED) {

            validTransition = true;

        } else if (currentStatus == ShipmentStatus.SHIPPED
                && status == ShipmentStatus.IN_TRANSIT) {

            validTransition = true;

        } else if (currentStatus == ShipmentStatus.IN_TRANSIT
                && status == ShipmentStatus.OUT_FOR_DELIVERY) {

            validTransition = true;

        } else if (currentStatus == ShipmentStatus.OUT_FOR_DELIVERY
                && status == ShipmentStatus.DELIVERED) {

            validTransition = true;

        } else if (status == ShipmentStatus.CANCELLED
                && currentStatus != ShipmentStatus.DELIVERED
                && currentStatus != ShipmentStatus.CANCELLED) {

            validTransition = true;
        }

        if (!validTransition) {
            throw new IllegalStateException(
                    "Invalid shipment status transition from "
                            + currentStatus + " to " + status
            );
        }

        shipment.setStatus(status);

        if (status == ShipmentStatus.SHIPPED) {
            shipment.setShippedAt(Instant.now());
        }
        if(status == ShipmentStatus.DELIVERED){
            shipment.setDeliveredAt(Instant.now());
        }

        Shipment savedShipment = shipmentRepository.save(shipment);

        sendShipmentNotification(savedShipment);

        return ShipmentMapper.shipmentToShipmentResponseDtoMapper(savedShipment);
    }

    @Override
    public ShipmentResponseDto cancelShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment",
                        "shipmentId",
                        shipmentId.toString()
                ));

        ShipmentStatus currentStatus = shipment.getStatus();

        if (currentStatus == ShipmentStatus.CREATED) {

            shipment.setStatus(ShipmentStatus.CANCELLED);

        } else if (currentStatus == ShipmentStatus.PACKED) {

            shipment.setStatus(ShipmentStatus.CANCELLED);

        } else if (currentStatus == ShipmentStatus.SHIPPED) {

            shipment.setStatus(ShipmentStatus.CANCELLED);

        } else if (currentStatus == ShipmentStatus.IN_TRANSIT) {

            shipment.setStatus(ShipmentStatus.CANCELLED);

        } else if (currentStatus == ShipmentStatus.OUT_FOR_DELIVERY) {

            throw new IllegalStateException(
                    "Shipment cannot be cancelled because it is already out for delivery"
            );

        } else if (currentStatus == ShipmentStatus.DELIVERED) {

            throw new IllegalStateException(
                    "Shipment cannot be cancelled because it has already been delivered"
            );

        } else if (currentStatus == ShipmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Shipment is already cancelled"
            );
        }

        Shipment savedShipment = shipmentRepository.save(shipment);

        sendShipmentCancellationNotification(savedShipment);

        return ShipmentMapper.shipmentToShipmentResponseDtoMapper(savedShipment);
    }
    private void sendShipmentNotification(Shipment shipment) {

        String message = switch (shipment.getStatus()) {

            case PACKED ->
                    "Your order has been packed.";

            case SHIPPED ->
                    "Your order has been shipped. Tracking number: "
                            + shipment.getTrackingNumber();

            case IN_TRANSIT ->
                    "Your order is currently in transit.";

            case OUT_FOR_DELIVERY ->
                    "Your order is out for delivery.";

            case DELIVERED ->
                    "Your order has been delivered.";

            case CANCELLED ->
                    "Your shipment has been cancelled.";

            default -> null;
        };
        NotificationType notificationType = switch (shipment.getStatus()) {

            case SHIPPED -> NotificationType.SHIPMENT_SHIPPED;

            case DELIVERED -> NotificationType.SHIPMENT_DELIVERED;

            case CANCELLED -> NotificationType.SHIPMENT_CANCELLED;

            default -> null;
        };

        if (message == null || notificationType == null) {
            return;
        }

        CreateNotificationDto request = new CreateNotificationDto(
                shipment.getCustomerId(),
                notificationType,
                "SMS",
                "Shipment",
                message,
                shipment.getId().toString()
        );

        notificationFeignClient.createNotification(request);
    }
    private void sendShipmentCancellationNotification(Shipment shipment) {

        CreateNotificationDto request = new CreateNotificationDto(
                shipment.getCustomerId(),
                NotificationType.SHIPMENT_CANCELLED,
                "Your shipment with tracking number "
                        + shipment.getTrackingNumber()
                        + " has been cancelled.",
                "Shipment",
                "Shipment has been cancelled",
                shipment.getId().toString()
        );

        notificationFeignClient.createNotification(request);
    }
}

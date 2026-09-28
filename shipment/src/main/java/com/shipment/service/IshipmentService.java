package com.shipment.service;

import com.shipment.dto.CreateShipmentDto;
import com.shipment.dto.ResponseDto;
import com.shipment.dto.ShipmentResponseDto;
import com.shipment.entity.enums.ShipmentStatus;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IshipmentService {


    /**
     *
     * @param createShipmentDto
     * @param shipmentIdempotencyKey
     * @return
     */
    ResponseDto createShipment(CreateShipmentDto createShipmentDto, String shipmentIdempotencyKey);

    /**
     *
     * @param shipmentId
     * @return
     */
    ShipmentResponseDto getShipment(Long shipmentId);

    /**
     *
     * @param customerId
     * @return
     */
    ShipmentResponseDto getShipmentByCustomer(Long customerId);

    /**
     *
     * @param orderId
     * @return
     */
    ShipmentResponseDto getShipmentByOrder(Long orderId);

    /**
     *
     * @param status
     * @param shipmentId
     * @param correlationId
     * @return
     */
    ShipmentResponseDto updateShipmentStatus(ShipmentStatus status, Long shipmentId, String correlationId);

    /**
     *
     * @param shipmentId
     * @return
     */
    ShipmentResponseDto cancelShipment(Long shipmentId);


}

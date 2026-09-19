package com.shipment.service;

import com.shipment.dto.shipment.CreateShipmentDto;
import com.shipment.dto.shipment.ResponseDto;
import com.shipment.dto.shipment.ShipmentResponseDto;
import com.shipment.entity.enums.ShipmentStatus;

public interface IshipmentService {


    /**
     *
     * @param createShipmentDto
     * @return
     */
    ResponseDto createShipment(CreateShipmentDto createShipmentDto);

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
     * @return
     */
    ShipmentResponseDto updateShipmentStatus(ShipmentStatus status,Long shipmentId);

    /**
     *
     * @param shipmentId
     * @return
     */
    ShipmentResponseDto cancelShipment(Long shipmentId);


}

package com.shipment.service;

import com.shipment.dto.CreateShipmentDto;
import com.shipment.dto.OrderAddressResponseDto;
import com.shipment.dto.ResponseDto;
import com.shipment.dto.ShipmentResponseDto;
import com.shipment.entity.enums.ShipmentStatus;

public interface IshipmentService {


    /**
     *
     * @param createShipmentDto
     * @param orderAddressResponseDto
     * @return
     */
    ResponseDto createShipment(CreateShipmentDto createShipmentDto, OrderAddressResponseDto orderAddressResponseDto);

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
     * @return
     */
    ShipmentResponseDto updateShipmentStatus(ShipmentStatus status);

    /**
     *
     * @param shipmentId
     * @return
     */
    ResponseDto cancelShipment(Long shipmentId);


}

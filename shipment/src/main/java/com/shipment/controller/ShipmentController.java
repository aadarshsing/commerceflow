package com.shipment.controller;


import com.shipment.dto.shipment.CreateShipmentDto;
import com.shipment.dto.shipment.ResponseDto;
import com.shipment.dto.shipment.ShipmentResponseDto;
import com.shipment.entity.enums.ShipmentStatus;
import com.shipment.service.IshipmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ShipmentController {

    @Autowired
    IshipmentService ishipmentService;

    @PostMapping("/shipments")
    public ResponseEntity<ResponseDto> createShipment(
            @NotBlank(message = "shipmentIdempotencyKey cannot be null, empty or blank")
            @RequestParam String shipmentIdempotencyKey,
            @Valid @RequestBody CreateShipmentDto createShipmentDto){
        ResponseDto responseDto = ishipmentService.createShipment(createShipmentDto,shipmentIdempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/shipments/{shipmentId}")
    public ResponseEntity<ShipmentResponseDto> getShipment(
            @NotNull(message = "shipment id cannot be null")
            @PathVariable Long shipmentId){

        ShipmentResponseDto shipmentResponseDto = ishipmentService.getShipment(shipmentId);
        return ResponseEntity.ok(shipmentResponseDto);
    }
    @GetMapping("/customers/{customerId}/shipments")
    public ResponseEntity<ShipmentResponseDto> getShipmentByCustomerId(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId){
        ShipmentResponseDto shipmentResponseDto = ishipmentService.getShipmentByCustomer(customerId);
        return ResponseEntity.ok(shipmentResponseDto);
    }
    @GetMapping("/orders/{orderId}/shipment")
    public ResponseEntity<ShipmentResponseDto> getShipmentByOrderId(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long orderId){
        ShipmentResponseDto shipmentResponseDto = ishipmentService.getShipmentByOrder(orderId);
        return ResponseEntity.ok(shipmentResponseDto);
    }

    @PatchMapping("/shipments/{shipmentId}/status")
    public ResponseEntity<ShipmentResponseDto> updateShipmentStatus(
            @NotNull(message = "shipment Id cannot be null")
            @PathVariable
            Long shipmentId,
            @NotNull(message = "shipment Status cannot be null")
            @RequestParam
            ShipmentStatus shipmentStatus){

        ShipmentResponseDto shipmentResponseDto = ishipmentService.updateShipmentStatus(shipmentStatus,shipmentId);
        return ResponseEntity.ok(shipmentResponseDto);
    }
}

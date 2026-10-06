package com.catalog.seller.controller;


import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.ResponseDto;
import com.catalog.seller.dto.SellerResponseDto;
import com.catalog.seller.service.IsellerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class SellerController {

    @Autowired
    IsellerService sellerService;

    @GetMapping("/seller")
    ResponseEntity<SellerResponseDto> getSellerByEmail(
            @Email(message = "Invalid email format")@RequestParam String email,
            @RequestHeader("commerceflow-correlation-id") String correlationId){
        SellerResponseDto sellerResponseDto = sellerService.fetchSeller(email,correlationId);
        return ResponseEntity.ok(sellerResponseDto);
    }

    @PostMapping("/seller")
    ResponseEntity<SellerResponseDto> createSeller(@Valid @RequestBody CreateSellerRequestDto sellerRequestDto,@RequestHeader("Idempotency-Key") String idempotencyKey,
                                             @RequestHeader("commerceflow-correlation-id") String correlationId){
        SellerResponseDto sellerResponseDto = sellerService.createSeller(sellerRequestDto,idempotencyKey,correlationId);
        return new ResponseEntity<>(
                sellerResponseDto,
                HttpStatus.CREATED
        );

    }

    @PutMapping("/seller")
    ResponseEntity<SellerResponseDto> updateSeller(
            @Valid @RequestBody CreateSellerRequestDto sellerRequestDto,
            @Email(message = "Invalid email format") String email,
            @RequestHeader("commerceflow-correlation-id") String correlationId){
        SellerResponseDto sellerResponseDto = sellerService.updateSeller(sellerRequestDto,email,correlationId);
        return new ResponseEntity<>(
                sellerResponseDto,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/seller")
    ResponseEntity<ResponseDto> deleteSeller(
            @Email(message = "Invalid email format") @RequestParam String email,
            @RequestHeader("commerceflow-correlation-id") String correlationId){
        boolean isDeleted = sellerService.deleteSeller(email,correlationId);
        if(isDeleted){
            return new ResponseEntity<>(
                    new ResponseDto(
                            HttpStatus.OK.toString(),
                            "seller deleted successfully"
                    ),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        "seller does not deleted "
                ),
                HttpStatus.OK
        );
    }

}

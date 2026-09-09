package com.customer.controller;



import com.customer.dto.*;
import com.customer.service.IAddressService;
import com.customer.service.IcustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class CustomerController {

    IcustomerService customerService;
    IAddressService addressService;

    @PostMapping("/customer")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest){
        customerService.createCustomer(createCustomerRequest);

        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "Customer Created Successfully"
                        ),
                HttpStatus.CREATED

        );
    }

    @GetMapping("/customer")
    public ResponseEntity<CustomerResponseDto> fetchCustomer(@Email(message = "Invalid email format") @RequestParam String email){
        CustomerResponseDto customerResponseDto = customerService.fetchCustomer(email);
        return ResponseEntity.ok(customerResponseDto);
    }
    @PutMapping("/customer")
    public  ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody @Valid UpdateCustomerDto updateCustomerDto,
                                                               @Email(message = "Invalid email format") @RequestParam  String email){
        CustomerResponseDto customerResponseDto = customerService.updateCustomer(updateCustomerDto,email);
        return new ResponseEntity<>(
                customerResponseDto,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/customer")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam @Email(message = "Invalid email format") String email) {
        boolean isDeleted = customerService.deleteCustomer(email);
        if (isDeleted) {
            return new ResponseEntity<>(
                    new ResponseDto(
                            HttpStatus.OK.toString(),
                            "Customer deleted Successfully"
                    ),
                    HttpStatus.OK
            );
        }
        else{
            return new ResponseEntity<>(
                    new ResponseDto(
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            "Customer don't deleted"
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


    }


    @PostMapping("/address")
    ResponseEntity<ResponseDto> createAddressForCustomer(@Valid @RequestBody CreateAddressRequestDto createAddressRequestDto){
        addressService.createAddress(createAddressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "Address created successfully"
                )
        );
    }


}

package com.catalog.category.controller;


import com.catalog.category.dto.CategoryResponseDto;
import com.catalog.category.dto.CreateCategoryRequestDto;
import com.catalog.category.dto.UpdateCategoryRequest;
import com.catalog.category.service.IcategoryService;
import com.catalog.product.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CategoryController {

    @Autowired
    IcategoryService icategoryService;

    @PostMapping("/category")
    ResponseEntity<ResponseDto> createCategory(@RequestBody CreateCategoryRequestDto categoryRequestDto){
        icategoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "Category created Successfully"
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/category")
    ResponseEntity<CategoryResponseDto> getCategory(@NotNull(message = "id cannot be null")
                                                    @Positive(message = "id must be a positive number") @RequestParam  Long id){
        CategoryResponseDto categoryResponseDto = icategoryService.getCategory(id);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/category/child")
    ResponseEntity<List<CategoryResponseDto>> getChildCategories(@NotNull(message = "id cannot be null")
                                                                 @Positive(message = "id must be a positive number") @RequestParam Long id){
        List<CategoryResponseDto> categoryResponseDtos = icategoryService.getAllChildCategories(id);
        return  ResponseEntity.ok(categoryResponseDtos);
    }

    @PutMapping("/category")
    ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest, @NotNull(message = "id cannot be null")
    @Positive(message = "id must be a positive number") @RequestParam Long id){
        CategoryResponseDto categoryResponseDto = icategoryService.updateCategory(updateCategoryRequest,id);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @DeleteMapping("/category")
    ResponseEntity<ResponseDto> deleteCategory(@NotNull(message = "id cannot be null")
                                               @Positive(message = "id must be a positive number") @RequestParam Long id){
        boolean isDeleted = icategoryService.deleteCategory(id);
        if(isDeleted){
            return ResponseEntity.ok(
                    new ResponseDto(
                            HttpStatus.OK.toString(),
                            "category is deleted successfully"
                    )
            );
        }
        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        "category is not deleted due to some issue"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}

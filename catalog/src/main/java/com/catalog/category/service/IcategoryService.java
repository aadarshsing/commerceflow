package com.catalog.category.service;


import com.catalog.category.dto.CategoryResponseDto;
import com.catalog.category.dto.CreateCategoryRequestDto;
import com.catalog.category.dto.UpdateCategoryRequest;

import java.util.List;

public interface IcategoryService {

    /**
     *
     * @param categoryRequestDto
     */
    void createCategory(CreateCategoryRequestDto categoryRequestDto);

    /**
     * @param id
     * @return -- it returns the category
     */
    CategoryResponseDto getCategory(Long id);

    /**
     *
     * @param id
     * @return -- it return the list of all active child category of given id of parent category
     */
    List<CategoryResponseDto> getAllChildCategories(Long id);

    /**
     *
     * @param categoryRequest
     * @return -- it return the updated categoryDTO
     */
    CategoryResponseDto updateCategory(UpdateCategoryRequest categoryRequest, Long id);

    /**
     *
     * @param id
     * @return -- it return true or false based on whether the category is deleted or not
     */
    boolean deleteCategory(Long id);
}

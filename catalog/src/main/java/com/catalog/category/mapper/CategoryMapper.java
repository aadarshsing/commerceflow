package com.catalog.category.mapper;


import com.catalog.category.dto.CategoryResponseDto;
import com.catalog.category.dto.CreateCategoryRequestDto;
import com.catalog.category.dto.UpdateCategoryRequest;
import com.catalog.category.entity.Category;
import com.catalog.category.entity.enums.CategoryStatus;

public class CategoryMapper {


    public static Category dtoToCreateCategory(Category category, CreateCategoryRequestDto categoryRequestDto){

        category.setName(categoryRequestDto.name());
        category.setDescription(categoryRequestDto.description());
        category.setStatus(CategoryStatus.ACTIVE);
        return category;
    }

    public  static CategoryResponseDto categoryToDto (Category category){

        Long parentCategory = category.getParentCategory() == null ? null : category.getParentCategory().getId();

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                parentCategory,
                category.getSubCategories().stream().map(CategoryMapper::categoryToDto).toList()
        );
    }

    public  static Category dtoToUpdateCategory(Category category, UpdateCategoryRequest categoryRequest){

        category.setName(categoryRequest.name());
        category.setDescription(categoryRequest.description());
        category.setStatus(categoryRequest.status());
        return category;

    }
}

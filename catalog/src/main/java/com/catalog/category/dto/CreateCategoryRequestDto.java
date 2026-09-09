package com.catalog.category.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotEmpty(message = "Category name cannot be null or empty")
        @Size(min = 2, max = 30, message = "The length of category name should be between 2 to 30")
        String name,
        String description,
        Long parentCategoryId
) {
}

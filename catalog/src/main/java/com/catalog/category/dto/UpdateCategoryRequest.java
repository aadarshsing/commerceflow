package com.catalog.category.dto;

import com.catalog.category.entity.enums.CategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotEmpty(message = "Category name cannot be null or empty")
        @Size(min = 2, max = 30, message = "The length of category name should be between 2 to 30")
        String name,
        String description,
        @NotNull(message = "Status cannot be null")
        CategoryStatus status
) {
}

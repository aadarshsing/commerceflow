package com.catalog.category.dto;

import java.util.List;

public record CategoryResponseDto(
        Long id,
        String name,
        String description,
        Long parentCategoryId,
        List<CategoryResponseDto> childCategoryList
) {
}

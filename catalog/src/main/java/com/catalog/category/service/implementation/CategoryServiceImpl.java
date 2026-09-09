package com.catalog.category.service.implementation;

import com.catalog.category.dto.CategoryResponseDto;
import com.catalog.category.dto.CreateCategoryRequestDto;
import com.catalog.category.dto.UpdateCategoryRequest;
import com.catalog.category.entity.Category;
import com.catalog.category.entity.enums.CategoryStatus;
import com.catalog.category.mapper.CategoryMapper;
import com.catalog.category.repository.CategoryRepository;
import com.catalog.category.service.IcategoryService;
import com.catalog.exception.ResourceNotActiveException;
import com.catalog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements IcategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void createCategory(CreateCategoryRequestDto categoryRequestDto) {
        if(categoryRequestDto.parentCategoryId() == null) {
            Category category = CategoryMapper.dtoToCreateCategory(new Category(), categoryRequestDto);
            category.setParentCategory(null);
            categoryRepository.save(category);
        }
        else{
            Optional<Category> parentCategory = categoryRepository.findById(categoryRequestDto.parentCategoryId());
            if(parentCategory.isEmpty()){
                throw new ResourceNotFoundException(
                        "Parentcategory",
                        "ParentCategoryId",
                        categoryRequestDto.parentCategoryId().toString()
                );
            }
            else if(!parentCategory.get().getStatus().equals(CategoryStatus.ACTIVE)){
                throw  new ResourceNotActiveException("Category","categoryId",parentCategory.get().getId().toString()
                );
            }
            else{
                Category category = CategoryMapper.dtoToCreateCategory(new Category(),categoryRequestDto);
                category.setParentCategory(parentCategory.get());
                categoryRepository.save(category);
            }
        }

    }

    @Override
    public CategoryResponseDto getCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException(
                    "Category",
                    "CategoryId",
                    id.toString()
            );
        }
        return CategoryMapper.categoryToDto(category.get());
    }

    @Override
    public List<CategoryResponseDto> getAllChildCategories(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException(
                    "Category",
                    "CategoryId",
                    id.toString()
            );
        }
        List<Category> subCategories= category.get().getSubCategories();

        return subCategories.stream()
                .map(CategoryMapper::categoryToDto)
                .toList();
    }

    @Override
    public CategoryResponseDto updateCategory(UpdateCategoryRequest categoryRequest, Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException(
                    "Category",
                    "CategoryId",
                    id.toString()
            );
        }
//        Optional<Category> parentCategory = categoryRepository.findById(categoryRequest.parentCategoryId());
//        if(parentCategory.isEmpty()){
//            throw new ResourceNotFoundException(
//                    "Parentcategory",
//                    "ParentCategoryId",
//                    categoryRequest.parentCategoryId().toString()
//            );
//        }
//        else if(!parentCategory.get().getStatus().equals("ACTIVE")){
//            throw  new CategoryNotActiveException(
//                    "Parent category is not Active"
//            );
//        }
        Category categoryToUpdate = CategoryMapper.dtoToUpdateCategory(category.get(), categoryRequest);
        categoryRepository.save(categoryToUpdate);
        return CategoryMapper.categoryToDto(categoryToUpdate);
    }

    @Override
    public boolean deleteCategory(Long id) {
       Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException(
                    "Category",
                    "CategoryId",
                    id.toString()
            );
        }
        else if(category.get().getSubCategories().isEmpty()){
            return false;
        }
        categoryRepository.deleteById(category.get().getId());
        return true;
    }
}

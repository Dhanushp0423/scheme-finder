package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto.CategoryResponse createCategory(CategoryDto.CreateCategoryRequest request);
    CategoryDto.CategoryResponse updateCategory(Long id, CategoryDto.CreateCategoryRequest request);
    CategoryDto.CategoryResponse getCategoryById(Long id);
    List<CategoryDto.CategoryResponse> getAllCategories();
    List<CategoryDto.CategoryResponse> getActiveCategories();
    void deleteCategory(Long id);
}

package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.CategoryDto;
import com.example.scheme.finder.entity.Category;
import com.example.scheme.finder.exception.BadRequestException;
import com.example.scheme.finder.exception.ResourceNotFoundException;
import com.example.scheme.finder.repository.CategoryRepository;
import com.example.scheme.finder.repository.SchemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SchemeRepository schemeRepository;

    @Override
    @Transactional
    public CategoryDto.CategoryResponse createCategory(CategoryDto.CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .colorCode(request.getColorCode())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isActive(true)
                .build();

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto.CategoryResponse updateCategory(Long id, CategoryDto.CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIconUrl(request.getIconUrl());
        category.setColorCode(request.getColorCode());
        if (request.getDisplayOrder() != null) category.setDisplayOrder(request.getDisplayOrder());

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryDto.CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToResponse(category);
    }

    @Override
    public List<CategoryDto.CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto.CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setIsActive(false);
        categoryRepository.save(category);
    }

//    private CategoryDto.CategoryResponse mapToResponse(Category category) {
//        long schemeCount = category.getSchemes() != null ? category.getSchemes().size() : 0;
//        return CategoryDto.CategoryResponse.builder()
//                .id(category.getId())
//                .name(category.getName())
//                .description(category.getDescription())
//                .iconUrl(category.getIconUrl())
//                .colorCode(category.getColorCode())
//                .isActive(category.getIsActive())
//                .displayOrder(category.getDisplayOrder())
//                .schemeCount(schemeCount)
//                .createdAt(category.getCreatedAt())
//                .build();
//    }

    private CategoryDto.CategoryResponse mapToResponse(Category category) {
        Long schemeCount = categoryRepository.countActiveSchemesByCategoryId(category.getId());
        return CategoryDto.CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .colorCode(category.getColorCode())
                .isActive(category.getIsActive())
                .displayOrder(category.getDisplayOrder())
                .schemeCount(schemeCount)
                .createdAt(category.getCreatedAt())
                .build();
    }
}
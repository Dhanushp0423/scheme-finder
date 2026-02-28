package com.example.scheme.finder.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CategoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String description;
        private String iconUrl;
        private String colorCode;
        private Boolean isActive;
        private Integer displayOrder;
        private Long schemeCount;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCategoryRequest {
        @NotBlank(message = "Name is required")
        private String name;
        private String description;
        private String iconUrl;
        private String colorCode;
        private Integer displayOrder;
    }
}
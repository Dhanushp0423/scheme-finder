package com.example.scheme.finder.dto;

import com.example.scheme.finder.entity.Scheme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SchemeDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemeResponse {
        private Long id;
        private String title;
        private String shortDescription;
        private String fullDescription;
        private CategoryDto.CategoryResponse category;
        private String ministryName;
        private Scheme.SchemeLevel schemeLevel;
        private String stateName;
        private LocalDate launchDate;
        private LocalDate endDate;
        private String officialWebsite;
        private String applicationUrl;
        private String helplineNumber;
        private Scheme.SchemeStatus status;
        private Integer minAge;
        private Integer maxAge;
        private Scheme.GenderEligibility genderEligibility;
        private String eligibleCategories;
        private Double maxAnnualIncome;
        private Boolean isForDisabled;
        private Boolean isForStudents;
        private Boolean isForEmployed;
        private String eligibleStates;
        private String benefitType;
        private String benefitAmount;
        private String benefits;
        private String documentsRequired;
        private String applicationProcess;
        private String tags;
        private Long viewCount;
        private Boolean isFeatured;
        private List<FaqResponse> faqs;
        private LocalDateTime createdAt;
        private Boolean isSaved; // For authenticated users
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemeSummary {
        private Long id;
        private String title;
        private String shortDescription;
        private CategoryDto.CategoryResponse category;
        private String ministryName;
        private Scheme.SchemeLevel schemeLevel;
        private String stateName;
        private Scheme.SchemeStatus status;
        private String benefitType;
        private String benefitAmount;
        private Long viewCount;
        private Boolean isFeatured;
        private Boolean isSaved;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSchemeRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Short description is required")
        private String shortDescription;

        private String fullDescription;

        @NotNull(message = "Category ID is required")
        private Long categoryId;

        private String ministryName;
        private Scheme.SchemeLevel schemeLevel;
        private String stateName;
        private LocalDate launchDate;
        private LocalDate endDate;
        private String officialWebsite;
        private String applicationUrl;
        private String helplineNumber;
        private Scheme.SchemeStatus status;
        private Integer minAge;
        private Integer maxAge;
        private Scheme.GenderEligibility genderEligibility;
        private String eligibleCategories;
        private Double maxAnnualIncome;
        private Boolean isForDisabled;
        private Boolean isForStudents;
        private Boolean isForEmployed;
        private String eligibleStates;
        private String benefitType;
        private String benefitAmount;
        private String benefits;
        private String documentsRequired;
        private String applicationProcess;
        private String tags;
        private Boolean isFeatured;
        private List<FaqRequest> faqs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaqRequest {
        private String question;
        private String answer;
        private Integer displayOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaqResponse {
        private Long id;
        private String question;
        private String answer;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EligibilityFilterRequest {
        private Integer age;
        private String gender;
        private String category;
        private Double annualIncome;
        private Boolean isDisabled;
        private Boolean isStudent;
        private Boolean isEmployed;
        private String state;
        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemeStats {
        private Long totalSchemes;
        private Long activeSchemes;
        private Long centralSchemes;
        private Long stateSchemes;
        private Long totalUsers;
        private Long totalApplications;
    }
}

package com.example.scheme.finder.dto;

import com.example.scheme.finder.entity.SchemeApplication;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ApplicationDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationResponse {
        private Long id;
        private Long userId;
        private String userName;
        private SchemeDto.SchemeSummary scheme;
        private SchemeApplication.ApplicationStatus status;
        private String notes;
        private String referenceNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateApplicationRequest {
        @NotNull(message = "Scheme ID is required")
        private Long schemeId;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateApplicationStatusRequest {
        private SchemeApplication.ApplicationStatus status;
        private String notes;
    }
}

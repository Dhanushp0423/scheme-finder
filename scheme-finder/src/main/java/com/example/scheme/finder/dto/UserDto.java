package com.example.scheme.finder.dto;

import com.example.scheme.finder.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String fullName;
        private String email;
        private String mobileNumber;
        private String state;
        private String district;
        private Integer age;
        private User.Gender gender;
        private String category;
        private Boolean isDisabled;
        private Boolean isStudent;
        private Boolean isEmployed;
        private Double annualIncome;
        private User.Role role;
        private Boolean isActive;
        private Boolean emailVerified;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        private String fullName;
        private String mobileNumber;
        private String state;
        private String district;
        private Integer age;
        private User.Gender gender;
        private String category;
        private Boolean isDisabled;
        private Boolean isStudent;
        private Boolean isEmployed;
        private Double annualIncome;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;
    }
}
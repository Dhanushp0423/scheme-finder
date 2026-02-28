package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.UserDto;

public interface UserService {
    UserDto.UserResponse getProfile(Long userId);
    UserDto.UserResponse updateProfile(Long userId, UserDto.UpdateProfileRequest request);
    void changePassword(Long userId, UserDto.ChangePasswordRequest request);
    PagedResponse<UserDto.UserResponse> getAllUsers(org.springframework.data.domain.Pageable pageable);
    void deactivateUser(Long userId);
}

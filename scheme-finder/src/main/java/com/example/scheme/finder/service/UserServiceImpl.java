package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.UserDto;
import com.example.scheme.finder.entity.User;
import com.example.scheme.finder.exception.BadRequestException;
import com.example.scheme.finder.exception.ResourceNotFoundException;
import com.example.scheme.finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto.UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserDto.UserResponse updateProfile(Long userId, UserDto.UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getMobileNumber() != null) user.setMobileNumber(request.getMobileNumber());
        if (request.getState() != null) user.setState(request.getState());
        if (request.getDistrict() != null) user.setDistrict(request.getDistrict());
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getCategory() != null) user.setCategory(request.getCategory());
        if (request.getIsDisabled() != null) user.setIsDisabled(request.getIsDisabled());
        if (request.getIsStudent() != null) user.setIsStudent(request.getIsStudent());
        if (request.getIsEmployed() != null) user.setIsEmployed(request.getIsEmployed());
        if (request.getAnnualIncome() != null) user.setAnnualIncome(request.getAnnualIncome());

        return mapToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, UserDto.ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public PagedResponse<UserDto.UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return PagedResponse.of(users.map(this::mapToResponse));
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    private UserDto.UserResponse mapToResponse(User user) {
        return UserDto.UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .state(user.getState())
                .district(user.getDistrict())
                .age(user.getAge())
                .gender(user.getGender())
                .category(user.getCategory())
                .isDisabled(user.getIsDisabled())
                .isStudent(user.getIsStudent())
                .isEmployed(user.getIsEmployed())
                .annualIncome(user.getAnnualIncome())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
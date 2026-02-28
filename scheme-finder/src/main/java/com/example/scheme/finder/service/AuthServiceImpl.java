package com.example.scheme.finder.service;


import com.example.scheme.finder.dto.AuthDto;
import com.example.scheme.finder.dto.UserDto;
import com.example.scheme.finder.entity.RefreshToken;
import com.example.scheme.finder.entity.User;
import com.example.scheme.finder.exception.BadRequestException;
import com.example.scheme.finder.exception.UnauthorizedException;
import com.example.scheme.finder.repository.RefreshTokenRepository;
import com.example.scheme.finder.repository.UserRepository;
import com.example.scheme.finder.security.JwtUtil;
import com.example.scheme.finder.security.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .state(request.getState())
                .age(request.getAge())
                .gender(request.getGender())
                .category(request.getCategory())
                .isDisabled(request.getIsDisabled() != null ? request.getIsDisabled() : false)
                .isStudent(request.getIsStudent() != null ? request.getIsStudent() : false)
                .isEmployed(request.getIsEmployed() != null ? request.getIsEmployed() : false)
                .annualIncome(request.getAnnualIncome())
                .role(User.Role.USER)
                .isActive(true)
                .emailVerified(false)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        UserPrincipal userPrincipal = new UserPrincipal(user);
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = createRefreshToken(user);

        return AuthDto.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        UserPrincipal userPrincipal = new UserPrincipal(user);
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String refreshToken = createRefreshToken(user);

        return AuthDto.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthDto.AuthResponse refreshToken(AuthDto.RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token has expired. Please login again.");
        }

        User user = refreshToken.getUser();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String accessToken = jwtUtil.generateToken(userPrincipal);
        String newRefreshToken = createRefreshToken(user);

        return AuthDto.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    @Transactional
    public void logout(User user) {
        refreshTokenRepository.deleteByUser(user);
        entityManager.flush();
    }

    private String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        entityManager.flush();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    private UserDto.UserResponse mapToUserResponse(User user) {
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
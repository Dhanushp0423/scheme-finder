package com.example.scheme.finder.service;


import com.example.scheme.finder.dto.AuthDto;
import com.example.scheme.finder.entity.User;

public interface AuthService {
    AuthDto.AuthResponse register(AuthDto.RegisterRequest request);
    AuthDto.AuthResponse login(AuthDto.LoginRequest request);
    AuthDto.AuthResponse refreshToken(AuthDto.RefreshTokenRequest request);
    void logout(User user);
}
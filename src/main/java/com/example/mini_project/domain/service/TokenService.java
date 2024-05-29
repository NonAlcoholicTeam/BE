package com.example.mini_project.domain.service;

import com.example.mini_project.domain.entity.User;

public interface TokenService {
    void createToken(String username, String accessToken, String refreshToken);
}

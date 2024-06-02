package com.example.mini_project.global.auth.service;

import com.example.mini_project.domain.entity.User;

public interface TokenService {
    void updateAccessToken(User user, String newAccessToken);
}

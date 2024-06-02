package com.example.mini_project.global.auth.service;

import com.example.mini_project.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface TokenService {
    @Transactional
    void updateAccessToken(User user, String newAccessToken);
}

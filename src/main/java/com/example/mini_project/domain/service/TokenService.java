package com.example.mini_project.domain.service;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.domain.entity.UserRoleEnum;
import com.example.mini_project.global.dto.ApiMessageDto;
import org.springframework.http.ResponseEntity;

public interface TokenService {
    void createToken(String username, String accessToken, String refreshToken);

    ResponseEntity<ApiMessageDto> deleteToken(String email);

    ResponseEntity<ApiMessageDto> updateToken(String email, UserRoleEnum role, String refreshToken);
}

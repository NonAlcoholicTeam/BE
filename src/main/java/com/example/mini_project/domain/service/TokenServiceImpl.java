package com.example.mini_project.domain.service;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.domain.repository.TokenRepository;
import com.example.mini_project.domain.repository.UserRepository;
import com.example.mini_project.global.auth.entity.Token;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void createToken(String username, String accessToken, String refreshToken) {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 이메일 처리입니다. 가입된 이메일인지 확인해주세요.");
        }

        Token token = new Token(userOptional.get(), accessToken, refreshToken);
        tokenRepository.save(token);
    }
}

package com.example.mini_project.global.auth.service;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.global.auth.entity.Token;
import com.example.mini_project.global.auth.repository.TokenRepository;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void updateAccessToken(User user, String newAccessToken) {
        Optional<Token> tokenOptional = tokenRepository.findByUser(user);

        if (tokenOptional.isEmpty()) {
            throw new ResourceNotFoundException("비정상적인 토큰 정보. 재확인 바람.");
        }

        Token tokenObj = tokenOptional.get();
        tokenObj.update(newAccessToken);
        tokenRepository.save(tokenObj);
    }
}

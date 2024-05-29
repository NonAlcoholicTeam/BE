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

    /**
     * 로그인에 따른 email, 엑세스 토큰, 리프레쉬 토큰 기반 토큰엔티티 생성
     * @param username
     * @param accessToken
     * @param refreshToken
     */
    @Override
    public void createToken(String username, String accessToken, String refreshToken) {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 이메일 처리입니다. 가입된 이메일인지 확인해주세요.");
        }

        Token token = new Token(userOptional.get(), accessToken, refreshToken);
        tokenRepository.save(token);
    }

    @Override
    public String deleteToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 이메일 처리입니다. 가입된 이메일인지 확인해주세요.");
        }

        tokenRepository.deleteByUser(userOptional.get());

        return "성공적으로 로그아웃됐습니다.";
    }
}

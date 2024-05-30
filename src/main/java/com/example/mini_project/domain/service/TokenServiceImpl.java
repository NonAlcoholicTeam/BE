package com.example.mini_project.domain.service;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.domain.entity.UserRoleEnum;
import com.example.mini_project.domain.repository.TokenRepository;
import com.example.mini_project.domain.repository.UserRepository;
import com.example.mini_project.global.auth.entity.Token;
import com.example.mini_project.global.auth.entity.TokenType;
import com.example.mini_project.global.auth.jwt.JwtUtil;
import com.example.mini_project.global.dto.ApiMessageDto;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

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

    /**
     * 로그아웃을 위해서 토큰 정보들 전부 삭제하는 메소드
     * @param email
     * @return
     */
    @Override
    public ResponseEntity<ApiMessageDto> deleteToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 이메일 처리입니다. 가입된 이메일인지 확인해주세요.");
        }

        tokenRepository.deleteByUser(userOptional.get());

        ApiMessageDto messageDto = new ApiMessageDto(HttpStatus.OK.value(), "성공적으로 로그아웃됐습니다.");
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiMessageDto> updateToken(String email, UserRoleEnum role, String refreshToken) {

        // 리프레쉬 토큰 우선 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResourceNotFoundException("리프레쉬 토큰이 만료됐습니다. 로그아웃 처리 요망");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 이메일 처리입니다. 가입된 이메일인지 확인해주세요.");
        }

        Optional<Token> tokenOptional = tokenRepository.findByUser(userOptional.get());

        if (tokenOptional.isEmpty()) {
            throw new ResourceNotFoundException("유효하지 않은 토큰 정보입니다. 토큰 정보를 확인해주세요.");
        }

        String accessToken = jwtUtil.createToken(jwtUtil.createTokenPayload(email, role, TokenType.ACCESS));
        tokenOptional.get().updateAccessToken(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);

        ApiMessageDto messageDto = new ApiMessageDto(HttpStatus.OK.value(), "액세스 토큰이 재발급됐습니다.");

        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);
    }
}

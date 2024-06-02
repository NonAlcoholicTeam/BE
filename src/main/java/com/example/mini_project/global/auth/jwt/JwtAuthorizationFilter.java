package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.entity.User;
import com.example.mini_project.domain.entity.UserDetailsServiceImpl;
import com.example.mini_project.domain.repository.UserRepository;
import com.example.mini_project.global.auth.entity.Token;
import com.example.mini_project.global.auth.entity.TokenType;
import com.example.mini_project.global.auth.repository.TokenRepository;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // 사용자가 있는지 확인
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        /**
         1. 토큰의 타입부터 확인한다
         2-1. 엑세스토큰이 확인됐다
         2-2. 유효성 판별 후 필터단 넘긴다
         2-3. 만약 만료된 토큰?
         3-1. 리프레쉬토큰이 확인됐다
         3-2. 유효한 리프레쉬토큰이면 리스폰 헤더에 새로 발급한 엑세스토큰 담기
         3-3. 만료된 리프레쉬토큰이면 그냥 예외 반환
         */

        String accessToken = jwtUtil.getAccessTokenFromRequestCookie(request);
        String refreshToken = jwtUtil.getRefreshTokenFromRequestCookie(request);

        // 우선 리프레쉬토큰인지부터 확인하기
        if (StringUtils.hasText(refreshToken)) {
            refreshToken = jwtUtil.substringToken(refreshToken);
            log.info("리프레쉬토큰: " + refreshToken);

            // 날짜 만료로 인한 로그아웃 처리가 요청될듯
            if (!jwtUtil.validateToken(refreshToken)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
            String email = info.getSubject();

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("비정상적인 이메일 정보. 재확인 바람.");
            }

            User user = userOptional.get();
            Optional<Token> tokenOptional = tokenRepository.findByUser(user);

            // 데이터베이스에 저장되어있는지 확인하기
            if (tokenOptional.isEmpty()) {
                throw new ResourceNotFoundException("비정상적인 토큰 정보. 재확인 바람.");
            }

            Token tokenObj = tokenOptional.get();

            // 데이터베이스의 내용과 일치하는지 확인하기
            if (!tokenObj.getRefreshToken().equals(refreshToken)) {
                throw new ResourceNotFoundException("일치하는 토큰 정보 찾을 수 없음. 재확인 바람.");
            }

            String newAccessToken = jwtUtil.createAccessToken(
                    jwtUtil.createTokenPayload(user.getEmail(), user.getRole(), TokenType.ACCESS));
            log.info("새로운 엑세스토큰: " + newAccessToken);
            // 새로운 엑세스토큰 업데이트
            tokenObj.update(newAccessToken);

            try {
                // username 담아주기
                setAuthentication(email);
                // 리스폰스에 새로운 엑세스토큰 담기
                response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, newAccessToken);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 리프레쉬토큰이 아니면 액세스토큰 검증
        if (StringUtils.hasText(accessToken)) {
            accessToken = jwtUtil.substringToken(accessToken);
            log.info("엑세스토큰: " + accessToken);

            // 날짜 만료로 인한 리프레쉬토큰 요청이 포함되는 부분
            if (!jwtUtil.validateToken(accessToken)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
            String email = info.getSubject();

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("비정상적인 이메일 정보. 재확인 바람.");
            }

            User user = userOptional.get();
            Optional<Token> tokenOptional = tokenRepository.findByUser(user);

            // 데이터베이스에 저장되어있는지 확인하기
            if (tokenOptional.isEmpty()) {
                throw new ResourceNotFoundException("비정상적인 토큰 정보. 재확인 바람.");
            }

            Token tokenObj = tokenOptional.get();

            // 데이터베이스의 내용과 일치하는지 확인하기
            if (!tokenObj.getAccessToken().equals(accessToken)) {
                throw new ResourceNotFoundException("일치하는 토큰 정보 찾을 수 없음. 재확인 바람.");
            }

            try {
                // username 담아주기
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }



        // 현재는 쿠키로부터 갖고오지만, 만약 헤더로부터 갖고온다면 맞춰 수정해야겠지
//        String token = jwtUtil.getTokenFromRequestCookie(request);
//
//        if (StringUtils.hasText(token)) {
//            token = jwtUtil.substringToken(token);
//            log.info(token);
//
//            // 날짜 만료로 인한 리프레쉬토큰 요청이 포함되는 부분
//            if (!jwtUtil.validateToken(token)) {
//                log.error("Token Error");
//                return;
//            }
//
//            Claims info = jwtUtil.getUserInfoFromToken(token);
//
//            try {
//                // username 담아주기
//                setAuthentication(info.getSubject());
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                return;
//            }
//        }

        // 다음 필터로 넘어가라는 의미
        // 이걸 이용해서 리프레쉬 토큰에 대한 로직 짜면 될 것 같은데
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) { // Authentication 인증 객체 만듦
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // UPAT 생성
    }
}

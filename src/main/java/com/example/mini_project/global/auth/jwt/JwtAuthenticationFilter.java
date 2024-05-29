package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.dto.UserLoginRequestDto;
import com.example.mini_project.domain.dto.UserLoginResponseDto;
import com.example.mini_project.domain.entity.UserDetailsImpl;
import com.example.mini_project.domain.entity.UserRoleEnum;
import com.example.mini_project.domain.service.TokenService;
import com.example.mini_project.global.auth.entity.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인 및 JWT 생성 + 인증")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil; // 로그인 성공 시, 존맛탱 발급을 위한 의존성 주입

    private final TokenService tokenService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        setFilterProcessesUrl("/mini/user/login"); // 로그인 처리 경로 설정(매우매우 중요)
        super.setUsernameParameter("email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그인 시도를 담당함
        log.info("로그인 단계 진입");

        try {
            UserLoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDto.class);
            return getAuthenticationManager().authenticate( // 인증 처리 하는 메소드
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // 기존 버전
//        String token = jwtUtil.createToken(username, role);
//        jwtUtil.addJwtToCookie(token, response);

        // 신 버전
        String accessToken = jwtUtil.createToken(jwtUtil.createTokenPayload(username, role, TokenType.ACCESS));
        String refreshToken = jwtUtil.createToken(jwtUtil.createTokenPayload(username, role, TokenType.REFRESH));

        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);
        jwtUtil.addJwtToCookie(refreshToken, response);

        tokenService.createToken(username, accessToken, refreshToken);

//        response.setStatus(HttpStatus.OK.value());
//        response.setCharacterEncoding("UTF-8"); // 클라이언트 전달 메시지 인코딩
//        response.getWriter().write("로그인 성공 및 토큰이 생성됐습니다");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(new UserLoginResponseDto("로그인 성공 및 토큰 발급"));
        PrintWriter writer = response.getWriter();

        writer.print(jsonMessage);
        writer.flush();
        writer.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패 및 401 에러 반환");

        // 로그인 실패로 상태코드 반환
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setCharacterEncoding("UTF-8"); // 클라이언트 전달 메시지 인코딩
//        response.getWriter().write("로그인에 실패했습니다");
        response.setContentType("application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(new UserLoginResponseDto("로그인 실패"));
        PrintWriter writer = response.getWriter();

        writer.print(jsonMessage);
        writer.flush();
        writer.close();
    }
}

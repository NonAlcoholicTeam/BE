package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.entity.UserDetailsServiceImpl;
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

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // 사용자가 있는지 확인

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        /**
         1. 토큰의 타입부터 확인한다
         2-1. 엑세스토큰이 확인됐다
         2-2. 유효성 판별 후 필터단 넘긴다
         */

        // 현재는 쿠키로부터 갖고오지만, 만약 헤더로부터 갖고온다면 맞춰 수정해야겠지
        String token = jwtUtil.getTokenFromRequestCookie(request);

        if (StringUtils.hasText(token)) {
            token = jwtUtil.substringToken(token);
            log.info(token);

            // 날짜 만료로 인한 리프레쉬토큰 요청
            if (!jwtUtil.validateToken(token)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);

            try {
                // username 담아주기
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

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

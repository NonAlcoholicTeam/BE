package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.entity.UserRoleEnum;
import com.example.mini_project.global.auth.entity.TokenType;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class TokenPayload {
    private String sub;
    private String jti;
    private String role;
    private Date iat;
    private Date expiresAt;
    private TokenType tokenType;

    public TokenPayload(String sub, String jti, Date iat, Date expiresAt, UserRoleEnum role, TokenType tokenType) {
        this.sub = sub;
        this.jti = jti;
        this.role = role.getRole();
        this.iat = iat;
        this.expiresAt = expiresAt;
        this.tokenType = tokenType;
    }
}

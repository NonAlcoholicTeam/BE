package com.example.mini_project.entity;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    MANAGER(Role.ADMIN),
    STAFF(Role.USER);

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    public static class Role {
        // 권한 이름 규칙은 "ROLE_"로 시작해야 함
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}

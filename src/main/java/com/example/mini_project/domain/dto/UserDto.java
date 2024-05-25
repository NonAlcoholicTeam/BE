package com.example.mini_project.domain.dto;

import com.example.mini_project.domain.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String adminNumber;
    private String role;

    public UserDto(String username, String email, UserRoleEnum role) {
        this.username = username;
        this.email = email;
        this.role = role.getRole();
    }
}

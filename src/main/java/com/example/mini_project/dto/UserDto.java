package com.example.mini_project.dto;

import com.example.mini_project.entity.User;
import com.example.mini_project.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String adminNumber;
    private String role;

    public UserDto(Long id, String username, String email, UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role.getRole();
    }
}

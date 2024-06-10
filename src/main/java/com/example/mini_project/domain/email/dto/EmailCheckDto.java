package com.example.mini_project.domain.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailCheckDto {
    @Email
    @NotEmpty(message = "입력했던 이메일 확인")
    private String email;

    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authNumber;
}

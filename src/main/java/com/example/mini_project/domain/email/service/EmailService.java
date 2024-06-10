package com.example.mini_project.domain.email.service;

import com.example.mini_project.domain.email.dto.EmailCheckDto;
import com.example.mini_project.domain.email.dto.EmailRequestDto;

public interface EmailService {

    Boolean isEmailAlreadyExisted(String email);

    String joinEmail(EmailRequestDto requestDto);

    void mailSend(String setFrom, String toMail, String title, String content);

    boolean checkAuthNum(EmailCheckDto checkDto);
}

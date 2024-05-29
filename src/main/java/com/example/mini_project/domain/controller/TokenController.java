package com.example.mini_project.domain.controller;


import com.example.mini_project.domain.entity.UserDetailsImpl;
import com.example.mini_project.domain.service.TokenService;
import com.example.mini_project.global.dto.ApiMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token 컨트롤러", description = "회원 기능 중 토큰 관련하여 담당하는 컨트롤러")
@AllArgsConstructor
@RestController
@RequestMapping("/mini/user")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 위한 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "비정상적인 로그아웃 시도",
                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class)))
    })
    @PostMapping("/logout")
    // 쿠키 네임에 Authorization를 붙여서 보내야 함
    public ResponseEntity<ApiMessageDto> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String message = tokenService.deleteToken(userDetails.getUsername());
        ApiMessageDto messageDto = new ApiMessageDto(HttpStatus.OK.value(), message);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}

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
import org.springframework.web.bind.annotation.*;

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
        return tokenService.deleteToken(userDetails.getUsername());
    }

//
//    @Operation(summary = "엑세스 토큰 업데이트", description = "토큰 만료 시, 엑세스 토큰 재발급")
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "엑세스 토큰 재발급 성공",
//                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class))),
//            @ApiResponse(
//                    responseCode = "401",
//                    description = "비정상적인 엑세스 토큰 재발급 시도",
//                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class)))
//    })
//    @PutMapping("/accessToken")
//    public ResponseEntity<ApiMessageDto> updateAccessToken(
//            @RequestHeader("Authorization") String refreshToken,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return tokenService.updateToken(userDetails.getUsername(), userDetails.getUser().getRole(), refreshToken);
//    }
}

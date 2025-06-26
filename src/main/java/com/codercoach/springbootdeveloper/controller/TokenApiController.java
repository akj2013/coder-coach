package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.dto.CreateAccessTokenRequest;
import com.codercoach.springbootdeveloper.dto.CreateAccessTokenResponse;
import com.codercoach.springbootdeveloper.service.RefreshTokenService;
import com.codercoach.springbootdeveloper.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 🔸 [A] JWT 인증 요청 흐름 (/api/**)
 * 1. 사용자가 /api/** 요청 → Authorization: Bearer <access_token>
 * 2. TokenAuthenticationFilter 실행
 *    ├─ Authorization 헤더에서 토큰 추출
 *    ├─ TokenProvider.validToken()으로 검증
 *    ├─ TokenProvider.getAuthentication()으로 인증 객체 생성
 *    └─ SecurityContextHolder.setAuthentication(authentication)
 * 3. 이후 컨트롤러 → 인증된 사용자로 접근 가능
 *
 * 🔸 [B] OAuth2 로그인 흐름
 * 1. 사용자가 /login → /oauth2/authorization/google 진입
 * 2. OAuth2AuthorizationRequestBasedOnCookieRepository
 *    └─ 요청 상태를 쿠키에 저장 (state, redirect_uri 등)
 * 3. 구글 로그인 성공 → Spring Security가 Authentication 생성
 *    └─ 자동으로 SecurityContextHolder.setAuthentication(...) 실행
 * 4. OAuth2SuccessHandler 실행
 *    ├─ OAuth2User에서 사용자 이메일 추출
 *    ├─ User 조회 및 액세스/리프레시 토큰 발급
 *    ├─ 리프레시 토큰 → DB 저장, 쿠키 저장
 *    └─ 액세스 토큰 포함하여 /articles?token=... 으로 리디렉트
 */
@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/api/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
            @RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }

    // 리프레시 토큰 삭제
    @DeleteMapping(value = "/api/refresh-token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> deleteRefreshToken() {
        refreshTokenService.delete();
        return ResponseEntity.ok()
                .build();
    }
}

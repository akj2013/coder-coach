package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.config.jwt.TokenProvider;
import com.codercoach.springbootdeveloper.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 리프레시 토큰으로 새로운 엑세스 토큰을 만든다.
    public String createNewAccessToken(String refreshToken) {
        // 리프레시 토큰이 유효하지 않은 경우
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다.");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);
        return tokenProvider.generateToken(user, Duration.ofDays(2));
    }
}

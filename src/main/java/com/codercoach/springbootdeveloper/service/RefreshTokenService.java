package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.config.jwt.TokenProvider;
import com.codercoach.springbootdeveloper.domain.RefreshToken;
import com.codercoach.springbootdeveloper.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenProvider tokenProvider;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("refreshToken에 해당하는 값이 없습니다."));
    }

    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);
        refreshTokenRepository.deleteByUserId(userId);
    }
}

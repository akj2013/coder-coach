package com.codercoach.springbootdeveloper.config.oauth;

import com.codercoach.springbootdeveloper.config.jwt.TokenProvider;
import com.codercoach.springbootdeveloper.domain.RefreshToken;
import com.codercoach.springbootdeveloper.domain.User;
import com.codercoach.springbootdeveloper.repository.RefreshTokenRepository;
import com.codercoach.springbootdeveloper.service.UserService;
import com.codercoach.springbootdeveloper.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

/**
 * OAuth2 인증 성공 후 토큰 발급 및 리디렉션 처리 담당
 *
 * 스프링 시큐리티의 기본 로직에서는 별도의 authenticationSuccessHandler를 지정하지 않으면
 * 로그인 성공 이후 SimpleUrlAuthenticationSuccessHandler를 사용합니다.
 * 일반적인 로직은 동일하게 사용하고, 토큰과 관련된 작업만 추가로 처리하기 위해 SimpleUrlAuthenticationSuccessHandler를
 * 상속받은 뒤에 onAuthenticationSuccess() 메서드를 오버라이드합니다.
 *
 *
 * 📌 역할:
 * OAuth2 로그인 성공 후 실행
 *
 * 인증된 사용자 정보로부터:
 *
 * 리프레시 토큰 생성 → DB 저장 → 쿠키 저장
 *
 * 액세스 토큰 생성 → URL에 포함시켜 리디렉트
 *
 * 인증 관련 쿠키 제거 (cleanup)
 *
 * ✅ 실행 시점:
 * OAuth2 인증 성공 직후
 *
 * oauth2Login().successHandler(...)에 의해 등록됨
 */
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/index";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository auth2AuthorizationRequestBasedOnCookieRepository;
    private final UserService userService;

    /* ✅ 중요한 정리
    🔹 onAuthenticationSuccess(...) 메서드의 Authentication 파라미터는 어디서 온 것인가?
        이미 OAuth2 로그인 인증 절차를 통해 만들어진 인증 객체입니다.
        Spring Security 내부에서, 정확히는 OAuth2LoginAuthenticationFilter에서 생성 후 자동으로 SecurityContext에 저장됩니다.
        따라서 여기서 다시 SecurityContextHolder.getContext().setAuthentication(...)을 호출할 필요가 없습니다.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 액세스 토큰 생성 -> 패스에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        // HTTP 응답으로 302 상태와 Location 헤더를 보냄
        // 브라우저가 해당 URL로 자동 이동, /articles 페이지에 로드된 HTML 내부에서 token.js가 실행됨
        // token.js가 URL에서 token 파라미터 추출 → localStorage에 저장
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    // 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        auth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }


}

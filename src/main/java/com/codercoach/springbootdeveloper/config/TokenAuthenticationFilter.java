package com.codercoach.springbootdeveloper.config;

import com.codercoach.springbootdeveloper.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰 필터 (사용자 정의 필터)
 * 액세스 토큰값이 담긴 Authorization 헤더값을 가져온 뒤 엑세스 토큰이 유효하다면 인증 정보를 설정합니다.
 * OncePerRequestFilter를 상속받았기 때문에, 이 메서드는 모든 요청당 한 번만 실행됩니다.
 *
 * 이 필터는 Spring Security 필터 체인에 자동으로 등록되지 않습니다
 * @Component를 붙여도 Security FilterChain이 인식하는 필터 체인에는 들어가지 않음
 * 반드시 http.addFilterBefore(...) 또는 addFilterAfter(...)로 명시적 등록과 순서 지정이 필요합니다
 * WebSecurityConfig 클래스의 filterChain 메소드에 명시적으로 설정해야 합니다.
 * .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
 *                              UsernamePasswordAuthenticationFilter.class);
 *
 * 📌 역할:
 * 요청에 포함된 JWT 액세스 토큰 추출 (Authorization: Bearer <token>)
 *
 * 토큰이 유효하면 Authentication 생성 후 SecurityContextHolder에 등록
 *
 * 이후 컨트롤러, 서비스에서 인증된 사용자로 간주됨
 *
 * ✅ 실행 시점:
 * 매 요청마다 실행
 * → Spring Security 필터 체인에서 UsernamePasswordAuthenticationFilter 앞에 등록됨
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    /*
    클라이언트 요청
       ↓
    TokenAuthenticationFilter
       ├─ Authorization 헤더 추출
       ├─ "Bearer " 제거
       ├─ JWT 유효성 검사
       ├─ 인증 객체 생성
       └─ SecurityContextHolder에 등록
       ↓
    다음 필터 or 컨트롤러로 요청 전달
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // 클라이언트 요청 객체
            HttpServletResponse response, // 서버의 응답 객체
            FilterChain filterChain) throws ServletException, IOException { // 다음 필터로 요청을 넘기기 위한 체인
        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

        // 가져온 토큰이 유효한지 확인하고, 유효한 때에는 인증 정보 설정
        if (tokenProvider.validToken(token)) {
            // Authentication은 현재 사용자의 인증 정보를 나타냄 (username, 권한, 비밀번호 등)
            Authentication authentication = tokenProvider.getAuthentication(token);
            /*
            이 정보를 Spring Security의 보안 컨텍스트에 저장하면,
            이후 컨트롤러나 서비스에서 @AuthenticationPrincipal,
            SecurityContextHolder.getContext().getAuthentication() 등을 통해 사용자 정보 접근 가능
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 현재 필터에서 요청 처리가 끝났으니, 다음 필터 혹은 최종 목적지(컨트롤러)로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length()); // "Bearer " 제거
        }
        return null;
    }
}

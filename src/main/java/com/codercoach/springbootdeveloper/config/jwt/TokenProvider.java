package com.codercoach.springbootdeveloper.config.jwt;

import com.codercoach.springbootdeveloper.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * 토큰 생성 클래스
 *  토큰을 생성하고
 *  올바른 토큰인지 유효성 검사를 하고
 *  토큰에서 필요한 정보를 가져온다.
 *
 * 📌 역할:
 * JWT 액세스 토큰 / 리프레시 토큰 생성
 *
 * JWT 파싱 → 사용자 ID 추출
 *
 * 유효성 검사
 *
 * JWT → Authentication 객체 생성
 *
 * ✅ 실행 시점:
 * TokenAuthenticationFilter에서 토큰 유효성 검사 및 인증 객체 생성 시 사용
 *
 * OAuth2SuccessHandler에서 토큰 발급 시 사용
 */

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메소드
    // User는 Domain의 유저
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                .setIssuer(jwtProperties.getIssuer()) // 내용 iss (application.yml에서 설정한 프로퍼티 값)
                .setIssuedAt(now) // 내용 iat : 현재시간
                .setExpiration(expiry) // 내용 exp : expiry 멤버 변수값
                .setSubject(user.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", user.getId()) // 클레임 ID : 유저 ID
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 : 비밀값과 해시값
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }

    // 클레임 조회
    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 기반으로 유저 ID를 가져오는 메소드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
}

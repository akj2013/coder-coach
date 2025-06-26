package com.codercoach.springbootdeveloper.config.jwt;

import com.codercoach.springbootdeveloper.SpringBootDeveloperApplication;
import com.codercoach.springbootdeveloper.domain.User;
import com.codercoach.springbootdeveloper.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SpringBootDeveloperApplication.class) // 테스트용 애플리케이션 컨텍스트
class TokenProviderTest { // 자바에서 public 키워드를 생략하면 기본 접근 제어자(default access modifier), 즉 package-private이 됩니다.
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    // generateToken() 검증 테스트
    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // GIVEN 토큰에 유저 정보를 추가하기 위한 테스트 유저를 만듭니다.
        User user = userRepository.save(User.builder()
                .email("user@email.com")
                .password("test")
                .build());

        // WHEN 토큰 제공자의 generateToken() 메소드를 호출해 토큰을 만듭니다.
        String token = tokenProvider.generateToken(user, Duration.ofDays(14));

        // THEN jjwt 라이브러리를 사용해 토큰을 복호화합니다. 토큰을 만들 때 클레임으로 넣어둔 id값이 given절에서 만든 값과 일치하는지.
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(user.getId());
    }

    // validToken() 검증 테스트
    @DisplayName("validToken() : 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // GIVEN : jjwt 라이브러리를 사용해 토큰을 생성합니다.
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // WHEN : 유효한 토큰인지 유효성 검사를 실시합니다.
        boolean result = tokenProvider.validToken(token);

        // THEN : 반환값을 확인합니다.
        assertThat(result).isFalse();
    }

    // validToken() 검증 테스트
    @DisplayName("validToken() : 만료된 토큰인 때에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // GIVEN : jjwt 라이브러리를 사용해 토큰을 생성합니다.
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // WHEN : 유효한 토큰인지 유효성 검사를 실시합니다.
        boolean result = tokenProvider.validToken(token);

        // THEN : 반환값을 확인합니다.
        assertThat(result).isTrue();
    }

    // getAuthentication() 검증 테스트
    @DisplayName("getAuthentication() : 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // GIVEN : JJWT 라이브러리를 사용해 토큰을 생성합니다. 토큰의 제목인 subject는 이메일 값으로 합니다.
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // WHEN 인증 객체를 반환받습니다.
        Authentication authentication = tokenProvider.getAuthentication(token);

        // THEN 반환받은 인증 객체의 유저 이름을 가져와 subject값과 같은지 확인합니다.
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    // getUserId() 검증 테스트
    @DisplayName("getUserId() : 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // GIVEN
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // WHEN
        Long userIdByToken = tokenProvider.getUserId(token);

        // THEN
        assertThat(userIdByToken).isEqualTo(userId);
    }
}

package com.codercoach.springbootdeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 관련 설정 클래스
 * application.yml 파일의 jwt에서 설정한 issuer와 secretKey가 매핑된다.
 */
@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 프로퍼티값을 가져와서 사용하는 어노테이션
public class JwtProperties {
    private String issuer;
    // Spring Boot의 @ConfigurationProperties는 스네이크 케이스 (secret_key)를 카멜 케이스 (secretKey)로 자동 변환해줍니다.
    private String secretKey;
}

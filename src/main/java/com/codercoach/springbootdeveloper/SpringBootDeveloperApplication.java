package com.codercoach.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // created_at, updated_at 자동 업데이트
@SpringBootApplication // 스프링 부트 관련 설정
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        // 에러 리포트
        // Process 'command 'C:\Users\akjak\.jdks\openjdk-24.0.1\bin\java.exe'' finished with non-zero exit value 1
        // 설정 -> 빌드 -> 그레이들에서 빌드 및 실행을 Gradle(default)에서 IntelliJ IDEA로 변경

        // run(메인 클래스로 사용할 클래스, 커맨드 라인 인수들)
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}

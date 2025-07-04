# Spring Boot 서버 포트 설정 및 로컬/배포 환경 분리 가이드

## 🧩 문제 요약
- Elastic Beanstalk 배포 후 502 오류 발생 → 원인은 Spring Boot 기본 포트 8080 사용 때문
- Elastic Beanstalk는 5000번 포트를 통해 애플리케이션에 프록시 연결
- 로컬에서는 8080 포트를 사용하고 싶음 → 포트 충돌 문제

---

## ✅ 해결 방법 요약

### 1. `application.yml`에 포트 지정 (배포용)
```yaml
server:
  port: 5000
```

### 2. 로컬에서 실행 시 포트 8080으로 실행하고 싶다면?
`application.yml`을 profile로 분리

### 🔧 application.yml (공통)
```yaml
spring:
  profiles:
    active: local
```

### 🔧 application-local.yml (로컬 실행용)
```yaml
server:
  port: 8080

spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    defer-datasource-initialization: true

  h2:
    console:
      enabled: true

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - email
              - profile

jwt:
  issuer: test@test.com
  secret_key: test-test
```

### 🔧 application-eb.yml (배포용)
```yaml
server:
  port: 5000
```

### 💡 IntelliJ 실행 시 VM options에 추가
```
-Dspring.profiles.active=local
```

---

## 🛠 임시 해결 방법
- 급할 경우 `application.yml`의 `server.port: 5000` 주석 처리
```yaml
# server:
#   port: 5000
```

그러면 기본 포트 8080으로 실행됨 → `localhost:8080` 접속 가능

---

📅 작성일: 2025-06-30 15:22:52

plugins {
    id("java")
    /**
     * 최초 생성은 그레이들 프로젝트로 생성했기에
     * 그레이들 설정 파일을 수정하여 스프링 부트 3 프로젝트로 수정한다.
     *
     * 책에서는
     * id 'org.springframework.boot' version '3.0.2'
     * id 'io.spring.dependency-management' version '1.1.0'
     * 이지만 형식이 다르다.
     */
    id("org.springframework.boot") version "3.0.2"          // 스프링 부트 플러그인
    id("io.spring.dependency-management") version "1.1.0"   // 스프링의 의존성을 자동 관리
}

group = "com.codercoach"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17            // 자바 버전 입력
}

repositories {
    mavenCentral()
}

dependencies {
    // Gradle/Dependencies/compileClasspath에서 확인가능
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Gradle/Dependencies/testCompileClasspath에서 확인가능
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // 인메모리 데이터베이스
    runtimeOnly("com.h2database:h2")
    // 컴파일 시에만 참조되며, 실행 시에는 포함되지 않음 → Lombok은 주로 소스코드 생성에만 사용되므로 compileOnly로 충분함.
    compileOnly("org.projectlombok:lombok")
    // 반복 메서드 작성 작업을 줄여주는 롬복
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.projectlombok:lombok")
    // 타임리프
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // 스프링 시큐리티를 사용하기 위한 스타터 추가
    implementation("org.springframework.boot:spring-boot-starter-security")
    // 타임리프에서 스프링 시큐리티를 사용하기 위한 의존성 추가
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    // 스프링 시큐리티를 테스트하기 위한 의존성 추가
    implementation("org.springframework.security:spring-security-test")
    // 자바 JWT 라이브러리
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    // XML 문서와 JAVA 객체 간 매핑 자동화
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    // OAuth2를 사용하기 위한 스타터 추가
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // PostgreSQL 드라이버 추가
    implementation("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}

// 플레인 JAR 생성 방지 설정 방법
tasks.getByName<Jar>("jar") {
    enabled = false
}
//package com.codercoach.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
///**
// * 로그인 User에 대한 실제 인증 처리를 하는 시큐리티 설정 파일
// */
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//
//    // 스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        /* 람다식
//            람다식은 "추상 메서드가 하나만 있는 인터페이스"에서만 사용 가능합니다.
//            이런 인터페이스를 함수형 인터페이스 (Functional Interface) 라고 부릅니다.
//
//            Java에서 람다식은 익명 클래스의 축약 문법인데,
//            람다식으로 어떤 메서드를 구현할지 컴파일러가 명확히 알아야 하므로,
//            하나의 추상 메서드만 있는 인터페이스에서만 사용 가능합니다.
//         */
//        return web -> web.ignoring()
//                .requestMatchers(toH2Console()) // h2-console 하위 url을 대상으로 ignoring() 메소드 사용
//                .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }
//
//
//    /* Spring Security 5.x부터 도입된 **람다 기반의 DSL 문법(도메인 특화 언어)**
//        auth -> auth... 부분은 customizer.customize()에 넘겨지는 람다
//        내부에서 auth는 AuthorizationManagerRequestMatcherRegistry 타입의 객체
//        이 안에서 .requestMatchers(), .permitAll() 등을 수행
//        람다가 끝나면 다시 HttpSecurity를 리턴 → 메서드 체이닝 가능
//
//        내부 설정을 "유연하고 확장 가능하게 하기 위해" 등장한 제네릭 타입 추론(Generic Type Inference)
//
//        즉, 지금까지 체이닝하며 설정해온 HttpSecurity 객체는 SecurityFilterChain 객체를 구성할 수 있는 정보를 가지고 있고,
//        .build()는 그 정보를 바탕으로 SecurityFilterChain 객체를 생성해 반환합니다.
//        즉, 형변환이 일어나는 게 아니라,
//        설정 정보들을 실제 실행 가능한 필터 체인(SecurityFilterChain 객체)으로 변환하는 단계입니다.
//     */
//    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests(auth -> auth         // 인증, 인가 설정
//                        .requestMatchers(                                       // 특정 요청과 일치하는 URL 액세스 설정
//                                new AntPathRequestMatcher("/login"),
//                                new AntPathRequestMatcher("/signup"),
//                                new AntPathRequestMatcher("/user")
//                        ).permitAll()                                            // 인증/인가 없이도 접근가능
//                        .anyRequest().authenticated())                           // 위 설정 URL 이외, 인가 X 인증 필요
//                .formLogin(formLogin -> formLogin   // 폼 기반 로그인 설정
//                        .loginPage("/login") // 로그인 페이지 경로를 설정
//                        .defaultSuccessUrl("/articles") // 로그인이 완료되었을 때 이동할 경로
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login") // 로그아웃 완료되었을 때 이동할 경로
//                        .invalidateHttpSession(true) // 로그아웃 이후에 세션을 전체 삭제할지 여부를 설정
//                )
//                .csrf(AbstractHttpConfigurer::disable) // CSRF 방지 설정(실습을 위해 비활성화)
//                .build(); // 최종 SecurityFilterChain 반환
//    }
//
//    // 인증 관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailsService userDetailsService) throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService); // 사용자 정보 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//        return new ProviderManager(authProvider);
//    }
//
//    // 패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}

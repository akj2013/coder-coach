package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.config.jwt.JwtFactory;
import com.codercoach.springbootdeveloper.config.jwt.JwtProperties;
import com.codercoach.springbootdeveloper.domain.RefreshToken;
import com.codercoach.springbootdeveloper.domain.User;
import com.codercoach.springbootdeveloper.dto.CreateAccessTokenRequest;
import com.codercoach.springbootdeveloper.repository.RefreshTokenRepository;
import com.codercoach.springbootdeveloper.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();;
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext securityContext = SecurityContextHolder.getContext();

        /* UsernamePasswordAuthenticationToken 생성자 구조
            principal	인증된 사용자 정보 (일반적으로 UserDetails, 또는 사용자 도메인 객체 등)
            credentials	사용자 자격 증명. 보통 비밀번호, 또는 토큰 등 인증에 사용되는 정보
            authorities	권한 정보 (ROLE_USER, ROLE_ADMIN 등)
         */
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("createNewAccessToken : 새로운 엑세스 토큰을 생성한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // GIVEN
        final String url = "/api/token";

        User testUser = userRepository.save(User.builder()
                .email("user@gamil.com")
                .password("test")
                .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // WHEN
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // GIVEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
        System.out.println(jsonPath("$.accessToken"));
    }

    @DisplayName("deleteRefreshToken : 리프레시 토큰을 삭제한다.")
    @Test
    public void deleteRfreshToken() throws Exception {
        // GIVEN
        final String url = "/api/refresh-token";

        String refreshToken = createRefreshToken();

        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, refreshToken, user.getAuthorities()));

        // WHEN
        ResultActions resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // THEN
        resultActions
                .andExpect(status().isOk());

        assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
    }

    private String createRefreshToken() {
        return JwtFactory.builder()
                .claims(Map.of("id", user.getId()))
                .build()
                .createToken(jwtProperties);
    }

}
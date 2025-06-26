package com.codercoach.springbootdeveloper.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2UserCustomService 클래스의
 * super.loadUser(...)는 DefaultOAuth2User 객체를 리턴합니다.
 * 이 객체는 내부에 당신이 만든 User 엔티티 정보를 포함하지 않습니다.
 * 그래서 SecurityContextHolder.getContext().getAuthentication().getPrincipal() 을 호출해도
 * 당신의 User 클래스가 아닌 OAuth2User (보통 DefaultOAuth2User) 가 나옵니다.
 *
 * 당신의 User 엔티티를 감싸는 CustomOAuth2User 클래스를 만들고, 그것을 반환해야 합니다.
 */
@RequiredArgsConstructor
@Getter
public class OAuth2CustomUser implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getNickname();
    }
}

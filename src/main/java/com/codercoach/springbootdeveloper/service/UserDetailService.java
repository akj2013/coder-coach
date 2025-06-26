package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 로그인 User에 대한 서비스 클래스
 * 스프링 시큐리티의 UserDetailService를 구현한다.
 * 로그인 시 인증용 사용자 정보 로딩
 * 사용자가 로그인할 때 Spring Security가 자동으로 호출
 */

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메소드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException((username)));
    }
}

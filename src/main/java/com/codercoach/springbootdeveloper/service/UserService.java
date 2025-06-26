package com.codercoach.springbootdeveloper.service;

import com.codercoach.springbootdeveloper.domain.User;
import com.codercoach.springbootdeveloper.dto.AddUserRequest;
import com.codercoach.springbootdeveloper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 로그인 유저 관련 서비스
 * 회원가입/사용자 관리 등의 일반 비즈니스 로직
 * 사용자가 회원가입하거나 직접 사용자 정보를 처리할 때 수동 호출
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 정보 추가
     * @param dto AddUserRequest 객체
     * @return Id(이메일)
     */
    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 유저 검색 by ID
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 유저가 없습니다."));
    }

    // 유저 검색 by EMAIL
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("EMAIL에 해당하는 유저가 없습니다."));
    }
}

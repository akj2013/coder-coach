package com.codercoach.springbootdeveloper.repository;

import com.codercoach.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 대한 리포지터리
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /*
    "값이 있을 수도 있고, 없을 수도 있다"
    는 것을 명시적으로 표현하기 위한 도구입니다.
    null-safe 코딩을 위해 자주 사용

    사용자가 존재하면 → Optional 내부에 User 객체가 담긴 상태
    사용자가 없으면 → Optional.empty() (null 대신)

    // 1. 값이 있는지 확인
    if (userOpt.isPresent()) {
        User user = userOpt.get(); // 값이 있음 → get()으로 가져옴
    }

    // 2. 값이 없으면 기본값 제공
    User user = userOpt.orElse(new User());

    // 3. 값이 없으면 예외 던지기
    User user = userOpt.orElseThrow(() -> new RuntimeException("사용자 없음"));

    // 4. 값이 있으면 특정 작업 수행
    userOpt.ifPresent(user -> {
        System.out.println("사용자 이메일: " + user.getEmail());
    });
     */
    Optional<User> findByEmail(String email); // email로 사용자 정보를 가져온다.
}

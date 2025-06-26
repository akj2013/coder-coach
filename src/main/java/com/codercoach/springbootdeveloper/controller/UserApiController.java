package com.codercoach.springbootdeveloper.controller;

import com.codercoach.springbootdeveloper.dto.AddUserRequest;
import com.codercoach.springbootdeveloper.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 로그인 유저 관련 컨트롤러
 *
 */

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    /* PRG 패턴 구현
    📌 1. PRG 패턴 (Post-Redirect-Get)
        웹에서 흔히 사용하는 디자인 패턴입니다.
        POST: 서버에 데이터 제출 (ex. 회원가입)
        Redirect: POST 결과 처리 후, GET 요청으로 다른 페이지로 이동
        GET: 결과 페이지 표시
    🔒 이유:
        사용자가 새로고침(F5)을 누를 때 **중복 요청(중복 가입)**이 일어나지 않도록 하기 위함
        URL이 /user로 남아있으면 사용자가 혼동할 수 있음
        명확하게 다음 화면(/login)으로 이동시킴
     */
    // 회원 가입 처리 API
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String signup(AddUserRequest request) {
        userService.save(request); // 회원가입 메소드 호출
        return "redirect:/login"; // 회원가입이 완료된 이후에 로그인 페이지로 이동
    }

    // 로그아웃 API
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}

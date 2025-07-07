package com.codercoach.springbootdeveloper.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 로그인, 회원가입 경로로 접근하는 뷰 파일 연결 컨트롤러
 *
 */
@Controller
public class UserViewController {

    // 로그인 화면
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public String login() {
//        return "login";
//    }
//    public String login() {
//        return "oauthLogin";
//    }
    public String login() {
        return "newLogin";
    }
    // 회원가입 화면
    @GetMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String signup() {
        return "signup";
    }

    // index 화면
    @GetMapping(value = "/index")
    public String index() { return "index"; }
}

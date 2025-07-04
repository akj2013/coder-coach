package com.codercoach.springbootdeveloper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SbAdmin2ViewController {

    @GetMapping(value = "/sbAdmin2/index")
    public String index() {
        return "/sbAdmin2/index";
    }

    @GetMapping(value = "/sbAdmin2/login")
    public String login() {
        return "/sbAdmin2/login";
    }

    @GetMapping(value = "/sbAdmin2/register")
    public String register() {
        return "/sbAdmin2/register";
    }

    @GetMapping(value = "/sbAdmin2/404")
    public String pageNotFound() {
        return "/sbAdmin2/404";
    }

    @GetMapping(value = "/sbAdmin2/blank")
    public String blank() {
        return "/sbAdmin2/blank";
    }

    @GetMapping(value = "/sbAdmin2/buttons")
    public String buttons() {
        return "/sbAdmin2/buttons";
    }

    @GetMapping(value = "/sbAdmin2/cards")
    public String cards() {
        return "/sbAdmin2/cards";
    }

    @GetMapping(value = "/sbAdmin2/charts")
    public String charts() {
        return "/sbAdmin2/charts";
    }

    @GetMapping(value = "/sbAdmin2/forgot-password")
    public String forgotPassword() {
        return "/sbAdmin2/forgot-password";
    }

    @GetMapping(value = "/sbAdmin2/tables")
    public String tables() {
        return "/sbAdmin2/tables";
    }

    @GetMapping(value = "/sbAdmin2/utilities-animation")
    public String animation() {
        return "/sbAdmin2/utilities-animation";
    }

    @GetMapping(value = "/sbAdmin2/utilities-border")
    public String border() {
        return "/sbAdmin2/utilities-border";
    }

    @GetMapping(value = "/sbAdmin2/utilities-color")
    public String color() {
        return "/sbAdmin2/utilities-color";
    }

    @GetMapping(value = "/sbAdmin2/utilities-other")
    public String other() {
        return "/sbAdmin2/utilities-other";
    }
}

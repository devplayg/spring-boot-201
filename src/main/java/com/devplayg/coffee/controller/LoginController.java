package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * REST APIs
 *
 * GET      /login/       display
 * POST     /login       login
 *
 */

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    public AppConfig appConfig;

    @GetMapping({"", "/"})
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) { // 이미 로그인 된 상태면
            return "redirect:" + appConfig.getHomeUri();
        } else {
            return "login/login";
        }
    }
}

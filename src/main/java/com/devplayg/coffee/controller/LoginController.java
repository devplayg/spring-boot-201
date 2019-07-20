package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequestMapping("login")
public class LoginController {

    @Autowired
    public AppConfig appConfig;

    @Autowired
    public InMemoryMemberManager inMemoryMemberManager ;

    @GetMapping({"", "/"})
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) { // 이미 로그인 된 상태면
            return "redirect:" + appConfig.getHomeUri();
        } else {
            return "login/login";
        }
    }

    @GetMapping("users")
    public ResponseEntity getUsers() {
//        inMemoryMemberManager.
//        UserDetails u1 = inMemoryMemberManager.loadUserByUsername("won");
//        UserDetails u2 = inMemoryMemberManager.loadUserByUsername("wsan");
//        log.debug("# user1: {}", u1);
//        log.debug("# user2: {}", u2);
        return new ResponseEntity(inMemoryMemberManager, HttpStatus.OK);
    }
}

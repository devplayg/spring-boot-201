package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;

/**
 * 로그인 컨트롤러
 */
@Controller
@Slf4j
@RequestMapping("login")
public class LoginController {

    public final AppConfig appConfig;
    public final DeviceService deviceService;

    public LoginController(AppConfig appConfig, DeviceService deviceService) {
        this.appConfig = appConfig;
        this.deviceService = deviceService;
    }

    @GetMapping({"", "/"})
    public String login(HttpServletRequest req, HttpServletResponse res, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) { // 이미 로그인 된 상태면
            return "redirect:" + appConfig.getHomeUri();
        }

        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            res.addCookie(new Cookie("APPLICATION_LOCALE", Locale.KOREA.toString()));
            return "login/login";
        }

        String cookie = Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals("APPLICATION_LOCALE"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if (cookie == null) {
            res.addCookie(new Cookie("APPLICATION_LOCALE", Locale.KOREA.toString()));
        }

        return "login/login";
    }

}

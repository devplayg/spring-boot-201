package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.util.WebHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class RootController {

    private final AppConfig appConfig;

    public RootController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping
    public ModelAndView redirectToLogin(ModelAndView mv) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            mv.setViewName("login/login");
            return mv;
        }

        // 불필요한 파라메터 없는 리다이렉션 처리
        mv.setView(WebHelper.getRedirectView(appConfig.getHomeUri()));
        return mv;
    }
}

package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.util.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    public AppConfig appConfig;

    @GetMapping({"", "/"})
    public ModelAndView login(ModelAndView mv) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            mv.setViewName("login/login");
            return mv;
        }
        mv.setView(WebHelper.getRedirectView(appConfig.getHomeUri()));
        return mv;
    }
}

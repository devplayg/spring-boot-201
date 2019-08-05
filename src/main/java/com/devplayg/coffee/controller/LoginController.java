package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.util.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping("login")
public class LoginController {

    private final AppConfig appConfig;

    public LoginController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping({"", "/"})
    public String login(HttpServletRequest req, HttpServletResponse res) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) { // Already logged in
            return "redirect:" + appConfig.getHomeUri();
        }

        Cookie[]cookies = req.getCookies();
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

//    @GetMapping({"", "/"})
//    public ModelAndView login(ModelAndView mv) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth instanceof AnonymousAuthenticationToken) {
//            mv.setViewName("login/login");
//            return mv;
//        }
//        mv.setView(WebHelper.getRedirectView(appConfig.getHomeUri()));
//        return mv;
//    }
}

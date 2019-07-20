package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class RootController {
    @Autowired
    public AppConfig appConfig;

    @GetMapping
    public ModelAndView redirectToLogin(ModelAndView mav) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            mav.setViewName("login/login");
            return mav;
        }

        // Following method provides unnecessary parameterless URLs when redirecting
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(appConfig.getHomeUri());
        redirectView.setExposeModelAttributes(false);
        mav.setView(redirectView);
        return mav;
    }
}

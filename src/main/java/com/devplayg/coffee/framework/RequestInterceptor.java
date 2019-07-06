package com.devplayg.coffee.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Interceptors
 */
@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private String username = "";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        // Logging
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("# Auth-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
//        log.info("##### Auth-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
//        log.info("##### Auth-3: object={}", auth.getDetails());

        Principal principal = req.getUserPrincipal();
        if (principal == null) {
            return true;
        }

        username = req.getUserPrincipal().getName();
        if ("anonymousUser".equals(username)) {
            return true;
        }

        Boolean anyNews = MembershipCenter.anyNews(username);
        if (!anyNews) {
            return true;
        }

        readNews(username);
        return true;

    }

    private void readNews(String username) {
        log.debug("# Got news to {}", username);
        UserDetails userDetails = MembershipCenter.readNews(username);
        if (userDetails != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "").toLowerCase();
            String methodName = handlerMethod.getMethod().getName();

            log.info("# Request from {} ({}, {}/{}): {}?{}", username, req.getMethod(), controllerName, methodName, req.getRequestURI(), req.getQueryString());
            if (methodName.startsWith("display")) {
                model.addObject("ctrl", controllerName);
            }
        }
    }
}





//
//        Boolean anyNews = mc.anyNews(req.getUserPrincipal().getName());
//        log.debug("### anyNews to {}: {}", req.getUserPrincipal().getName(), anyNews);
//        membershipCenter.anyNews("system");
//        membershipCenter().anyNews("abc");
//        log.info("##### Request: {} -- {}?{}", req.getMethod(), req.getRequestURI(), req.getQueryString());
//        Member m = (Member) auth.getDetails();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        log.debug("# session member: {}", m);
//        log.info("##### Auth-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
//        log.info("##### Auth-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
//        log.info("##### Auth-3: object={}", auth.getDetails());
//        SecurityContextHolder.getContext().getAuthentication().

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String username = ((UserDetails)principal).getUsername();
//        } else {
//            String username = principal.toString();
//        }
//        Member m = (Member) auth.getDetails();
//        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.debug("# session member: {}", m);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        if req.getUserPrincipal()

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
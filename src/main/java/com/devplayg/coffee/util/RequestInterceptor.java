package com.devplayg.coffee.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        log.info("##### Request: {} -- {}?{}", req.getMethod(), req.getRequestURI(), req.getQueryString());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("##### Auth-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
        log.info("##### Auth-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
//        log.info("##### Auth-3: object={}", auth.getDetails());
//        SecurityContextHolder.getContext().getAuthentication().
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //controllerName = handlerMethod.getBean().getClass().getSimpleName().replace("Controller", "");
            String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "").toLowerCase();
            String methodName = handlerMethod.getMethod().getName();
            log.info("### request: {} / {}",  controllerName, methodName);
            if (methodName.startsWith("display")) {
                model.addObject("ctrl", controllerName);
            }
        }
    }

}
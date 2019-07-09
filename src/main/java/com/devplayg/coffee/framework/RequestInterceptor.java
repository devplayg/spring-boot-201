package com.devplayg.coffee.framework;

import com.devplayg.coffee.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
import java.util.TimeZone;

/**
 * Interceptors
 */
@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private String username = "";
    private String userTz = TimeZone.getDefault().toZoneId().getId();

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            log.debug("### RequestInterceptor-0: not logged in yet");
            return true;
        }

//        Principal principal = req.getUserPrincipal();
//        if (principal == null) {
//            log.debug("### RequestInterceptor: WHAAAAAAAAAAAAAAAAAAAAAAA!");
//            return true;
//        }
        // Logging
        log.debug("### RequestInterceptor-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
        log.debug("### RequestInterceptor-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
        log.debug("### RequestInterceptor-3: object={}", auth.getDetails());
        Member member = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("### RequestInterceptor-4: member={}", member);

        username = req.getUserPrincipal().getName();
        userTz = member.getTimezone();
//        if ("anonymousUser".equals(username)) {
//            return true;
//        }
//
        Boolean anyNews = MembershipCenter.anyNews(username);
        if (!anyNews) {
            return true;
        }

        readNews(username);
        return true;

        // https://www.leafcats.com/40
//        HttpSession session = request.getSession();
//        UserVO loginVO = (UserVO) session.getAttribute("loginUser");
//        if(ObjectUtils.isEmpty(loginVO)){
//            response.sendRedirect("/moveLogin.go");
//            return false;
//        }
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "").toLowerCase();
            String methodName = handlerMethod.getMethod().getName();

            log.info("### postHandle from {} ({}, {}/{}): {}?{}", username, req.getMethod(), controllerName, methodName, req.getRequestURI(), req.getQueryString());
            if (methodName.startsWith("display")) {
                mv.addObject("ctrl", controllerName);
                mv.addObject("systemTz",  TimeZone.getDefault().toZoneId().getId());
                mv.addObject("userTz", userTz);
                log.debug("system tz: {}", TimeZone.getDefault().getRawOffset());
            }
        }
    }

    private void readNews(String username) {
        log.debug("==============================");
        log.debug("Got news to {}", username);
        log.debug("==============================");
        UserDetails userDetails = MembershipCenter.readNews(username);
        if (userDetails != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

}





//
//        Boolean anyNews = mc.anyNews(req.getUserPrincipal().getName());
//        log.debug("### anyNews to {}: {}", req.getUserPrincipal().getName(), anyNews);
//        membershipCenter.anyNews("system");
//        membershipCenter().anyNews("abc");
//        log.info("### Request: {} -- {}?{}", req.getMethod(), req.getRequestURI(), req.getQueryString());
//        Member m = (Member) auth.getDetails();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        log.debug("# session member: {}", m);
//        log.info("### Auth-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
//        log.info("### Auth-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
//        log.info("### Auth-3: object={}", auth.getDetails());
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
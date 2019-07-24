package com.devplayg.coffee.framework;

import com.devplayg.coffee.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            log.debug("# RequestInterceptor-preHandle: not logged in yet, {}", req.getRequestURI());
            return true;
        }

        // Need exception handler when member is deleted
        // If member information is changed
        InMemoryMemberManager inMemoryMemberManager = InMemoryMemberManager.getInstance();
        String username = req.getUserPrincipal().getName();
        if (inMemoryMemberManager.isChanged(username)) {
            UserDetails userDetails = inMemoryMemberManager.loadUserByUsername(username);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());
            // Update member information
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            inMemoryMemberManager.gotNews(username);
        }

//        }
//        UserDetails userDetails = null;
//        try {
//            userDetails = inMemoryMemberManager.loadUserByUsername(username);
//        } catch(UsernameNotFoundException e) {
//            return false;
//        }

        if (log.isDebugEnabled()) {
            log.debug("----------------- REQUEST ---------------------------------------");
            log.debug("# RequestInterceptor-1: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
            log.debug("# RequestInterceptor-2: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
            log.debug("# RequestInterceptor-3: object={}", auth.getDetails());
            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("# RequestInterceptor-4: member={}", member);
            log.debug("# RequestInterceptor-5: Url={}?{}", req.getRequestURI(), req.getQueryString());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        // if not model and view request
        if (mv == null) {
            return;
        }

        if (handler instanceof  HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "").toLowerCase();
            //String methodName = handlerMethod.getMethod().getName();
            mv.addObject("systemTz", TimeZone.getDefault().toZoneId().getId());
            mv.addObject("ctrl", controllerName);
            mv.addObject("remoteAddr", req.getRemoteAddr());

            // Get member'ㄴ timezone and set it to view object
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof AnonymousAuthenticationToken) {
                return;
            }
            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            mv.addObject("userTz", member.getTimezone());
        }
    }

}



//    private void readNews(String username) {
//        log.debug("==============================");
//        log.debug("Got news to {}", username);
//        log.debug("==============================");
//        UserDetails userDetails = MembershipCenter.readNews(username);
//        if (userDetails != null) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(newAuth);
//        }
//    }
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
package com.devplayg.coffee.util;

import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.vo.MembershipCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {


//    @Autowired
//    private MembershipCenter membershipCenter;

/*
 * Membership Center
 */
//    @Bean
//    public MembershipCenter membershipCenter() {
//        log.debug("## Loading membership center");
//        Hashtable<String, UserDetails> membership = new Hashtable<>();
//        Hashtable<String, Boolean> memberNews = new Hashtable<>();
//        List<Member> list = memberRepository.findAll();
//        for (Member m : list) {
//            membership.put(m.getUsername(), m);
//            memberNews.put(m.getUsername(), false);
//        }
//        return new MembershipCenter(membership, memberNews);
//    }


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {

//
//        Boolean anyNews = mc.anyNews(req.getUserPrincipal().getName());
//        log.debug("### anyNews to {}: {}", req.getUserPrincipal().getName(), anyNews);
//        membershipCenter.anyNews("system");
//        membershipCenter().anyNews("abc");
//        log.info("##### Request: {} -- {}?{}", req.getMethod(), req.getRequestURI(), req.getQueryString());
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Member m = (Member) auth.getDetails();
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
        Principal principal = req.getUserPrincipal();
        if (principal == null) {
            return true;
        }
//        if req.getUserPrincipal()
        String username = req.getUserPrincipal().getName();
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
        log.debug("## interceptor username: {}", username);
        if ("anonymousUser".equals(username)) {
            log.debug("## anonymousUser pass");
            return true;
        }

        MembershipCenter mc = MembershipCenter.getInstance();

        Boolean anyNews = mc.anyNews(username);
        log.debug("### anyNews to {}: {}", username, anyNews);

//        if (membershipCenter().anyNews(username)) {
//            log.debug("############## prehandle {}", handler);
//        handler
//        }

        if (anyNews) {
            UserDetails userDetails = mc.get(username);
//            req.
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials() , userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        return true;


//        membershipCenter

//        MembershipCenter mc = MembershipCenter

//        Boolean anyNews = membershipCenter.anyNews(username);
//        if (!anyNews) {
//            return true;
//        }

//        log.debug("### News to {}", username);


//        log.debug("## pre     auth: {}", auth);
//        log.debug("## pre username: {}", username);

//        auth.
//        auth.
        // 권한 리로드 필요여부 확인

        // 권한 리로드


    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //controllerName = handlerMethod.getBean().getClass().getSimpleName().replace("Controller", "");
            String controllerName = handlerMethod.getBeanType().getSimpleName().replace("Controller", "").toLowerCase();
            String methodName = handlerMethod.getMethod().getName();

            log.info("##### Request({}, {}/{}): {}?{}", req.getMethod(), controllerName, methodName, req.getRequestURI(), req.getQueryString());
            if (methodName.startsWith("display")) {
                model.addObject("ctrl", controllerName);
            }
        }
    }

    private void reloadAuth() {
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        updatedAuthorities.add(...); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        updatedAuthorities.add(...); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
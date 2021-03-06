package com.devplayg.coffee.framework;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ForbiddenException;
import com.devplayg.coffee.util.SubnetUtils;
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

    private final AppConfig appConfig;

    public RequestInterceptor(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (log.isDebugEnabled()) {
            log.debug("----------------- REQUEST ---------------------------------------");
            log.debug("# RequestInterceptor-1: [{} - {}] {}{}", req.getMethod(), req.getRemoteAddr(), req.getRequestURI(), (req.getQueryString() == null) ? "" : "?" + req.getQueryString());
            for (String key : req.getParameterMap().keySet()) {
                log.debug("     - {} = {}", key, req.getParameterMap().get(key));
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            log.debug("# RequestInterceptor-preHandle: [{}] not logged in yet, {}", req.getMethod(), req.getRequestURI());
            return true;
        }

        // Need exception handler when member is deleted
        // If member information is changed
        InMemoryMemberManager inMemoryMemberManager = InMemoryMemberManager.getInstance();
        if (req.getUserPrincipal() == null) return true;
        String username = req.getUserPrincipal().getName();
        if (inMemoryMemberManager.isChanged(username)) {
            UserDetails userDetails = inMemoryMemberManager.loadUserByUsername(username);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());
            // Update member information
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            inMemoryMemberManager.gotNews(username);
        }

        // IP Filtering
        if (appConfig.getUseIpBlocking()) {
            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("# member networks: {}", member.getSubnetUtils());
            if (member.getSubnetUtils().size() > 0) {
                boolean allowed = false;

                for (SubnetUtils s : member.getSubnetUtils()) {
                    if (s.getInfo().isInRange(req.getRemoteAddr())) {
                        allowed = true;
                    }
                }

                if (!allowed) {
                    throw new ForbiddenException();
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("# RequestInterceptor-2: name={}, isLogged={}, role={}", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
//            log.debug("# RequestInterceptor-3: username={}, detail={}", auth.getPrincipal(), auth.getDetails());
//            log.debug("# RequestInterceptor-4: object={}", auth.getDetails());
//            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            log.debug("# RequestInterceptor-5: member={}", member);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        // if not model and view request
        if (mv == null) {
            return;
        }

        // Set view's variables
        if (handler instanceof HandlerMethod) {
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
            mv.addObject("memberName", member.getName());
            mv.addObject("memberUsername", member.getUsername());
        }
    }

}

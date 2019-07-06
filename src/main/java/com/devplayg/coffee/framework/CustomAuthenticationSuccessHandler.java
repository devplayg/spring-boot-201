package com.devplayg.coffee.framework;

// https://www.baeldung.com/spring_redirect_after_login

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private AuditService auditService;

    @Autowired
    private AppConfig appConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication auth)
            throws IOException {

        auditService.audit(AuditCategory.LOGIN_SUCCESS,  request.getQueryString());
        handle(request, response, auth);

        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication)
            throws IOException {
//        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            log.warn("Response has already been committed. Unable to redirect to "+ appConfig.getHomeUri());
            return;
        }

        redirectStrategy.sendRedirect(request, response, appConfig.getHomeUri());
    }

    protected String determineTargetUrl(Authentication authentication) {
//        boolean isUser = false;
//        boolean isAdmin = false;
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for (GrantedAuthority grantedAuthority : authorities) {
//            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
//                isUser = true;
//                break;
//            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
//                isAdmin = true;
//                break;
//            }
//        }


//        if (isAdmin) {
//
//        if (isUser) {
//            return "/audit/";
//        } else if (isAdmin) {
//            return "/members/";
//        } else {
//            throw new IllegalStateException();
//        }
        return "";
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}

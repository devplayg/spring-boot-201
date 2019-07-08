package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.framework.CustomAuthenticationFailureHandler;
import com.devplayg.coffee.framework.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Hashtable;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        httpSecurity
//                .authorizeRequests()
//
//                // 아래 URL 패턴에 매칭되면
//                .antMatchers("/audit/**")
//                .hasAnyRole(RoleType.Role.ADMIN.getCode(), RoleType.Role.SHERIFF.getCode())
//
//                // 아래 URL 패턴에 매칭되면
//                .antMatchers("/", "/login/**", "/modules/**", "/plugins/**", "/css/**", "/img/**", "/js/**")
//                // 모든 요청을 허용함
//                .permitAll() // 이 URL 패턴들은 인증요구 없이 허용
//                // 그외 요청은
//                .anyRequest()
//                // 인증을 요구
//                .authenticated()
//                .and()
//
//                // 로그인
//                .formLogin()
//                // https://docs.spring.io/spring-security/site/docs/current/guides/html5/form-javaconfig.html
//                // 로그인 페이지
//                .loginPage("/login")
//
//                // 로그인 절차 진행
//                .loginProcessingUrl("/app-login")
//
//                // 사용자 아이디 파라메터
//                .usernameParameter("app_username")
//
//                // 사용자 비밀번호 파라메터
//                .passwordParameter("app_password")
//
//                // 로그인 성공 시
//                //.successForwardUrl("/app/articles")
//                //.defaultSuccessUrl(appConfig.getHomeUri())
//                .successHandler(authenticationSuccessHandler())
//
//                // 로그인 실패 시
//                .failureHandler(authenticationFailureHandler())
//                //.failureUrl("/login?error")
////                .failureForwardUrl()
//                //.successForwardUrl("/members")
//                .permitAll()
//                .and()
//
//                .logout()
//                // 로그아웃 URL
//                .logoutUrl("/logout")
//
//                // 로그아웃 성공 시 리다이렉트 될 URL
//                .logoutSuccessUrl("/login")
//
//                // 쿠키 삭제
//                .deleteCookies("JSESSIONID")
//
//                // 세션 제거
//                .invalidateHttpSession(true)
//
//                .permitAll()
//                .and()
//
//                .addFilterBefore(filter, CsrfFilter.class)
//                .csrf().disable()
//
//                .sessionManagement().maximumSessions(10).sessionRegistry(sessionRegistry());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 로그인 disabled
        http.httpBasic().disable();
        http
            .cors()
            .and()

            .csrf()
                .disable();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Hashtable<String, Boolean> userWatcher() {
        return new Hashtable<>();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}

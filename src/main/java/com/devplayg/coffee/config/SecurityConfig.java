package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.framework.CustomAuthenticationFailureHandler;
import com.devplayg.coffee.framework.CustomAuthenticationSuccessHandler;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.TimeZone;

/**
 * Security configuration
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppConfig appConfig;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.debug("# security: {}", appConfig.getPathPatternsNotToBeIntercepted());
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        httpSecurity
                .authorizeRequests()

                // API for Administrators
                .antMatchers("/audit/**", "/members/**")
                .hasAnyRole(RoleType.Role.ADMIN.getCode(), RoleType.Role.SHERIFF.getCode())

                // White APIs
//                .antMatchers("/favicon.ico", "/assets/**", "/modules/**", "/plugins/**", "/css/**", "/font/**", "/img/**", "/js/**")
                .antMatchers(appConfig.getPathPatternsNotToBeIntercepted().stream().toArray(String[]::new))
                .permitAll()
//                .antMatchers("/favicon.ico", "/css/**")
//                .permitAll()

                // Others need to be authenticated
                .anyRequest()
                .authenticated()
                .and()

                // Login
                .formLogin()
                // https://docs.spring.io/spring-security/site/docs/current/guides/html5/form-javaconfig.html
                // Login plage
                .loginPage("/login")
                .loginProcessingUrl("/app-login")
                .usernameParameter("app_username")
                .passwordParameter("app_password")

                // Logged in successfully
                //.successForwardUrl("/app/articles")
                //.defaultSuccessUrl(appConfig.getHomeUri())
                //.successForwardUrl("/members")
                .successHandler(authenticationSuccessHandler())
                // Login failed
                .failureHandler(authenticationFailureHandler())
                //.failureUrl("/login?error")
                //.failureForwardUrl()

                .permitAll()
                .and()

                // Logout
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
                .and()

                .addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable();

        // .sessionManagement().maximumSessions(10).sessionRegistry(sessionRegistry());
    }

//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            // Spring security off
//            http.httpBasic().disable();
//            http
//                .cors()
//                .and()
//
//                .csrf()
//                    .disable();
//        }

//    @Bean
//    public SessionRegistry sessionRegistry() {
//        return new SessionRegistryImpl();
//    }


    /**
     * Default password encoder is BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Handle a successful user authentication
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     * Handle a failed authentication attempt
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /**
     * Use InMemory authentication
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryMemberManager());
    }

    @Bean
    public InMemoryMemberManager inMemoryMemberManager() {
        List members = memberRepository.findAll();
//        Member member = Member.builder()
//                .id(InMemoryMemberManager.adminId)
//                .username(InMemoryMemberManager.adminUsername)
//                .enabled(false)
//                .name("System Administrator")
//                .roles(RoleType.Role.ADMIN.getValue())
//                .timezone(TimeZone.getDefault().toZoneId().getId())
//                .email("admin@admin.com")
//                .password("")
//                .build();
        Member member = new Member();
        member.setId(InMemoryMemberManager.adminId);
        member.setUsername(InMemoryMemberManager.adminUsername);
        member.setEnabled(false);
        member.setName(appConfig.getAdminInfo().getName());
        member.setRoles(RoleType.Role.ADMIN.getValue());
        member.setTimezone(TimeZone.getDefault().toZoneId().getId());
        member.setEmail(appConfig.getAdminInfo().getEmail());
        member.setPassword("");
        members.add(member);
        return new InMemoryMemberManager(members);
    }

//    @Bean
//    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowSemicolon(true);
//        return firewall;
//    }

}

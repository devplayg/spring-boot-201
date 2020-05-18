package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.framework.AssetManager;
import com.devplayg.coffee.framework.CustomAuthenticationFailureHandler;
import com.devplayg.coffee.framework.CustomAuthenticationSuccessHandler;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.asset.AssetRepository;
import com.devplayg.coffee.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberRepository memberRepository;

    private final AssetRepository assetRepository;

    private final AppConfig appConfig;

    public SecurityConfig(MemberRepository memberRepository, AppConfig appConfig, AssetRepository assetRepository) {
        this.memberRepository = memberRepository;
        this.appConfig = appConfig;
        this.assetRepository = assetRepository;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        httpSecurity
                .authorizeRequests()

                // API for Administrators
                .antMatchers("/audit/**", "/members/**")
                .hasAnyRole(RoleType.Role.ADMIN.getCode(), RoleType.Role.SHERIFF.getCode())

                .antMatchers("/cameras/{\\d+}/policy", "/factoryevent/{\\d+}/toggleEventType")
                .hasAnyRole(RoleType.Role.ADMIN.getCode())

                // White APIs
                .antMatchers(appConfig.getPathPatternsNotToBeIntercepted().stream().toArray(String[]::new))
                .permitAll()

                // Others need to be authenticated
                .anyRequest()
                .authenticated()
                .and()

                // Login
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/app-login")
                .usernameParameter("app_username")
                .passwordParameter("app_password")

                // Logged in successfully
                .successHandler(authenticationSuccessHandler())

                // Login failed
                .failureHandler(authenticationFailureHandler())

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
    }


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

    /**
     * Firewall
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }


    /**
     * Asset
     */
    @Bean
    public AssetManager assetManager() {
        return new AssetManager(assetRepository.findAllByOrderByTypeAscNameAsc());
    }
}

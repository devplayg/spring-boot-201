package com.devplayg.coffee.service;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("member", "username", username));
        log.info("member: {}", member.toString());
//        String[] roles = member.getRoleEnumList().stream().map(role -> role.getKey()).toArray(String[]::new);
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        UserDetails userDetails = (UserDetails) new User(member.getUsername(), member.getPassword(), getAuthorities(member));

        return userDetails;
    }

    private List<? extends GrantedAuthority> getAuthorities(Member member) {
        String[] roles = member.getRoleList().stream().map(role -> "ROLE_" + role.getRole().getCode()).toArray(String[]::new);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        return authorities;
    }
}


package com.devplayg.coffee.service;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 서비스
 */
@Service
@Slf4j
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

//    @Autowired
//    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

//    @PostConstruct
//    public void initUsers() {
//        log.debug("### initUsers in InMemoryUserDetailsManager");
//        List<Member> members = memberRepository.findAll();
//        for (Member m : members) {
//            inMemoryUserDetailsManager.createUser(m);
//        }

//        inMemoryUserDetailsManager.
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        users = Stream.of(new User("user3", passwordEncoder.encode("pass3"), authorities)).collect(Collectors.toSet());
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.debug("# loadUserByUsername");
//        UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);
//        if (userDetails == null) {
//            throw new ResourceNotFoundException("member", "member.username", username);
//        }
//        return userDetails;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);
//        if (userDetails == null) {
//            throw new ResourceNotFoundException("member", "member.username", username);
//        }
//        return userDetails;
//    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("member", "member.username", username));
        return member;
    }

//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("member: {}", member.toString());
//        String[] roles = member.getRoleEnumList().stream().map(role -> role.getKey()).toArray(String[]::new);
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
//        UserDetails userDetails = (UserDetails) new User(member.getUsername(), member.getPassword(), getAuthorities(member));

//    private List<? extends GrantedAuthority> getAuthorities(Member member) {
//        String[] roles = member.getRolesKeys().stream().map(role -> "ROLE_" + role).toArray(String[]::new);
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
//        return authorities;
//    }
}



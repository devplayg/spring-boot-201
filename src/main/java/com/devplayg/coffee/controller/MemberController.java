package com.devplayg.coffee.controller;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.MemberNetwork;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.util.SubnetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



/*
 * REST APIs
 *
 * GET      /members/       display
 * GET      /members        list
 * POST     /members        insert
 * GET      /members/{id}   get
 * PATCH    /members/{id}   update
 * DELETE   /members/{id}   delete
 *
 */

@Controller
@RequestMapping("members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    @Autowired
    private InMemoryMemberManager inMemoryMemberManager;

    @GetMapping("/")
    public String display() {
        return "member/member";
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Member> list = memberRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
     * 사용자 등록
     */
    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute @Valid Member member, BindingResult bindingResult) {
        log.debug("# post member: {}", member);

        // Check binding error
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        // Set member role
        member.setRoles(member.getRoleList().stream().mapToInt(i -> i.getValue()).sum());
//        List<MemberRole> list = member.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

        // Encrypt password
        member.setPassword(passwordEncoder.encode(member.getInputPassword()));

        // Set allowed IP list
        List<MemberNetwork> memberNetworks = null;
        try {
            memberNetworks = this.getValidNetworkList(member);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        member.setAccessibleIpList(memberNetworks);

        // Save
        try {
            log.debug("# post member converted: {}", member);
            Member newMember = memberRepository.save(member);
            inMemoryMemberManager.createUser(member);
            auditService.audit(AuditCategory.MEMBER_CREATE, newMember);
            return new ResponseEntity<>(newMember.getId(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * 사용자 조회
     */
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /*
     * 사용자 업데이트
     */
    @PatchMapping("{id}")
    public ResponseEntity<?> update(@ModelAttribute Member input, @PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        member.setName(input.getName());
        member.setEmail(input.getEmail());
        member.setEnabled(input.isEnabled());
        member.setTimezone(input.getTimezone());

        // 권한
        member.setRoles(input.getRoleList().stream().mapToInt(i -> i.getValue()).sum());
//        // Set member roles
//        List<MemberRole> list = input.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

        // 접속 허용 IP
        List<MemberNetwork> memberNetworks = null;
        try {
            memberNetworks = this.getValidNetworkList(input);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        member.setAccessibleIpList(memberNetworks);

        try {
            log.debug("# update member: {}", member);
            Member changed = memberRepository.save(member);
            log.debug("## member changed: {}", changed);
            log.debug("## member     now: {}", member);
            inMemoryMemberManager.updateUser(member);
            auditService.audit(AuditCategory.MEMBER_UPDATE, member);
            inMemoryMemberManager.informMemberHasChanged(member.getUsername());
            return new ResponseEntity<>(changed, HttpStatus.OK);

         } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * 사용자 삭제
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        memberRepository.deleteById(id);
        inMemoryMemberManager.deleteUser(member.getUsername());
        auditService.audit(AuditCategory.MEMBER_REMOVE, member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // test -------------------------------------------------------------------------
//    @GetMapping("ship")
//    public ResponseEntity<?> ship() {
//        MembershipCenter mc = MembershipCenter.getInstance();
//        return new ResponseEntity<>(mc.getMembership(), HttpStatus.OK);
//    }
//
//    @GetMapping("news")
//    public ResponseEntity<?> news() {
//        MembershipCenter mc = MembershipCenter.getInstance();
//        return new ResponseEntity<>(mc.getMemberNews(), HttpStatus.OK);
//    }

    private List<MemberNetwork> getValidNetworkList(Member member) throws IllegalArgumentException {
        return Arrays.stream(member.getAccessibleIpListText().split("\\s+|,\\s*|\\,\\s*"))
                // CIDR Notation 으로 변경
                .map(s -> {
                    s = s.trim();
                    return s + (s.contains("/") ? "" : "/32");
                })

                // 공백 IP 제거
                .filter(s -> s.length() >= 10) // 1.1.1.1/32

                // IP 유효성 검사
                .map(s -> {
                    try {
                        new SubnetUtils(s);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(e.getMessage() + ", "+ s);
                    }
                    MemberNetwork memberNetwork =new MemberNetwork(s);
                    memberNetwork.setMember(member);
                    return memberNetwork;
                })
                .collect(Collectors.toList());
    }

//    @GetMapping("info")
//    public ResponseEntity<?> currentUserName(Authentication auth) {
//        String[] arr = "1.1.1.0    ,,1.1.1.1,,   10.10.10.0/24\n   192.168.0.3".split("\\s+|,\\s*|\\,\\s*");
//        List<String> arr2 = Arrays.stream(arr).map(s -> s.trim()).filter(ip -> ip.length() > 0).collect(Collectors.toList());
//
//        for (String s : arr) {
//            System.out.println(s);
//        }
//        System.out.println("==============================");
//
//        for (String s : arr2) {
//            System.out.println(s);
//        }
//
//        log.debug("## auth: {}", auth);
//        return new ResponseEntity<>(auth, HttpStatus.OK);
//    }
//
//    private Boolean isValidNetwork(String ip) {
//        if (ip.length() < 1) {
//            return true;
//        }
//    }

//    @GetMapping("session")
//    public ResponseEntity<?> getAllSessions() {
//        List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
//        log.info("logged: {}", loggedUsers);
//        return new ResponseEntity<>(loggedUsers, HttpStatus.OK);
//        for (UserDetails user : list) {
//            if (user.getUsername().equals(username)) {
//                List<GrantedAuthority> roles = new ArrayList<>(user.getAuthorities());
//                roles.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
//                Authentication newAuth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), roles);
//                List<SessionInformation> sessions = sessionRegistry.getAllSessions(user, false);
//
//                log.info("sessions: {}", sessions.toString());
//                for (SessionInformation s : sessions) {
//                }
//            }
//        }
//        List<UserDetails> list = sessionRegistry.getAllPrincipals()
//                .stream()
//                .map(e -> (UserDetails) e)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

}

package com.devplayg.coffee.controller;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.framework.MembershipCenter;
import com.devplayg.coffee.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/")
    public String display() {
        return "member/member";
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Member> list = memberRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute @Valid Member member, BindingResult bindingResult) {

        // Check binding errors
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Result(bindingResult), HttpStatus.OK);
        }

        member.setRoles(member.getRoleList().stream().mapToInt(i-> i.getValue()).sum());
        // Set member role
//        List<MemberRole> list = member.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

        // Encrypt password
        member.setPassword(passwordEncoder.encode(member.getInputPassword()));

        // Save
        try {
            Member newMember = memberRepository.save(member);
            auditService.audit(AuditCategory.MEMBER_CREATE, newMember);
            return new ResponseEntity<>(new Result(newMember.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new Result(e.getMessage()), HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        return new ResponseEntity<>(new Result(member), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@ModelAttribute Member input, @PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        member.setName(input.getName());
        member.setEmail(input.getEmail());
        member.setEnabled(input.isEnabled());
        member.setTimezone(input.getTimezone());
        member.setRoles(input.getRoleList().stream().mapToInt(i-> i.getValue()).sum());
//        // Set member roles
//        List<MemberRole> list = input.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

        Member changed = memberRepository.save(member);
        log.debug("## member changed: {}", changed);
        log.debug("## member     now: {}", member);
        auditService.audit(AuditCategory.MEMBER_UPDATE, member);
        MembershipCenter.notifyChanges(changed);
        return new ResponseEntity<>(new Result(changed), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        memberRepository.deleteById(id);
        auditService.audit(AuditCategory.MEMBER_REMOVE, member);
        return new ResponseEntity<>(new Result(id), HttpStatus.OK);
    }



    // test -------------------------------------------------------------------------
    @GetMapping("ship")
    public ResponseEntity<?> ship() {
        MembershipCenter mc = MembershipCenter.getInstance();
        return new ResponseEntity<>(mc.getMembership(), HttpStatus.OK);
    }

    @GetMapping("news")
    public ResponseEntity<?> news() {
        MembershipCenter mc = MembershipCenter.getInstance();
        return new ResponseEntity<>(mc.getMemberNews(), HttpStatus.OK);
    }

    @GetMapping("info")
    public ResponseEntity<?> currentUserName(Authentication auth) {
        log.debug("## auth: {}", auth);
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }

    @GetMapping("session")
    public ResponseEntity<?> getAllSessions() {
        List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
        log.info("logged: {}", loggedUsers);
        return new ResponseEntity<>(loggedUsers, HttpStatus.OK);
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

    }

}

package com.devplayg.coffee.controller;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
 * Member controller REST APIs
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
    private MemberService memberService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private InMemoryMemberManager inMemoryMemberManager;

    /**
     * Display
     */
    @GetMapping("/")
    public String display() {
        return "member/member";
    }

    /**
     * Get member list
     */
    @GetMapping
    public ResponseEntity<?> list() {
        List<Member> list = memberRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Get member
     */
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * Create member
     */
    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute @Valid Member member, BindingResult bindingResult) {
        log.debug("# post member: {}", member);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        try {
            Member memberCreated = memberService.create(member);
            return new ResponseEntity<>(memberCreated, HttpStatus.OK);
        } catch(DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update member
     */
    @PatchMapping("{id}")
    public ResponseEntity<?> patch(@ModelAttribute Member member, @PathVariable("id") long id, BindingResult bindingResult) {

        log.debug("# patch member: {}", member);

        // Check binding error
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        try {
            member.setId(id);
            Member memberCreated = memberService.update(id, member);
            return new ResponseEntity<>(memberCreated, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Delete member
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));

        // Delete member from database
        memberRepository.deleteById(id);

        // Delete member from in-memory database
        inMemoryMemberManager.deleteUser(member.getUsername());

        // Write audit log
        auditService.audit(AuditCategory.MEMBER_REMOVE, member);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

//    private List<MemberNetwork> getValidNetworkList(Member member) throws IllegalArgumentException {
//        return Arrays.stream(member.getAccessibleIpListText().split("\\s+|,\\s*|\\,\\s*"))
//                // Convert IP address to CIDR notation format
//                .map(s -> {
//                    s = s.trim();
//                    return s + (s.contains("/") ? "" : "/32");
//                })
//
//                // Filter
//                .filter(s -> s.length() >= 10) // 1.1.1.1/32
//
//                // Validate CIDR notation format
//                .map(s -> {
//                    try {
//                        new SubnetUtils(s);
//                    } catch (IllegalArgumentException e) {
//                        throw new IllegalArgumentException(e.getMessage() + ", "+ s);
//                    }
//                    MemberNetwork memberNetwork =new MemberNetwork(s);
//                    memberNetwork.setMember(member);
//                    return memberNetwork;
//                })
//                .collect(Collectors.toList());
//    }
//    @PostMapping
//    public ResponseEntity<?> create(@ModelAttribute @Valid Member member, BindingResult bindingResult) {
//        log.debug("# post member: {}", member);
//
//        // Check binding error
//        if (bindingResult.hasErrors()) {
//            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
//        }
//
//        // Set member role
//        member.setRoles(member.getRoleList().stream().mapToInt(i -> i.getValue()).sum());
////        List<MemberRole> list = member.getRoleList().stream()
////                .filter(r -> r.getRole() != null)
////                .collect(Collectors.toList());
////        list.forEach(r -> r.setMember(member));
////        member.setRoleList(list);
//
//        // Encrypt password
//        member.setPassword(passwordEncoder.encode(member.getInputPassword()));
//
//        // Set allowed IP list
//        List<MemberNetwork> memberNetworks;
//        try {
//            memberNetworks = this.getValidNetworkList(member);
//        } catch(IllegalArgumentException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        member.setAccessibleIpList(memberNetworks);
//
//        // Save
//        try {
//            log.debug("# post member converted: {}", member);
//            Member newMember = memberRepository.save(member);
//            inMemoryMemberManager.createUser(member);
//            auditService.audit(AuditCategory.MEMBER_CREATE, newMember);
//            return new ResponseEntity<>(newMember.getId(), HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//    @PatchMapping("{id}")
//    public ResponseEntity<?> update(@ModelAttribute Member input, @PathVariable("id") long id) {
//        Member member = memberRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
//        member.setName(input.getName());
//        member.setEmail(input.getEmail());
//        member.setEnabled(input.isEnabled());
//        member.setTimezone(input.getTimezone());
//
//        // 권한
//        member.setRoles(input.getRoleList().stream().mapToInt(i -> i.getValue()).sum());
////        // Set member roles
////        List<MemberRole> list = input.getRoleList().stream()
////                .filter(r -> r.getRole() != null)
////                .collect(Collectors.toList());
////        list.forEach(r -> r.setMember(member));
////        member.setRoleList(list);
//
//        // Accessible IP list
//        List<MemberNetwork> memberNetworks;
//        try {
//            memberNetworks = this.getValidNetworkList(input);
//        } catch(IllegalArgumentException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        member.setAccessibleIpList(memberNetworks);
//
//        try {
//            log.debug("# update member: {}", member);
//            Member changed = memberRepository.save(member);
//            log.debug("## member changed: {}", changed);
//            log.debug("## member     now: {}", member);
//            inMemoryMemberManager.updateUser(member);
//            auditService.audit(AuditCategory.MEMBER_UPDATE, member);
//            inMemoryMemberManager.informMemberHasChanged(member.getUsername());
//            return new ResponseEntity<>(changed, HttpStatus.OK);
//
//         } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

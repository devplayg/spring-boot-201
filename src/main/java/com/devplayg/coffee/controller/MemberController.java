package com.devplayg.coffee.controller;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.member.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.service.MemberService;
import com.devplayg.coffee.vo.MemberPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * 사용자 컨트롤러
 */
@Controller
@RequestMapping("members")
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final AuditService auditService;

    private final InMemoryMemberManager inMemoryMemberManager;

    public MemberController(MemberRepository memberRepository, MemberService memberService, AuditService auditService, InMemoryMemberManager inMemoryMemberManager) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.auditService = auditService;
        this.inMemoryMemberManager = inMemoryMemberManager;
    }

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


    /**
     * Update password
     */
    @PatchMapping("{id}/password")
    public ResponseEntity<?> patch(@ModelAttribute MemberPassword memberPassword, @PathVariable("id") long id, BindingResult bindingResult) {
//        log.debug("pw: {}", input);
        memberService.updatePassword(id, memberPassword);
        return new ResponseEntity<>(memberPassword, HttpStatus.OK);
    }
}

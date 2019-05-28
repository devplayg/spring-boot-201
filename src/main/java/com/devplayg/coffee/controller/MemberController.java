package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.MemberRole;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String display() {
        return "member/member";
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Member> list = memberRepository.findAll();
        ResponseEntity.ok(list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insert(@ModelAttribute @Valid Member member, BindingResult bindingResult) {

        // Check binding errors
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Result(bindingResult), HttpStatus.OK);
        }

        // Set member role
        List<MemberRole> list = member.getRoleList().stream()
                .filter(r -> r.getRole() != null)
                .collect(Collectors.toList());
        list.forEach(r -> r.setMember(member));
        member.setRoleList(list);

        // Encrypt password
        member.setPassword(bCryptPasswordEncoder.encode(member.getInputPassword()));

        // Save
        try {
            Member newMember = memberRepository.save(member);
            return new ResponseEntity<>(new Result(newMember.getId()), HttpStatus.OK);
        } catch (Exception e) {
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

        // Set member roles
        List<MemberRole> list = input.getRoleList().stream()
                .filter(r -> r.getRole() != null)
                .collect(Collectors.toList());
        list.forEach(r -> r.setMember(member));
        member.setRoleList(list);

        Member changed = memberRepository.save(member);
        return new ResponseEntity<>(new Result(changed), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        memberRepository.deleteById(id);
        return new ResponseEntity<>(new Result(id), HttpStatus.OK);
    }
}

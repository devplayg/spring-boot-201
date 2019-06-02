package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Hashtable;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Hashtable<String, Boolean> userWatcher;

    @Autowired
    private EntityManager em;

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
    public ResponseEntity<?> insert(@ModelAttribute @Valid Member member, BindingResult bindingResult) {

        // Check binding errors
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Result(bindingResult), HttpStatus.OK);
        }

//        log.info("member: {}", member.toString());
        member.setRoles(member.getRoleList().stream().mapToInt(i-> i.getValue()).sum());
        //  member: Member(id=0, username=xxxxaa, email=xxxxaa@a.com, name=xxxxaa, enabled=false, roles=0, timezone=Asia/Seoul, created=null, updated=null, roleList=[ADMIN, SHERIFF, USER])
        // Set member role
//        List<MemberRole> list = member.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

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
        member.setRoles(input.getRoleList().stream().mapToInt(i-> i.getValue()).sum());
//        // Set member roles
//        List<MemberRole> list = input.getRoleList().stream()
//                .filter(r -> r.getRole() != null)
//                .collect(Collectors.toList());
//        list.forEach(r -> r.setMember(member));
//        member.setRoleList(list);

        Member changed = memberRepository.save(member);
        return new ResponseEntity<>(new Result(changed), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        memberRepository.deleteById(id);
        return new ResponseEntity<>(new Result(id), HttpStatus.OK);
    }

    @GetMapping("test")
    public ResponseEntity<?> test() {
        userWatcher.put("member", false);
        return new ResponseEntity<>(userWatcher, HttpStatus.OK);
    }
}

package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("members")
/*
 * REST API and method name
 *
 * GET      /members/       display
 * GET      /members        list
 * GET      /members/3      get
 * POST     /members        insert
 * PATCH    /members/3      patch
 * DELETE   /members/3      delete
 */


public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/")
    public String display() {
        return "member/member1";
    }

    @GetMapping("list")
    public ResponseEntity<?> list() {
        List<Member> list = memberRepository.findAll();
        ResponseEntity.ok(list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

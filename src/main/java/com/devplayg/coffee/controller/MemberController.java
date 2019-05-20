package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("members")
/*
 * REST API and method name
 *
 * GET      /members/       display
 * GET      /members        list
 * POST     /members        insert
 * GET      /members/{id}   get
 * PATCH    /members/{id}   patch
 * DELETE   /members/{id}   delete
 *
 */





public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

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
    public void insert() {
    }

    @GetMapping("{id}")
    public void get() {
    }

    @PatchMapping("{id}")
    public void patch() {
    }

    @DeleteMapping("{id}")
    public void delete() {
    }
}

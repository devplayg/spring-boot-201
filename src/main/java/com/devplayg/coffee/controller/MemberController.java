package com.devplayg.coffee.controller;

import com.devplayg.coffee.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public String get() {
        return "member/member";
    }
}

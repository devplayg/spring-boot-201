package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.repository.AuditRepository;
import com.devplayg.coffee.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuditRepository auditRepository;

    @GetMapping("/users/{id}")
    public Member findUserById(@PathVariable("id") Member member) {
        return member;
    }

    @GetMapping("/users")
    public Page<Member> findAllUsers(Pageable pageable) {
        log.debug("page: {}", pageable);
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/test/{id}")
    public Audit findAuditById(@PathVariable("id") Audit audit) {
        return audit;
    }

    @GetMapping("/test")
    public Page<Audit> findAllAudits(Pageable pageable, @ModelAttribute AuditFilter filter) {
        log.debug("auditFilter: {}", filter);
        log.debug("page: {}", pageable);
        return auditRepository.findAll(pageable);
    }


//    @GetMapping("/filteredusers")
//    public Iterable<Audit> getUsersByQuerydslPredicate(@QuerydslPredicate(root = Audit.class) Predicate predicate) {
//        return auditRepository.findAll(predicate);
//    }

}

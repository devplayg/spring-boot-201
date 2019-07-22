package com.devplayg.coffee.controller;

import com.devplayg.coffee.config.AppConfig;
import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.support.audit.AuditRepository;
import com.devplayg.coffee.repository.support.member.MemberRepository;
import com.devplayg.coffee.repository.support.sample.SampleRepository;
import com.devplayg.coffee.repository.support.sample.SampleRepositorySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("test")
public class TestController {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleRepositorySupport sampleRepositorySupport;

//    @Autowired
//    private AppConfig appConfig;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private AuditRepository auditRepository;
//
//
//
//    @Autowired
//    public InMemoryMemberManager inMemoryMemberManager ;
//
//    @GetMapping("/users/{id}")
//    public Member findUserById(@PathVariable("id") Member member) {
//        return member;
//    }
//
//    @GetMapping("users")
//    public Page<Member> findAllUsers(Pageable pageable) {
//        log.debug("page: {}", pageable);
//        return memberRepository.findAll(pageable);
//    }
//
    @GetMapping("jpa")
    public List<Audit> findAllWithJPA() {
        return sampleRepository.findAll();
    }

    @GetMapping("custom")
    public List<Audit> findAllWithCustom() {
        return sampleRepositorySupport.findAll();
    }

//    @GetMapping("audit")
//    public List<Audit> findAllAudits() {
//        return sampleRepository.findAll();
//    }
//
//    @GetMapping("/test/{id}")
//    public Audit findAuditById(@PathVariable("id") Audit audit) {
//        return audit;
//    }
//
//    @GetMapping("/test")
//    public Page<Audit> findAllAudits(Pageable pageable, @ModelAttribute AuditFilter filter) {
//        log.debug("auditFilter: {}", filter);
//        log.debug("page: {}", pageable);
//        return auditRepository.findAll(pageable);
//    }
//
//    @GetMapping("info")
//    public ResponseEntity<?> currentUserName(Authentication auth) {
//        log.debug("## auth: {}", auth);
//        return new ResponseEntity<>(auth, HttpStatus.OK);
//    }
//
//    @GetMapping("memusers")
//    public ResponseEntity getUsers() {
//        return new ResponseEntity(inMemoryMemberManager, HttpStatus.OK);
//    }
//
//    @GetMapping("app")
//    public ResponseEntity app() {
//        return new ResponseEntity(appConfig, HttpStatus.OK);
//    }


//    @GetMapping("/filteredusers")
//    public Iterable<Audit> getUsersByQuerydslPredicate(@QuerydslPredicate(root = Audit.class) Predicate predicate) {
//        return auditRepository.findAll(predicate);
//    }

}

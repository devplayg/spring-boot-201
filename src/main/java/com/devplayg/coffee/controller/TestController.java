package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.repository.sample.SampleRepository;
import com.devplayg.coffee.repository.sample.SampleRepositorySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("test")
public class TestController {

    private final SampleRepository sampleRepository;

    private final SampleRepositorySupport sampleRepositorySupport;

    public TestController(SampleRepository sampleRepository, SampleRepositorySupport sampleRepositorySupport) {
        this.sampleRepository = sampleRepository;
        this.sampleRepositorySupport = sampleRepositorySupport;
    }

    @GetMapping("auth")
    public Object getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "";
        }

        return auth;
    }

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

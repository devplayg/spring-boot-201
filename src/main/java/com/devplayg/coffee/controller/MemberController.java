package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.MemberRole;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.vo.Result;
import com.devplayg.coffee.vo.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
@Controller
@RequestMapping("members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private List<TimeZone> timezoneList;


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

        // Set roles' owner
        if (member.getRoleList() != null) {
            for (MemberRole role : member.getRoleList()) {
                role.setMember(member);
            }
        }

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
    public void get() {
    }

    @PatchMapping("{id}")
    public void patch() {
    }

    @DeleteMapping("{id}")
    public void delete() {
    }

    @GetMapping("tz")
    public ResponseEntity<?> zone() {
        return new ResponseEntity(timezoneList, HttpStatus.OK);
    }

//    public List<String> getTimeZoneList() {
//        LocalDateTime now = LocalDateTime.now();
//        List<String> list = ZoneId.getAvailableZoneIds().stream()
//                .map(ZoneId::of)
//                .sorted(new ZoneComparator())
//                .map(id -> String.format(
//                        "(UTC%s) %s", getOffset(now, id), id.getId()))
//                .collect(Collectors.toList());
//
//        return list;
//    }
//
//    private class ZoneComparator implements Comparator<ZoneId> {
//
//        @Override
//        public int compare(ZoneId zoneId1, ZoneId zoneId2) {
//            LocalDateTime now = LocalDateTime.now();
//            ZoneOffset offset1 = now.atZone(zoneId1).getOffset();
//            ZoneOffset offset2 = now.atZone(zoneId2).getOffset();
//
//            return offset1.compareTo(offset2);
//        }
//    }
//
//    private String getOffset(LocalDateTime dateTime, ZoneId id) {
//        return dateTime
//                .atZone(id)
//                .getOffset()
//                .getId()
//                .replace("Z", "+00:00");
//    }
}

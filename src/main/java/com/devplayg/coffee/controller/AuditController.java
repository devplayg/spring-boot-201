package com.devplayg.coffee.controller;

/*
 * REST API and method name
 *
 * GET      /audit/     display
 * GET      /audit      list
 */


import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.filter.AuditFilter;
import com.devplayg.coffee.repository.support.AuditRepositorySupport;
import com.devplayg.coffee.vo.Result;
import com.querydsl.core.QueryResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.ZoneId;
import java.util.TimeZone;


@Controller
@Slf4j
@RequestMapping("audit")
public class AuditController {

    @Autowired
    private AuditRepositorySupport auditRepositorySupport;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(@ModelAttribute AuditFilter filter, Model model) {
        this.tune(filter);
        model.addAttribute("filter", filter);
        return "audit/audit";
    }

    @GetMapping
    public ResponseEntity<?> list(@ModelAttribute AuditFilter filter) {
        this.tune(filter);
        if (filter.isFastPaging()) {
//            List list = auditRepositorySupport.searchFast(filter);
//            return new ResponseEntity<>(list, HttpStatus.OK);
        }

        QueryResults result = auditRepositorySupport.search(filter);
        return new ResponseEntity<>(new Result(result.getResults(), result.getTotal()), HttpStatus.OK);
    }

    private void tune(AuditFilter filter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            log.debug("### RequestInterceptor-0: not logged in yet");
            filter.tune(TimeZone.getDefault().toZoneId());
            return;
        }

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        filter.tune(ZoneId.of(member.getTimezone()));
    }


//    Pageable pageable = PageRequest.of(0, 5, Sort.by("name"));
//        return userRepository.findAll(pageable);
}


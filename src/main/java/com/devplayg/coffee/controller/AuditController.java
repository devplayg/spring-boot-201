package com.devplayg.coffee.controller;

/*
 * REST API and method name
 *
 * GET      /audit/     display
 * GET      /audit      list
 */


import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.filter.AuditFilter;
import com.devplayg.coffee.repository.support.AuditRepositorySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("audit")
public class AuditController {

    @Autowired
    private AuditRepositorySupport auditRepositorySupport;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(@ModelAttribute AuditFilter filter, Model model) {
        String tz = "Asia/Taipei";
        filter.check(tz);
        model.addAttribute("filter", filter);
        return "audit/audit";
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Audit> list = auditRepositorySupport.find("-4");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("filter")
    public ResponseEntity<?> filter() {
        AuditFilter filter = new AuditFilter();
//        filter.check("America/Los_Angeles");
        filter.check("Asia/Seoul");
        return new ResponseEntity<>(filter, HttpStatus.OK);
    }
}


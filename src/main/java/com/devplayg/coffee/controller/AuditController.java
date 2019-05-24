package com.devplayg.coffee.controller;

/*
 * REST API and method name
 *
 * GET      /audit/     display
 * GET      /audit      list
 */


import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.repository.AuditRepository;
import com.devplayg.coffee.repository.support.AuditRepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("audit")
public class AuditController {

    @Autowired
    private AuditRepositorySupport auditRepositorySupport;

    @GetMapping("/")
    public String display() {
        return "audit/audit";
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Audit> list = auditRepositorySupport.find("-4");
        return new ResponseEntity<>(list, HttpStatus.OK);


    }
}

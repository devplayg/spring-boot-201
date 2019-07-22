package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.repository.support.audit.AuditRepository;
import com.devplayg.coffee.repository.support.audit.AuditPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Audit controller audits system events.
 */
@Controller
@RequestMapping("audit")
public class AuditController {

    @Autowired
    private AuditRepository auditRepository;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String display(@ModelAttribute AuditFilter filter, Model model) {
        filter.tune();
        model.addAttribute("filter", filter);
        return "audit/audit";
    }

    @GetMapping
    public ResponseEntity<?> findAll(@ModelAttribute AuditFilter filter, Pageable pageable) {
        filter.tune();
        Page<Audit> page = auditRepository.findAll(AuditPredicate.search(filter), pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}


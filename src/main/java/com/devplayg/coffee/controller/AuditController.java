package com.devplayg.coffee.controller;

/*
 * REST API and method name
 *
 * GET      /audit/     display
 * GET      /audit      list
 */


import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.repository.AuditRepository;
import com.devplayg.coffee.repository.support.AuditPredicate;
import lombok.extern.slf4j.Slf4j;
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


@Controller
@Slf4j
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
        log.debug("# filter: {}", filter);
        log.debug("# page: {}", pageable);
        Page<Audit> page = auditRepository.findAll(AuditPredicate.search(filter), pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


//    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
//    public String display(@ModelAttribute AuditFilter filter, Model model) {
//        this.tune(filter);
//        model.addAttribute("filter", filter);
//        return "audit/audit";
//    }
//
//    @GetMapping
//    public ResponseEntity<?> list(@ModelAttribute AuditFilter filter) {
//        this.tune(filter);
//        if (filter.isFastPaging()) {
////            List list = auditRepositorySupport.searchFast(filter);
////            return new ResponseEntity<>(list, HttpStatus.OK);
//        }
//
//        QueryResults result = auditRepositorySupport.search(filter);
//        return new ResponseEntity<>(new Result(result.getResults(), result.getTotal()), HttpStatus.OK);
//    }
//
//    Pageable pageable = PageRequest.of(0, 5, Sort.by("name"));
//        return userRepository.findAll(pageable);
}


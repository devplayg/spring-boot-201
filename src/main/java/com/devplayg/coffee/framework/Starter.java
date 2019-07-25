package com.devplayg.coffee.framework;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Starter {

    /**
     * Audit service
     */
    private final AuditService auditService;

    public Starter(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Audit program start
     */
    @PostConstruct
    public void postConstruct() {
        log.info("### PostConstruct");
        auditService.audit(AuditCategory.APPLICATION_STARTED);
    }
}

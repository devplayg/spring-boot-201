package com.devplayg.coffee.service;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.support.audit.AuditRepository;
import com.devplayg.coffee.util.SubnetUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 감사로그 서비스
 */
@Service
@Slf4j
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private InMemoryMemberManager inMemoryMemberManager;

    public void audit(AuditCategory category, Object message) {
        Audit audit = Audit.builder()
                .category(category)
                .message(this.toJson(message))
                .ip(this.getCurrentIp())
                .member(getCurrentMember())
                .build();
        auditRepository.save(audit);
    }

    public void audit(AuditCategory category) {
        Audit audit = Audit.builder()
                .category(category)
                .ip(this.getCurrentIp())
                .member(getCurrentMember())
                .build();
        auditRepository.save(audit);
    }

    private Member getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return (Member)inMemoryMemberManager.loadUserByUsername(InMemoryMemberManager.adminUsername);
        }

        if (auth instanceof AnonymousAuthenticationToken) {
            return (Member)inMemoryMemberManager.loadUserByUsername(InMemoryMemberManager.adminUsername);
        }

//        if (auth.getPrincipal() instanceof String) {
//            return (Member)inMemoryMemberManager.loadUserByUsername(InMemoryMemberManager.adminUsername);
//        }

        if (auth.getPrincipal() instanceof Member) {
            return (Member)auth.getPrincipal();
        }

        return (Member)inMemoryMemberManager.loadUserByUsername(InMemoryMemberManager.adminUsername);
    }

    private int getCurrentIp() {
        String currentIp = this.getRequestIp();
        SubnetUtils net = new SubnetUtils(currentIp+"/32");
        return net.getInfo().asInteger(currentIp);
    }

    private String getRequestIp() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "127.0.0.1";
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    private String toJson(Object obj) {
        if (obj == null ) {
            return "";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            log.error("{}", e.getMessage());
        }

        return json;
    }
}

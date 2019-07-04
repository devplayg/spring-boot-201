package com.devplayg.coffee.service;

import com.devplayg.coffee.config.MembershipCenter;
import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.AuditRepository;
import com.devplayg.coffee.util.SubnetUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private MembershipCenter membershipCenter;

    public void audit(AuditCategory category, Object message) {
        Audit audit = Audit.builder()
                .category(category)
                .message(this.toJson(message))
                .ip(this.getCurrentIp())
                .member(getCurrentMember())
                .build();
        auditRepository.save(audit);
    }

    public Member getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return membershipCenter.getSystemAccount();
        }

        return (Member)auth.getPrincipal();
    }

    public int getCurrentIp() {
        String currentIp = this.getRequestIp();
        SubnetUtils net = new SubnetUtils(currentIp+"/32");
        return net.getInfo().asInteger(currentIp);
    }

    private String getRequestIp() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    private String toJson(Object obj) {
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

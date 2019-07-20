package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.util.EnumMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.TimeZone;

@Configuration
@Slf4j
public class InitConfig {

    @Autowired
    private AuditService auditService;

    /*
     * Application start
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        auditService.audit(AuditCategory.APPLICATION_STARTED);
    }

    /*
     * Enum mapper
     */
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("role", RoleType.Role.class);
        enumMapper.put("auditCategory", AuditCategory.class);
        return enumMapper;
    }

    /*
     * QueryDSL
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.util.AES256Util;
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
import java.io.UnsupportedEncodingException;

@Configuration
@Slf4j
public class InitConfig {

    /**
     * Enum mapper
     */
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("role", RoleType.Role.class);
        enumMapper.put("auditCategory", AuditCategory.class);
        return enumMapper;
    }

    /**
     * QueryDSL
     */
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public AES256Util aes256Util() {
        try {
            return new AES256Util();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}

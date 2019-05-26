package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.util.EnumMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class InitConfig {

    /*
     * Enum mapper
     */
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("role", RoleType.Role.class);
        //enumMapper.put("auditCategory", AuditCategory.class);
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

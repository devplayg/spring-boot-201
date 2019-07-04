package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.util.EnumMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Hashtable;
import java.util.List;

@Configuration
public class InitConfig {

    @Autowired
    private MemberRepository memberRepository;

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

    /*
     * Membership Center
     */
    @Bean
    public MembershipCenter membershipCenter() {
        Hashtable<Long, Member> membership = new Hashtable<>();
        List<Member> list = memberRepository.findAll();
        for (Member m : list) {
            membership.put(m.getId(), m);
        }
        return new MembershipCenter(membership);
    }
}

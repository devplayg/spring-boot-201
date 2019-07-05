package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import com.devplayg.coffee.service.AuditService;
import com.devplayg.coffee.util.EnumMapper;
import com.devplayg.coffee.vo.MembershipCenter;
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

@Configuration
@Slf4j
public class InitConfig {

    @Autowired
    private AuditService auditService;

    @Autowired
    private MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
                List<Member> list = memberRepository.findAll();
        log.debug("### init list {} ", list);

        MembershipCenter mc = MembershipCenter.getInstance();
        mc.init(list);

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

    /*
     * Membership Center
     */
    @Bean
    public MembershipCenter membershipCenter() {
        return MembershipCenter.getInstance();
    }

//    @Bean
//    public MembershipCenter membershipCenter() {
//        Hashtable<String, UserDetails> membership = new Hashtable<>();
//        Hashtable<String, Boolean> memberNews = new Hashtable<>();
//        List<Member> list = memberRepository.findAll();
//        for (Member m : list) {
//            membership.put(m.getUsername(), m);
//            memberNews.put(m.getUsername(), false);
//        }
//        return new MembershipCenter(membership, memberNews);
//    }
}

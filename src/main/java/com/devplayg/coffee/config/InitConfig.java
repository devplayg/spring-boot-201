package com.devplayg.coffee.config;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.util.AES256Util;
import com.devplayg.coffee.util.EnumMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * Face center
     * 사용자 얼굴 정보를 저장하는 Bean
     */
//    @Bean
//    public FaceCenter faceCenter(FaceUserRepository faceUserRepository) {
//        return new FaceCenter(faceUserRepository);
//    }
    @Bean
    public AES256Util aes256Util() {
        try {
            return new AES256Util();
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}

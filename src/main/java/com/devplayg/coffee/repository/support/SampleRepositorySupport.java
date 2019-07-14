package com.devplayg.coffee.repository.support;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SampleRepositorySupport implements SampleRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Audit> findAll() {
        QAudit audit = QAudit.audit;
        List<Audit > list = queryFactory.selectFrom(audit)
                .fetch();
        return list;
    }
}

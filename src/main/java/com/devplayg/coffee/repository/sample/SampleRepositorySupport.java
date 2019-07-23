package com.devplayg.coffee.repository.sample;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SampleRepositorySupport implements SampleRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Audit> findAll() {
        QAudit audit = QAudit.audit;
        return queryFactory.selectFrom(audit).fetch();
    }
}

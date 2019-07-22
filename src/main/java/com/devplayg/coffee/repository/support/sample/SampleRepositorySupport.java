package com.devplayg.coffee.repository.support.sample;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.repository.support.sample.SampleRepositoryCustom;
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
        List<Audit > list = queryFactory.selectFrom(audit)
                .fetch();
        return list;
    }
}

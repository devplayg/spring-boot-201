package com.devplayg.coffee.repository.support;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public AuditRepositorySupport(JPAQueryFactory queryFactory) {
        super(Audit.class);
        this.queryFactory = queryFactory;
    }

    public List<Audit> find(String name) {
        QAudit audit = QAudit.audit;
        return queryFactory
                .selectFrom(audit)
                .fetch();
    }
}

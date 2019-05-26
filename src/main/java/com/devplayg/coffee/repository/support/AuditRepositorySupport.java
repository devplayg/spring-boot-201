package com.devplayg.coffee.repository.support;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.entity.QAuditDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AuditRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory query;

    public AuditRepositorySupport(JPAQueryFactory queryFactory) {
        super(Audit.class);
        this.query = queryFactory;
    }

    // Tips
    // https://leanpub.com/opinionatedjpa/read

    @Transactional
    public List<Audit> find(String name) {
        QAudit audit = QAudit.audit;
        QAuditDetail auditDetail = QAuditDetail.auditDetail;

//        return from(audit).fetch();
//        QAuditDetail auditDetail = QAuditDetail.auditDetail;
//                return this.query.selectFrom(audit)
//                .leftJoin(user.articles, article)
//                .fetchJoin()
//                .limit(2)
//                .fetch();

        // good
//        return from(audit)
//                .leftJoin(auditDetail)
//                    .on(audit.id.eq(auditDetail.auditID))
//                .distinct()
//                .where(audit.category.eq(AuditCategory.MEMBER))
//                .limit(10).fetch();

//        QueryResults<Audit> list =  query.selectFrom(audit)
//                .where(audit.id.lt(100))
//                .leftJoin(audit.details, auditDetail)
//                .fetchJoin()
//                .offset(0).limit(5)
//                .fetchResults();
//
//        return list.getResults();
//        Query jpaQuery = query.selectFrom(audit)
//                .where(audit.id.lt(100))
//            .leftJoin(audit.details, auditDetail)
//                .fetchJoin()p
//                .offset(0).limit(5)
//                .createQuery();
//        List results = jpaQuery.getResultList();
//        return results;



        return query
                .selectFrom(audit)
                .where(audit.category.eq(AuditCategory.MEMBER))
                .limit(10)
                .fetch();
//
//                .fetchJoin()
//                .offset(0)
//                .limit(5)
//                .fetch();

//                .on(audit.id.eq(auditDetail.auditID))
//                .where(audit.id.lt(10))
//                .limit(10);
//
    }
}

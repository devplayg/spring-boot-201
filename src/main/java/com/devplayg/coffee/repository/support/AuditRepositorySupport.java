package com.devplayg.coffee.repository.support;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.filter.AuditFilter;
import com.devplayg.coffee.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
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
    public List<Audit> find(AuditFilter filter) {
        QAudit audit = QAudit.audit;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(audit.created.between(filter.getStartDate(), filter.getEndDate()));

        if (filter.getCategoryList().size() > 0) {
            builder.and(audit.category.in(filter.getCategoryList()));
        }

        if (!StringUtils.isBlank(filter.getMessage())) {
            builder.and(audit.message.contains(filter.getMessage()));
        }

       Query jpaQuery = query.selectFrom(audit)
                .where(builder)
                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
                .offset(filter.getOffset())
                .limit(filter.getLimit())
               .createQuery();

        List rows = jpaQuery.getResultList();
        return rows;




//        Query jpaQuery = query.selectFrom(audit)
//                .where(audit.id.lt(100))
//            .leftJoin(audit.details, auditDetail)
//                .fetchJoin()p
//                .offset(0).limit(5)
//                .createQuery();
//        List results = jpaQuery.getResultList();



        // Good
//        return query
//                .selectFrom(audit)
//                .where(builder)
//                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
//                .offset(filter.getOffset())
//                .limit(filter.getLimit())
//                .fetch();

    }

    private OrderSpecifier<?> getSortedColumn(Order order, String fieldName){
        Path<Object> fieldPath = Expressions.path(Object.class, QAudit.audit, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }



        // Default
//        return query
//                .selectFrom(audit)
//                .where(audit.category.eq(AuditCategory.MEMBER))
//                .limit(10)
//                .fetch();

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
}

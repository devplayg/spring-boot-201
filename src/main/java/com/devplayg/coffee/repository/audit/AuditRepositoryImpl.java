package com.devplayg.coffee.repository.audit;

// 추후 변경될 방식: https://adrenal.tistory.com/25
// 참고: https://nhnent.dooray.com/share/posts/9YbTE52ER2m9A0sEP881hg

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.util.WebHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditRepositoryImpl extends QuerydslRepositorySupport implements AuditRepositoryCustom {

    private final JPAQueryFactory query;

    public AuditRepositoryImpl(JPAQueryFactory query) {
        super(Audit.class);
        this.query = query;
    }

    @Override
    public List<Audit> find(Predicate predicate, Pageable pageable) {
        QAudit audit = QAudit.audit;

        // Generate orders
        List<OrderSpecifier> orders = WebHelper.getOrders(
                new PathBuilder<>(Audit.class, "audit"),
                pageable,
                audit.id.desc() // default sort and order
        );

        return query.selectFrom(audit)
                .where(predicate)
                .orderBy(orders.toArray(new OrderSpecifier[orders.size()]))
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

//public class AuditRepositoryImpl extends QuerydslRepositorySupport implements AuditRepositoryCustom {
//    @Autowired
//    private JPAQueryFactory query;
//
////    @Override
//    public List<Audit> findAll(Predicate builder, Pageable pageable) {
//        QAudit audit = QAudit.audit;
//
//        PathBuilder<Audit> entityPath = new PathBuilder<>(Audit.class, "audit");
//        OrderSpecifier order;
//        if (pageable.getSort() == Sort.unsorted()) {
//            order = new OrderSpecifier(Order.DESC, audit.id);
//        } else {
//            List<OrderSpecifier> orders = pageable.getSort().stream()
//                    .map(o -> {
//                        PathBuilder<Object> path = entityPath.get(o.getProperty());
//                        return new OrderSpecifier(Order.valueOf(o.getDirection().name()), path);
//                    })
//                    .collect(Collectors.toList());
//            order = orders.get(0);
//
//        }
//
//        return query.selectFrom(audit)
//                .where(builder)
//                .orderBy(order)
//                .offset(pageable.getPageNumber() * pageable.getPageSize())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }
//}

//    public AuditRepositorySupport(JPAQueryFactory queryFactory) {
//        super(Audit.class);
//        this.query = queryFactory;
//    }

//    public List<Audit> search(Predicate builder, Pageable pageable) {
//        QAudit audit = QAudit.audit;
//        QMember member = QMember.member;
//
//        PathBuilder<Audit> entityPath = new PathBuilder<>(Audit.class, "audit");
//        List<OrderSpecifier> orders = pageable.getSort().stream()
//                .map(o -> {
//                    PathBuilder<Object> path = entityPath.get(o.getProperty());
//                    return new OrderSpecifier(Order.valueOf(o.getDirection().name()), path);
//                })
//                .collect(Collectors.toList());
//
//
//        QueryResults<Audit> result = query.select(audit)
//                .from(audit)
//                .leftJoin(member)
//                .on(audit.member.eq(member))
//                .where(builder);
////                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
////                .offset(filter.getOffset())
////                .limit(filter.getLimit())
////                .fetchResults();
//
//
//    }

//    public QueryResults<Audit> search(AuditFilter filter) {
//        QAudit audit = QAudit.audit;
//        QMember member = QMember.member;
//
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(audit.created.between(filter.getStartDate(), filter.getEndDate()));
//
//        if (filter.getCategoryList().size() > 0) {
//            builder.and(audit.category.in(filter.getCategoryList()));
//        }
//
//        if (!StringUtils.isBlank(filter.getMessage())) {
//            builder.and(audit.message.contains(filter.getMessage()));
//        }
//        QueryResults<Audit> result = query.select(audit)
//                .from(audit)
//                .leftJoin(member)
//                .on(audit.member.eq(member))
//                .where(builder)
//                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
//                .offset(filter.getOffset())
//                .limit(filter.getLimit())
//                .fetchResults();
//        return result;
//    }
//
//    // Tips
//    // https://leanpub.com/opinionatedjpa/read
//
//    public List searchFast(AuditFilter filter) {
//        QAudit audit = QAudit.audit;
//        QMember member = QMember.member;
//
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(audit.created.between(filter.getStartDate(), filter.getEndDate()));
//
//        if (filter.getCategoryList().size() > 0) {
//            builder.and(audit.category.in(filter.getCategoryList()));
//        }
//
//        if (!StringUtils.isBlank(filter.getMessage())) {
//            builder.and(audit.message.contains(filter.getMessage()));
//        }
//
//        List<Audit> list = query.select(audit)
//                .from(audit)
//                .leftJoin(member)
//                .on(audit.member.eq(member))
//                .where(builder)
//                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
//                .offset(filter.getOffset())
//                .limit(filter.getLimit())
//                .fetch();
//
//        return list;
////
////        List<Audit> list = query.select(audit)
////                .from(audit)
////                .leftJoin(member)
////                    .on(audit.memberId.eq(member.id))
////                .where(builder)
////                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
////                .offset(filter.getOffset())
////                .limit(filter.getLimit())
////                .fetchJoin()
////                .fetch();
//////
////        query.select(audit, member)
////                .from(audit)
////                .leftJoin(member).on(audit.memberId.eq(member.id))
////                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
////                .offset(filter.getOffset())
////                .limit(filter.getLimit())
////                .where(builder).fetch();
//
////        return new Result(list);
//    }
//
//
//    private OrderSpecifier<?> getSortedColumn(Order order, String fieldName) {
//        Path<Object> fieldPath = Expressions.path(Object.class, QAudit.audit, fieldName);
//        return new OrderSpecifier(order, fieldPath);
//    }
//
//
////        Query jpaQuery = query.selectFrom(audit)
////                .where(audit.id.lt(100))
////            .leftJoin(audit.details, auditDetail)
////                .fetchJoin()
////                .offset(0).limit(5)
////                .createQuery();
////        List results = jpaQuery.getResultList();
//
//
//    // Good
////        return query
////                .selectFrom(audit)
////                .where(builder)
////                .orderBy(this.getSortedColumn(Order.DESC, filter.getSort()))
////                .offset(filter.getOffset())
////                .limit(filter.getLimit())
////                .fetch();
//
//    // Default
////        return query
////                .selectFrom(audit)
////                .where(audit.category.eq(AuditCategory.MEMBER))
////                .limit(10)
////                .fetch();
//
////        return from(audit).fetch();
////        QAuditDetail auditDetail = QAuditDetail.auditDetail;
////                return this.query.selectFrom(audit)
////                .leftJoin(user.articles, article)
////                .fetchJoin()
////                .limit(2)
////                .fetch();
//
//    // good
////        return from(audit)
////                .leftJoin(auditDetail)
////                    .on(audit.id.eq(auditDetail.auditID))
////                .distinct()
////                .where(audit.category.eq(AuditCategory.MEMBER))
////                .limit(10).fetch();
//
////        Query jpaQuery = query.selectFrom(audit)
////                .where(audit.id.lt(100))
////            .leftJoin(audit.details, auditDetail)
////                .fetchJoin()p
////                .offset(0).limit(5)
////                .createQuery();
////        List results = jpaQuery.getResultList();
////        return results;
//}

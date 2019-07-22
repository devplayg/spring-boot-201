package com.devplayg.coffee.repository.support.audit;

import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class AuditPredicate {
    public static Predicate search(AuditFilter filter) {
        QAudit audit = QAudit.audit;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(audit.created.between(filter.getStartDate(), filter.getEndDate()));

        if (filter.getCategoryList().size() > 0) {
            builder.and(audit.category.in(filter.getCategoryList()));
        }

        if (!StringUtils.isBlank(filter.getMessage())) {
            builder.and(audit.message.contains(filter.getMessage()));
        }

        return builder;
    }
}

package com.devplayg.coffee.repository.audit;

import com.devplayg.coffee.entity.QAudit;
import com.devplayg.coffee.entity.filter.AuditFilter;
import com.devplayg.coffee.util.NetworkUtils;
import com.devplayg.coffee.util.StringUtils;
import com.devplayg.coffee.util.SubnetUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class AuditPredicate {
    public static Predicate find(AuditFilter filter) {
        QAudit audit = QAudit.audit;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(audit.created.between(filter.getStartDate(), filter.getEndDate()));

        if (filter.getCategoryList().size() > 0) {
            builder.and(audit.category.in(filter.getCategoryList()));
        }

        if (!StringUtils.isBlank(filter.getMessage())) {
            builder.and(audit.message.contains(filter.getMessage()));
        }

        if (filter.getIp().length()> 0) {
            String ip = filter.getIp();
            if (!ip.contains("/")) {
                ip += "/32";
            }
            SubnetUtils net = new SubnetUtils(ip);
            if (net.getInfo().getAddressCountLong() > 0) {
                long min = NetworkUtils.ipToLong(net.getInfo().getLowAddress());
                long max = NetworkUtils.ipToLong(net.getInfo().getHighAddress());
                builder.and(audit.ip.between(min, max));
            } else {
                builder.and(audit.ip.eq(NetworkUtils.ipToLong(net.getInfo().getAddress())));
            }
        }

        return builder;
    }
}

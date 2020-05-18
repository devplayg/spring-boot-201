package com.devplayg.coffee.repository.factoryevent;

import com.devplayg.coffee.entity.FactoryEvent;
import com.devplayg.coffee.entity.QFactoryEvent;
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
public class FactoryEventRepositoryImpl extends QuerydslRepositorySupport implements FactoryEventRepositoryCustom {

    private final JPAQueryFactory query;

    public FactoryEventRepositoryImpl(JPAQueryFactory query) {
        super(FactoryEvent.class);
        this.query = query;
    }

    @Override
    public List<FactoryEvent> find(Predicate predicate, Pageable pageable) {
        QFactoryEvent factoryEvent = QFactoryEvent.factoryEvent;

        // Generate orders
        List<OrderSpecifier> orders = WebHelper.getOrders(
                new PathBuilder<>(FactoryEvent.class, "factoryEvent"),
                pageable,
                factoryEvent.id.desc() // default sort and order
        );

        return query.selectFrom(factoryEvent)
                .where(predicate)
                .orderBy(orders.toArray(new OrderSpecifier[orders.size()]))
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

package com.devplayg.coffee.repository.factoryevent;

import com.devplayg.coffee.entity.QFactoryEvent;
import com.devplayg.coffee.entity.filter.FactoryEventFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class FactoryEventPredicate {
    public static Predicate find(FactoryEventFilter filter) {
        QFactoryEvent factoryEvent = QFactoryEvent.factoryEvent;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(factoryEvent.date.between(filter.getLocalizedStartdate(), filter.getLocalizedEndDate()));

        if (filter.getFactoryIdList().size() > 0) {
            builder.and(factoryEvent.factoryId.in(filter.getFactoryIdList()));
        }

        if (filter.getCameraList().size() > 0) {
            builder.and(factoryEvent.cameraId.in(filter.getCameraList()));
        }

        if (filter.getEventTypeList().size() > 0) {
            builder.and(factoryEvent.eventType.in(filter.getEventTypeList()));
        }

        if (filter.getHour() > -1) {
            builder.and(factoryEvent.date.hour().eq(filter.getHour()));
        }

        return builder;
    }
}

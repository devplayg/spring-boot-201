package com.devplayg.coffee.repository.factoryevent;

import com.devplayg.coffee.entity.FactoryEvent;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface FactoryEventRepositoryCustom {
    List<FactoryEvent> find(Predicate builder, Pageable pageable);
}

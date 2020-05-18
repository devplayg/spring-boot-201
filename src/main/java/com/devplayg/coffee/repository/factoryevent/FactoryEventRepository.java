package com.devplayg.coffee.repository.factoryevent;

import com.devplayg.coffee.entity.FactoryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface FactoryEventRepository extends JpaRepository<FactoryEvent, Long>, QuerydslPredicateExecutor<FactoryEvent> {
    List<FactoryEvent> findTop20ByIdGreaterThanAndDateAfterOrderByIdDesc(long id, LocalDateTime date);

    List<FactoryEvent> findTop20ByIdGreaterThanAndDateAfterAndFactoryIdInOrderByIdDesc(long id, LocalDateTime date, List<Long> factoryIdList);

    List<FactoryEvent> findTop20ByDateGreaterThanEqualOrderByIdDesc(LocalDateTime date);

    List<FactoryEvent> findByDateBetweenOrderByDateDesc(LocalDateTime startDate, LocalDateTime endDate);
}

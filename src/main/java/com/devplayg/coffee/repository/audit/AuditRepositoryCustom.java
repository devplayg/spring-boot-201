package com.devplayg.coffee.repository.audit;

import com.devplayg.coffee.entity.Audit;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditRepositoryCustom {
    List<Audit> find(Predicate builder, Pageable pageable);
}

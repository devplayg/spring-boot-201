package com.devplayg.coffee.repository.audit;

import com.devplayg.coffee.entity.Audit;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AuditRepositoryCustom {
    List<Audit> find(Predicate builder, Pageable pageable);
}

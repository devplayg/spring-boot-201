package com.devplayg.coffee.repository.audit;

import com.devplayg.coffee.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AuditRepository extends JpaRepository<Audit, Long>, QuerydslPredicateExecutor<Audit> {
}

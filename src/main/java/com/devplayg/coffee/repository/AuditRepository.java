package com.devplayg.coffee.repository;

import com.devplayg.coffee.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}

//@Repository
//public interface AuditRepository extends PagingAndSortingRepository<Audit, Long>, QuerydslPredicateExecutor<Audit> {
//
//}
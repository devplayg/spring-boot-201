package com.devplayg.coffee.repository;

import com.devplayg.coffee.entity.AuditDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditDetailRepository extends JpaRepository<AuditDetail, Long> {
}

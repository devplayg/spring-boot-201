package com.devplayg.coffee.repository;

import com.devplayg.coffee.entity.Audit;
import com.devplayg.coffee.repository.support.SampleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Audit, Long>, SampleRepositoryCustom {
}

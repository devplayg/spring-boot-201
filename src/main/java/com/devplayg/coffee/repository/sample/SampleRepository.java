package com.devplayg.coffee.repository.sample;

import com.devplayg.coffee.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Audit, Long> {
}

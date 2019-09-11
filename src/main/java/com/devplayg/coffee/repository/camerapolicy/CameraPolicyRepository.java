package com.devplayg.coffee.repository.camerapolicy;

import com.devplayg.coffee.entity.CameraPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CameraPolicyRepository extends JpaRepository<CameraPolicy, Long>, QuerydslPredicateExecutor<CameraPolicy> {
    CameraPolicy findOneByCode(String code);
    CameraPolicy findByDeviceId(long deviceId);
}

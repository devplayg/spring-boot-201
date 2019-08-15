package com.devplayg.coffee.repository.camera;

import com.devplayg.coffee.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CameraRepository extends JpaRepository<Device, Long>, QuerydslPredicateExecutor<Device> {
    List<Device> findAllByAssetId(long assetId);
    Device findOneByAssetIdAndUid(long assetId, String uid);
}


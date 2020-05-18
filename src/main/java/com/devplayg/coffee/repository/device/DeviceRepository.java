package com.devplayg.coffee.repository.device;

import com.devplayg.coffee.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long>, QuerydslPredicateExecutor<Device> {
    List<Device> findByCategoryOrderByNameAsc(int category);

    List<Device> findByCategoryOrderByDeviceIdAsc(int category);

    List<Device> findByCategoryAndTypeOrderByNameAsc(int category, int type);

    Optional<Device> findByAssetIdAndCategoryAndType(long assetId, int category, int type);

    List<Device> findByAssetIdAndCategoryOrderByNameAsc(long assetId, int category);

    List<Device> findByAssetIdAndCategoryOrderByDeviceIdAsc(long assetId, int category);

    Device findByCategoryAndType(int category, int type);

}

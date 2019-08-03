package com.devplayg.coffee.repository.device;

import com.devplayg.coffee.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByCategory(int category);
    List<Device> findByCategoryAndType(int category, int type);
}

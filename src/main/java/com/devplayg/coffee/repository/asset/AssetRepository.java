package com.devplayg.coffee.repository.asset;

import com.devplayg.coffee.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long>, QuerydslPredicateExecutor<Asset> {
    List<Asset> findAllByOrderByTypeAscNameAsc();
}

package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.entity.RegionGwpMonthly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccumulatedMonthlyRegionRepo extends JpaRepository<RegionGwpMonthly, Long> {
    Optional<RegionGwpMonthly> findByRegionCodeAndYearAndMonth(String regionCode, int year, int month);
}

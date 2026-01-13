package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.dto.region.RegionsWithGWPDTO;
import com.dulara.figure_controller.entity.RegionGwpDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccumulatedAndCurrentMysqlRepoRegion extends JpaRepository<RegionGwpDaily, Long> {
    void deleteBySnapshotDate(LocalDate snapshotDate);

    @Query("SELECT new com.dulara.figure_controller.dto.region.RegionsWithGWPDTO (r.regionCode, r.regionName, r.currentMonthGwp, r.accumulatedGwp) FROM RegionGwpDaily r WHERE r.snapshotDate = :today")
    List<RegionsWithGWPDTO> getDailyRegionGWP(LocalDate today);
}

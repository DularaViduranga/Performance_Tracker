package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.branch.DailyBranchGWPDTO;
import com.dulara.figure_controller.dto.region.RegionsWithGWPDTO;
import com.dulara.figure_controller.entity.BranchGwpDaily;
import com.dulara.figure_controller.entity.RegionGwpDaily;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyBranchRepo;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyRepoRegion;
import com.dulara.figure_controller.repository.oracle.AcumiliatedAndCurrentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class GwpDailySyncService {
    private final AcumiliatedAndCurrentRepo oracleRepo;
    private final AccumulatedDailyBranchRepo mysqlRepo;
    private final AccumulatedDailyRepoRegion mysqlRepoRegion;


    @Scheduled(cron = "0 24 12 * * ?") // Runs every day at 3 AM
    @Transactional
    public void syncDailyGwp(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        mysqlRepo.deleteBySnapshotDate(yesterday);

        List<DailyBranchGWPDTO> dailyGwpData = oracleRepo.getDailyBranchGWP();

        List<BranchGwpDaily> gwpEntities = dailyGwpData.stream()
                .map(dto -> new BranchGwpDaily(
                        null,
                        dto.getBranchCode(),
                        dto.getBranchName(),
                        dto.getCurrentMonthGwp(),
                        dto.getAccumulatedGwp(),
                        today
                ))
                .toList();

        mysqlRepo.saveAll(gwpEntities);
    }


    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 3 AM
    @Transactional
    public void syncDailyRegionGwp(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        mysqlRepoRegion.deleteBySnapshotDate(yesterday);

        List<RegionsWithGWPDTO> dailyGwpData = oracleRepo.getDailyRegionGWP();

        List<RegionGwpDaily> gwpEntities = dailyGwpData.stream()
                .map(dto -> new RegionGwpDaily(
                        null,
                        dto.getRegionCode(),
                        dto.getRegionName(),
                        dto.getCurrentMonthGwp(),
                        dto.getAccumulatedGwp(),
                        today
                ))
                .toList();

        mysqlRepoRegion.saveAll(gwpEntities);
    }



}

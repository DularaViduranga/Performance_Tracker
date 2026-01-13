package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.entity.BranchGwpDaily;
import com.dulara.figure_controller.entity.BranchGwpMonthly;
import com.dulara.figure_controller.entity.RegionGwpDaily;
import com.dulara.figure_controller.entity.RegionGwpMonthly;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyBranchRepo;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyRepoRegion;
import com.dulara.figure_controller.repository.mysql.AccumulatedMonthlyBranchRepo;
import com.dulara.figure_controller.repository.mysql.AccumulatedMonthlyRegionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class GwpMonthlySyncService {
    private final AccumulatedDailyRepoRegion dailyRepoRegion;
    private final AccumulatedMonthlyRegionRepo monthlyRepoRegion;
    private final AccumulatedDailyBranchRepo dailyBranchRepo;
    private final AccumulatedMonthlyBranchRepo monthlyBranchRepo;

    @Scheduled(cron = "0 2 9 * * ?")
    public void syncMonthlyRegionGWP(){
        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();

        List<RegionGwpDaily> dailyGwpData = dailyRepoRegion.findAll();

        for (RegionGwpDaily daily : dailyGwpData) {

            RegionGwpMonthly monthly =
                    monthlyRepoRegion
                            .findByRegionCodeAndYearAndMonth(
                                    daily.getRegionCode(),
                                    year,
                                    month
                            )
                            .orElseGet(() -> {
                                RegionGwpMonthly m = new RegionGwpMonthly();
                                m.setRegionCode(daily.getRegionCode());
                                m.setYear(year);
                                m.setMonth(month);
                                m.setMonthlyGwp(daily.getCurrentMonthGwp());
                                m.setLastUpdated(today);
                                return m;
                            });

            // update every day
            monthly.setMonthlyGwp(daily.getCurrentMonthGwp());
            monthly.setLastUpdated(today);

            monthlyRepoRegion.save(monthly);
        }
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void syncMonthlyBranchGWP(){
        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();

        List<BranchGwpDaily> dailyGwpData = dailyBranchRepo.findAll();

        for (BranchGwpDaily daily : dailyGwpData) {

            BranchGwpMonthly monthly =
                    monthlyBranchRepo
                            .findByBranchCodeAndYearAndMonth(
                                    daily.getBranchCode(),
                                    year,
                                    month
                            )
                            .orElseGet(() -> {
                                BranchGwpMonthly m = new BranchGwpMonthly();
                                m.setBranchCode(daily.getBranchCode());
                                m.setYear(year);
                                m.setMonth(month);
                                m.setMonthlyGwp(daily.getCurrentMonthGwp());
                                m.setLastUpdated(today);
                                return m;
                            });

            // update every day
            monthly.setMonthlyGwp(daily.getCurrentMonthGwp());
            monthly.setLastUpdated(today);

            monthlyBranchRepo.save(monthly);
        }
    }


}

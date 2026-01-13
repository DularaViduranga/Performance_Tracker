package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.entity.RegionGwpDaily;
import com.dulara.figure_controller.entity.RegionGwpMonthly;
import com.dulara.figure_controller.repository.mysql.AccumulatedAndCurrentMysqlRepoRegion;
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
    private final AccumulatedAndCurrentMysqlRepoRegion dailyRepoRegion;
    private final AccumulatedMonthlyRegionRepo monthlyRepoRegion;

    @Scheduled(cron = "0 33 16 * * ?")
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


}

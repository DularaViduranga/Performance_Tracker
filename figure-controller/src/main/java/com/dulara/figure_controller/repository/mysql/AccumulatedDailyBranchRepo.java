package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.dto.branch.DailyBranchGWPDTO;
import com.dulara.figure_controller.dto.branch.GetAllBranchesFromDaily;
import com.dulara.figure_controller.entity.BranchGwpDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccumulatedDailyBranchRepo extends JpaRepository<BranchGwpDaily, Long> {
    void deleteBySnapshotDate(LocalDate snapshotDate);

    List<BranchGwpDaily> findByBranchCodeIn(List<String> branchCodes);

    @Query("SELECT new com.dulara.figure_controller.dto.branch.GetAllBranchesFromDaily(b.branchCode, b.branchName) " +
            "FROM BranchGwpDaily b")
    List<GetAllBranchesFromDaily> findAllBranches();


//    @Query("SELECT new com.dulara.figure_controller.dto.branch.DailyBranchGWPDTO(" +
//            "b.branchCode, b.branchName, b.accumulatedGwp) " +
//            "FROM BranchGwpDaily b " +
//            "ORDER BY b.accumulatedGwp DESC")
    List<BranchGwpDaily> findTop10ByOrderByAccumulatedGwpDesc();

    BranchGwpDaily findByBranchCode(String branchCode);
}

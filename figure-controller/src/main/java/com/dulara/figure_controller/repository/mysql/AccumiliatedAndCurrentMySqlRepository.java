package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.entity.BranchGwpDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccumiliatedAndCurrentMySqlRepository extends JpaRepository<BranchGwpDaily, Long> {
    void deleteBySnapshotDate(LocalDate snapshotDate);

    List<BranchGwpDaily> findByBranchCodeIn(List<String> branchCodes);

}

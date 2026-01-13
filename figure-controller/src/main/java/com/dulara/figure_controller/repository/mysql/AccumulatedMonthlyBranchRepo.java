package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.entity.BranchGwpMonthly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccumulatedMonthlyBranchRepo extends JpaRepository<BranchGwpMonthly,Long> {
    Optional<BranchGwpMonthly> findByBranchCodeAndYearAndMonth(String branchCode, int year, int month);

    List<BranchGwpMonthly> findByBranchCodeAndYear(String branchCode, int year);
}

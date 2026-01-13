package com.dulara.figure_controller.service;

import com.dulara.figure_controller.dto.branch.*;
import com.dulara.figure_controller.dto.myFigure.MyGWPResponseDTO;
import com.dulara.figure_controller.dto.myFigure.MyPerformanceResponseDTO;
import com.dulara.figure_controller.dto.region.MonthWiseRegionGwpDTO;

import java.util.List;

public interface BranchService {
    BranchCreateResponseDTO saveBranch(BranchCreateRequestDTO branchCreateRequestDTO);

    List<GetBranchesDTO> getAllBranches();

    List<GetBranchesDTO> getBranchesByRegionId(Long id);

    BranchCreateResponseDTO updateBranch(Long id, BranchUpdateRequestDTO branchUpdateRequestDTO);

    List<String>getBranch1();

    List<String>getBranch();

    List<MyGWPResponseDTO> getBranchGWP(String branchCode, String start, String end);

    List<BranchCashCollectionDTO> getBranchCashCollection(String branchCode, String start, String end);

    List<BranchRenewalResponseDTO> getBranchRenewal(String start, String end, String branchCode);

    List<BranchCancellationResponseDTO> getBranchCancellation(String start, String end, String branchCode);

    List<MyPerformanceResponseDTO> getBranchPerformance(String start, String end, String branchCode);

    List<MonthWiseRegionGwpDTO> getMonthWiseBranchGwp(String branchCode, int year);

    List<GetAllBranchesFromDaily> getAllBranchesFromDaily();

    List<DailyBranchGWPDTO> getTop10AccumulatedBranchesFromDaily();
}

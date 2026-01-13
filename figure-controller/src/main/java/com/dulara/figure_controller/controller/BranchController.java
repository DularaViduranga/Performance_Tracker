package com.dulara.figure_controller.controller;


import com.dulara.figure_controller.dto.branch.*;
import com.dulara.figure_controller.dto.myFigure.MyGWPResponseDTO;
import com.dulara.figure_controller.dto.myFigure.MyPerformanceResponseDTO;
import com.dulara.figure_controller.dto.region.MonthWiseRegionGwpDTO;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyBranchRepo;
import com.dulara.figure_controller.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
  
@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {
    private final BranchService branchService;
    private final AccumulatedDailyBranchRepo accumiliatedAndCurrentMySqlRepository;

    public BranchController(BranchService branchService, AccumulatedDailyBranchRepo accumiliatedAndCurrentMySqlRepository) {
        this.branchService = branchService;
        this.accumiliatedAndCurrentMySqlRepository = accumiliatedAndCurrentMySqlRepository;
    }

    @PostMapping("/saveBranch")
    public ResponseEntity<BranchCreateResponseDTO> saveBranch(@RequestBody BranchCreateRequestDTO branchCreateRequestDTO) {
        BranchCreateResponseDTO response = branchService.saveBranch(branchCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getAllBranches")
    public ResponseEntity<List<GetBranchesDTO>> getAllBranches() {
        List<GetBranchesDTO> response = branchService.getAllBranches();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getBranchByRegionId/{id}")
    public ResponseEntity<List<GetBranchesDTO>> getBranchByRegionId(@PathVariable Long id) {
        List<GetBranchesDTO> response = branchService.getBranchesByRegionId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateBranch/{id}")
    public ResponseEntity<BranchCreateResponseDTO> updateBranch(@PathVariable Long id, @RequestBody BranchUpdateRequestDTO branchUpdateRequestDTO) {
        BranchCreateResponseDTO response = branchService.updateBranch(id, branchUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }
//    @GetMapping("/getBranchName")
//    public ResponseEntity<List<String>> getBranch() {
//        List<String> response = branchService.getBranch();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/getBranchName1")
//    public ResponseEntity<List<String>> getBranch1() {
//        List<String> response = branchService.getBranch1();
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/branchGWP")
    public ResponseEntity<List<MyGWPResponseDTO>> getBranchGWP(@RequestParam String branchCode,
                                                               @RequestParam String start,
                                                               @RequestParam String end) {
        List<MyGWPResponseDTO> response = branchService.getBranchGWP(branchCode, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branchCashCollection")
    public ResponseEntity<List<BranchCashCollectionDTO>> getBranchCashCollection(@RequestParam String branchCode,
                                                                                 @RequestParam String start,
                                                                                 @RequestParam String end) {
        List<BranchCashCollectionDTO> response = branchService.getBranchCashCollection(branchCode, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branchRenewal")
    public ResponseEntity<List<BranchRenewalResponseDTO>> getBranchRenewal(@RequestParam String start,
                                                                           @RequestParam String end,
                                                                           @RequestParam String branchCode) {
        List<BranchRenewalResponseDTO> response = branchService.getBranchRenewal(start,end,branchCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branchCancellation")
    public ResponseEntity<List<BranchCancellationResponseDTO>> getBranchCancellation(@RequestParam String start,
                                                                                @RequestParam String end,
                                                                                @RequestParam String branchCode) {
        List<BranchCancellationResponseDTO> response = branchService.getBranchCancellation(start,end,branchCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/branchPerformance")
    public ResponseEntity<List<MyPerformanceResponseDTO>> getBranchPerformance(@RequestParam String start,
                                                                               @RequestParam String end,
                                                                               @RequestParam String branchCode) {
        List<MyPerformanceResponseDTO> response = branchService.getBranchPerformance(start,end,branchCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMonthWiseBranchGwp")
    public ResponseEntity<List<MonthWiseRegionGwpDTO>> getMonthWiseBranchGwp(@RequestParam String branchCode,
                                                                             @RequestParam int year) {
        List<MonthWiseRegionGwpDTO> response = branchService.getMonthWiseBranchGwp(branchCode,year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllBranchesFromDaily")
    public ResponseEntity<List<GetAllBranchesFromDaily>> getAllBranchesFromDaily() {
        List<GetAllBranchesFromDaily> response = branchService.getAllBranchesFromDaily();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getTop10AccumulatedBranchesFromDaily")
    public ResponseEntity<List<DailyBranchGWPDTO>> getTop10AccumulatedBranchesFromDaily() {
        List<DailyBranchGWPDTO> response = branchService.getTop10AccumulatedBranchesFromDaily();
        return ResponseEntity.ok(response);
    }


}

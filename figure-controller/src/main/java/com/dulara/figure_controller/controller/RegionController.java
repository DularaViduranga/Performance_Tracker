package com.dulara.figure_controller.controller;

import com.dulara.figure_controller.dto.branch.DailyBranchGWPDTO;
import com.dulara.figure_controller.dto.region.*;
import com.dulara.figure_controller.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping("/saveRegion")
    public ResponseEntity<RegionCreateResponseDTO> saveRegion(@RequestBody RegionCreateRequestDTO regionCreateRequestDTO) {
        RegionCreateResponseDTO response = regionService.saveRegion(regionCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllRegions")
    public ResponseEntity<List<GetRegionsDTO>> getAllRegions() {
        List<GetRegionsDTO> response = regionService.getAllRegions();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateRegion/{id}")
    public ResponseEntity<RegionCreateResponseDTO> updateRegion(@PathVariable Long id, @RequestBody RegionCreateRequestDTO regionCreateRequestDTO) {
        RegionCreateResponseDTO response = regionService.updateRegion(id, regionCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getBranchesByRegion")
    public ResponseEntity<List<BranchesByRegionDTO>> getBranchesByRegion(@RequestParam String regionCode) {
        List<BranchesByRegionDTO> response = regionService.getBranchesByRegion(regionCode);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getRegionsWithGWP")
    public ResponseEntity<List<RegionsWithGWPDTO>> getRegionsWithGWP() {
        List<RegionsWithGWPDTO> response = regionService.getRegionsWithGWP();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMonthWiseRegionGwp")
    public ResponseEntity<List<MonthWiseRegionGwpDTO>> getMonthWiseRegionGwp(@RequestParam String regionCode,
                                                                             @RequestParam int year) {
        List<MonthWiseRegionGwpDTO> response = regionService.getMonthWiseRegionGwp(regionCode,year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCurrentAccumulatedGwp")
    public ResponseEntity<BigDecimal> getCurrentAccumulatedGwp() {
        BigDecimal response = regionService.getCurrentAccumulatedGwp();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTop3AccumulatedRegionsFromDaily")
    public ResponseEntity<List<RegionsWithGWPDTO>> getTop10AccumulatedRegionsFromDaily() {
        List<RegionsWithGWPDTO> response = regionService.getTop3AccumulatedRegionsFromDaily();
        return ResponseEntity.ok(response);
    }
}

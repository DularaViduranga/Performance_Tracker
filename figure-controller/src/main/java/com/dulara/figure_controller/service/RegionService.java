package com.dulara.figure_controller.service;

import com.dulara.figure_controller.dto.region.*;

import java.math.BigDecimal;
import java.util.List;

public interface RegionService {
    RegionCreateResponseDTO saveRegion(RegionCreateRequestDTO regionCreateRequestDTO);

    List<GetRegionsDTO> getAllRegions();

    RegionCreateResponseDTO updateRegion(Long id, RegionCreateRequestDTO regionCreateRequestDTO);

    List<BranchesByRegionDTO> getBranchesByRegion(String regionCode);

    List<RegionsWithGWPDTO> getRegionsWithGWP();

    List<MonthWiseRegionGwpDTO> getMonthWiseRegionGwp(String regionCode, int year);

    BigDecimal getCurrentAccumulatedGwp();

    List<RegionsWithGWPDTO> getTop3AccumulatedRegionsFromDaily();
}

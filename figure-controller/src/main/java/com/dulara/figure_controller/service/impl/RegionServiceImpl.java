package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.region.*;
import com.dulara.figure_controller.entity.BranchGwpDaily;
import com.dulara.figure_controller.entity.RegionEntity;
import com.dulara.figure_controller.repository.mysql.AccumiliatedAndCurrentMySqlRepository;
import com.dulara.figure_controller.repository.mysql.AccumulatedAndCurrentMysqlRepoRegion;
import com.dulara.figure_controller.repository.mysql.RegionRepository;
import com.dulara.figure_controller.repository.oracle.MyFiguresRepository;
import com.dulara.figure_controller.service.RegionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final MyFiguresRepository myFiguresRepository;
    private final AccumiliatedAndCurrentMySqlRepository accumulatedRepo;
    private final AccumulatedAndCurrentMysqlRepoRegion accumulatedAndCurrentMysqlRepoRegion;

    public RegionServiceImpl(RegionRepository regionRepository, MyFiguresRepository myFiguresRepository, AccumiliatedAndCurrentMySqlRepository accumulatedRepo, AccumulatedAndCurrentMysqlRepoRegion accumulatedAndCurrentMysqlRepoRegion) {
        this.regionRepository = regionRepository;
        this.myFiguresRepository = myFiguresRepository;
        this.accumulatedRepo = accumulatedRepo;
        this.accumulatedAndCurrentMysqlRepoRegion = accumulatedAndCurrentMysqlRepoRegion;
    }


    @Override
    public RegionCreateResponseDTO saveRegion(RegionCreateRequestDTO regionCreateRequestDTO) {
        if(regionCreateRequestDTO.getName() == null || regionCreateRequestDTO.getCode() == null){
            throw new IllegalArgumentException("Region name and code must not be null");
        }
        if(regionRepository.existsByName(regionCreateRequestDTO.getName().toUpperCase())){
            throw new IllegalArgumentException("Region with name " + regionCreateRequestDTO.getName() + " already exists");
        }

        RegionEntity regionEntity = new RegionEntity(
                regionCreateRequestDTO.getName().toUpperCase(),
                regionCreateRequestDTO.getCode()
        );

        regionRepository.save(regionEntity);

        return new RegionCreateResponseDTO("Region created successfully",null);
    }

    @Override
    public List<GetRegionsDTO> getAllRegions() {
        List<RegionEntity> regionEntities = regionRepository.findAll();
        return regionEntities.stream()
                .map(region -> new GetRegionsDTO(region.getId(), region.getName(), region.getCode()))
                .toList();
    }

    @Override
    public RegionCreateResponseDTO updateRegion(Long id, RegionCreateRequestDTO regionCreateRequestDTO) {
        RegionEntity regionEntity = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region with id " + id + " not found"));

        if(regionCreateRequestDTO.getName() != null && regionCreateRequestDTO.getCode() != null){
            if(regionRepository.existsByName(regionCreateRequestDTO.getName().toUpperCase())){
                throw new IllegalArgumentException("Region with name " + regionCreateRequestDTO.getName() + " already exists");
            }else{
                regionEntity.setName(regionCreateRequestDTO.getName().toUpperCase());
                regionEntity.setCode(regionCreateRequestDTO.getCode());
            }
        }
        regionRepository.save(regionEntity);

        return new RegionCreateResponseDTO("Region updated successfully", null);
    }

    @Override
    public List<BranchesByRegionDTO> getBranchesByRegion(String regionCode) {
        List<Map<String,Object>> branches = myFiguresRepository.findBranchesByRegionCode(regionCode);

        if (branches == null || branches.isEmpty()) {
            return List.of();
        }

        // 1. Extract branch codes
        List<String> branchCodes = branches.stream()
                .map(b -> (String) b.get("BRN_CODE"))
                .toList();

        // 2. Fetch ALL GWP records in ONE query
        List<BranchGwpDaily> gwpList =
                accumulatedRepo.findByBranchCodeIn(branchCodes);


        Map<String, BranchGwpDaily> gwpMap = gwpList.stream()
                .collect(Collectors.toMap(
                        BranchGwpDaily::getBranchCode,
                        g -> g
                ));

        // 4. Merge data
        return branches.stream()
                .map(branch -> {
                    String code = (String) branch.get("BRN_CODE");
                    BranchGwpDaily gwp = gwpMap.get(code);

                    return new BranchesByRegionDTO(
                            code,
                            (String) branch.get("BRN_NAME"),
                            (String) branch.get("REGION_NAME"),
                            gwp != null ? gwp.getCurrentMonthGwp() : BigDecimal.valueOf(0.0),
                            gwp != null ? gwp.getAccumulatedGwp() : BigDecimal.valueOf(0.0)
                    );
                })
                .toList();
    }

    @Override
    public List<RegionsWithGWPDTO> getRegionsWithGWP() {
        LocalDate today = LocalDate.now();
        return accumulatedAndCurrentMysqlRepoRegion.getDailyRegionGWP(today);
    }


}

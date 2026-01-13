package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dbConnection.JDBC;
import com.dulara.figure_controller.dto.branch.*;
import com.dulara.figure_controller.dto.myFigure.MyGWPResponseDTO;
import com.dulara.figure_controller.dto.myFigure.MyPerformanceResponseDTO;
import com.dulara.figure_controller.dto.region.MonthWiseRegionGwpDTO;
import com.dulara.figure_controller.entity.*;
import com.dulara.figure_controller.repository.mysql.AccumulatedDailyBranchRepo;
import com.dulara.figure_controller.repository.mysql.AccumulatedMonthlyBranchRepo;
import com.dulara.figure_controller.repository.mysql.BranchRepository;
import com.dulara.figure_controller.repository.mysql.RegionRepository;
import com.dulara.figure_controller.repository.oracle.MyFiguresRepository;
import com.dulara.figure_controller.service.BranchService;
import com.dulara.figure_controller.utils.dateConverter;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final RegionRepository regionRepository;
    private final MyFiguresRepository myFiguresRepository;
    private final AccumulatedMonthlyBranchRepo monthlyBranchRepo;
    private final AccumulatedDailyBranchRepo dailyBranchRepo;


    public BranchServiceImpl(BranchRepository branchRepository, RegionRepository regionRepository, MyFiguresRepository myFiguresRepository, AccumulatedMonthlyBranchRepo monthlyBranchRepo, AccumulatedDailyBranchRepo dailyBranchRepo) {
        this.regionRepository = regionRepository;
        this.branchRepository = branchRepository;
        this.myFiguresRepository = myFiguresRepository;
        this.monthlyBranchRepo = monthlyBranchRepo;
        this.dailyBranchRepo = dailyBranchRepo;
    }

    @Override
    public BranchCreateResponseDTO saveBranch(BranchCreateRequestDTO branchCreateRequestDTO) {
        RegionEntity regionEntity = regionRepository.findById(branchCreateRequestDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Region with id " + branchCreateRequestDTO.getRegionId() + " not found"));

        BranchEntity branchEntity = null;
        if (branchCreateRequestDTO.getName() != null && branchCreateRequestDTO.getCode() != null) {
            if (branchRepository.existsByName(branchCreateRequestDTO.getName().toUpperCase())) {
                throw new IllegalArgumentException("Branch with name " + branchCreateRequestDTO.getName() + " already exists");
            } else {
                branchEntity = new BranchEntity();
                branchEntity.setName(branchCreateRequestDTO.getName().toUpperCase());
                branchEntity.setCode(branchCreateRequestDTO.getCode());
                branchEntity.setRegion(regionEntity);
            }
        }
        branchRepository.save(branchEntity);

        return new BranchCreateResponseDTO("Branch created successfully", null);
    }

    @Override
    public List<GetBranchesDTO> getAllBranches() {
        List<BranchEntity> branchEntities = branchRepository.findAll();
        return branchEntities.stream()
                .map(branch -> new GetBranchesDTO(
                        branch.getId(),
                        branch.getName(),
                        branch.getCode()
                ))
                .toList();
    }

    @Override
    public List<GetBranchesDTO> getBranchesByRegionId(Long id) {
        List<BranchEntity> branchEntities = branchRepository.findByBranchByRegionId(id);
        return branchEntities.stream()
                .map(branch -> new GetBranchesDTO(
                        branch.getId(),
                        branch.getName(),
                        branch.getCode()
                ))
                .toList();
    }

    @Override
    public BranchCreateResponseDTO updateBranch(Long id, BranchUpdateRequestDTO branchUpdateRequestDTO) {
        BranchEntity branchEntity = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch with id " + id + " not found"));

        if (branchUpdateRequestDTO.getName() != null && branchUpdateRequestDTO.getCode() != null) {
            if (branchRepository.existsByName(branchUpdateRequestDTO.getName().toUpperCase())) {
                throw new IllegalArgumentException("Branch with name " + branchUpdateRequestDTO.getName() + " already exists");
            } else {
                branchEntity.setName(branchUpdateRequestDTO.getName().toUpperCase());
                branchEntity.setCode(branchUpdateRequestDTO.getCode());
            }
        }
        branchRepository.save(branchEntity);

        return new BranchCreateResponseDTO("Branch updated successfully", null);
    }

    @Override
    public List<String> getBranch1() {
        try{
            List<String> d = new ArrayList<>();
            Connection co = JDBC.con();

            Statement stmt = co.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT DISTINCT S.SOF_SFC_CODE," +
                            "       sicl.pk_sm_m_sales_force.fn_get_name(S.SOF_SFC_CODE) AS SFC_NAME," +
                            "       S.SOF_BRN_CODE " +
                            "FROM SM_M_SALES_OFFICER S " +
                            "WHERE S.SOF_BRN_CODE = 'CPOF'" +
                            "ORDER BY S.SOF_SFC_CODE"
            );

            while (rs.next()) {
                String sfc = rs.getString(1);
                String name = rs.getString(2);
                String bran = rs.getString(3);

                System.out.println(sfc + " → " + name+ " → " + bran);
                d.add(sfc + "." + name+ "." + bran);
            }

            return d;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


//        ResultSet rs = stmt.executeQuery(
//                    "select BBT_YEAR,BBT_MONTH,BBT_TOT_VALUE,BBT_BRN_CODE,BBT_STY_CODE " +
//                            "from SICL.sm_T_BRA_Budget " +
//                            "where BBT_TAR_PERSON_CODE='M8155' and BBT_YEAR='2025' and BBT_MONTH='11'"
//            );            while (rs.next()) {
//                d.add(rs.getString("BBT_YEAR")+"  "+rs.getString("BBT_MONTH")+"  "+rs.getString("BBT_TOT_VALUE")+"  "+rs.getString("BBT_BRN_CODE")+"  "+rs.getString("BBT_STY_CODE"));
//            }


    @Override
    public List<String> getBranch() {
        List<String> d = new ArrayList<>();

        String query = "SELECT DISTINCT S2.SOF_SFC_CODE FROM SM_M_OVERRIDERS O2 " +
                "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE=S2.SOF_CODE " +
                "WHERE O2.OVR_SFC_CODE=? AND SOF_SFC_CODE<>? " +
                "AND O2.OVR_VALID_STOP BETWEEN ? AND ? AND O2.OVR_VALID_START<=? " +
                "UNION ALL " +
                "SELECT DISTINCT S2.SOF_SFC_CODE FROM SM_M_OVERRIDERS O2 " +
                "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE=S2.SOF_CODE " +
                "WHERE O2.OVR_SFC_CODE=? AND SOF_SFC_CODE<>? " +
                "AND O2.OVR_VALID_START<=? AND O2.OVR_VALID_STOP IS NULL " +
                "UNION ALL " +
                "SELECT DISTINCT S2.SOF_SFC_CODE FROM SM_M_OVERRIDERS O2 " +
                "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE=S2.SOF_CODE " +
                "WHERE O2.OVR_SFC_CODE=? AND SOF_SFC_CODE<>? AND O2.OVR_VALID_STOP>=? " +
                "UNION ALL " +
                "SELECT DISTINCT O.OVR_SFC_CODE FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
                "WHERE SOF_CODE=OVR_OVRRIDER_CODE AND S.SOF_SFC_CODE=? " +
                "AND O.OVR_SFC_CODE<>? AND O.OVR_VALID_STOP BETWEEN ? AND ? AND O.OVR_VALID_START<=? " +
                "UNION ALL " +
                "SELECT DISTINCT O.OVR_SFC_CODE FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
                "WHERE SOF_CODE=OVR_OVRRIDER_CODE AND S.SOF_SFC_CODE=? " +
                "AND O.OVR_SFC_CODE<>? AND O.OVR_VALID_START<=? AND O.OVR_VALID_STOP IS NULL " +
                "UNION ALL " +
                "SELECT DISTINCT O.OVR_SFC_CODE FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
                "WHERE SOF_CODE=OVR_OVRRIDER_CODE AND S.SOF_SFC_CODE=? " +
                "AND O.OVR_SFC_CODE<>? AND O.OVR_VALID_STOP>?";

        try (Connection co = JDBC.con();
             PreparedStatement stmt = co.prepareStatement(query)) {

            // Set parameters for first UNION query
            stmt.setString(1, "F8858");          // OVR_SFC_CODE
            stmt.setString(2, "F8858");          // SOF_SFC_CODE exclusion
            stmt.setDate(3, Date.valueOf("2024-01-01"));  // OVR_VALID_STOP lower bound
            stmt.setDate(4, Date.valueOf("2024-12-31"));    // OVR_VALID_STOP upper bound
            stmt.setDate(5, Date.valueOf("2024-12-31"));   // OVR_VALID_START check

            // Set parameters for second UNION query M5428
            stmt.setString(6, "F8858");          // OVR_SFC_CODE
            stmt.setString(7, "F8858");          // SOF_SFC_CODE exclusion
            stmt.setDate(8, Date.valueOf("2024-12-31"));    // OVR_VALID_START check

            // Set parameters for third UNION query
            stmt.setString(9, "F8858");          // OVR_SFC_CODE
            stmt.setString(10, "F8858");         // SOF_SFC_CODE exclusion
            stmt.setDate(11,Date.valueOf("2024-12-31"));   // OVR_VALID_STOP check

            // Set parameters for fourth UNION query
            stmt.setString(12, "F8858");         // SOF_SFC_CODE
            stmt.setString(13, "F8858");         // OVR_SFC_CODE exclusion
            stmt.setDate(14,Date.valueOf("2024-01-01")); // OVR_VALID_STOP lower bound
            stmt.setDate(15,Date.valueOf("2024-12-31"));   // OVR_VALID_STOP upper bound
            stmt.setDate(16,Date.valueOf("2024-12-31"));   // OVR_VALID_START check

            // Set parameters for fifth UNION query
            stmt.setString(17, "F8858");         // SOF_SFC_CODE
            stmt.setString(18, "F8858");         // OVR_SFC_CODE exclusion
            stmt.setDate(19,Date.valueOf("2024-12-31"));   // OVR_VALID_START check

            // Set parameters for sixth UNION query
            stmt.setString(20, "F8858");         // SOF_SFC_CODE
            stmt.setString(21, "F8858");         // OVR_SFC_CODE exclusion
            stmt.setDate(22,Date.valueOf("2024-12-31"));   // OVR_VALID_STOP check
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    d.add(rs.getString(1));
                }
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<MyGWPResponseDTO> getBranchGWP(String branchCode, String start, String end) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getBranchGWPData(branchCode, formattedStart, formattedEnd);
        List<MyGWPResponseDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            MyGWPResponseDTO dto = new MyGWPResponseDTO();
            dto.setPolicyNumber((String) row.get("GLT_POLICY_NO"));
            dto.setBasic((Double) row.get("PREMIUM"));
            dto.setSrcc((Double) row.get("SRCC"));
            dto.setTc((Double) row.get("TC"));
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public List<BranchCashCollectionDTO> getBranchCashCollection(String branchCode, String start, String end) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getBranchCashCollection(branchCode, formattedStart, formattedEnd);
        List<BranchCashCollectionDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            String tranType = String.valueOf(row.get("DEB_TRAN_TYPE"));

            String type = switch (tranType) {
                case "R" -> "Renewals";
                case "A" -> "Additions";
                case "N" -> "New";
                case "S" -> "Special";
                case "F" -> "Refund";
                default -> "UNKNOWN TYPE";
            };

            BranchCashCollectionDTO dto = new BranchCashCollectionDTO();
            dto.setCusName((String) row.get("CUS_NAME"));
            dto.setTranType(type);
            dto.setSettledAmount((Double) row.get("SETTLED_AMOUNT"));
            dto.setDebPolicyNo((String) row.get("DEB_POLICY_NO"));
            dto.setIntermediaryName((String) row.get("INTER_DESC"));
            dto.setDebClaCode((String) row.get("DEB_CLA_CODE"));
            dto.setProDesc((String) row.get("PRO_DESC"));
            dtoList.add(dto);
        }
        return dtoList;
    }

//    @Override
//    public List<BranchRenewalResponseDTO> getBranchRenewal(String start, String end, String branchCode) {
//        String formattedStart = dateConverter.toOracleFormat(start);
//        String formattedEnd = dateConverter.toOracleFormat(end);
//
//        List<Map<String, Object>> rawData = myFiguresRepository.getBranchRenewal(formattedStart, formattedEnd, branchCode);
//        List<BranchRenewalResponseDTO> dtoList = new ArrayList<>();
//
//        for (Map<String, Object> row : rawData) {
//            BranchRenewalResponseDTO dto = new BranchRenewalResponseDTO();
//            dto.setPolicyNo((String) row.get("POLICY_NO"));
//            dto.setPhone(String.valueOf(row.get("Phone")));
//            dto.setClassDesc((String) row.get("CLASS_DESC"));
//            dto.setProdDesc((String) row.get("PRODUCT_DESC"));
//            dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
//            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
//            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
//            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
//            dto.setIntermediaryName((String) row.get("INTMDRY_NAME"));
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }


    public List<BranchRenewalResponseDTO> getBranchRenewal(String start, String end, String branchCode) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getBranchRenewal(formattedStart, formattedEnd, branchCode);

        return rawData.parallelStream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private BranchRenewalResponseDTO mapToDTO(Map<String, Object> row) {
        BranchRenewalResponseDTO dto = new BranchRenewalResponseDTO();
        dto.setPolicyNo((String) row.get("POLICY_NO"));
        dto.setPhone(String.valueOf(row.get("Phone")));
        dto.setClassDesc((String) row.get("CLASS_DESC"));
        dto.setProdDesc((String) row.get("PRODUCT_DESC"));
        dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
        dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
        dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
        dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
        dto.setIntermediaryName((String) row.get("INTMDRY_NAME"));
        return dto;
    }

    @Override
    public List<BranchCancellationResponseDTO> getBranchCancellation(String start, String end, String branchCode) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getBranchCancellation(formattedStart, formattedEnd, branchCode);
        List<BranchCancellationResponseDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            BranchCancellationResponseDTO dto = new BranchCancellationResponseDTO();
            dto.setPolicyNo((String) row.get("POLICY_NO"));
            dto.setClassDesc((String) row.get("CLASS_DESC"));
            dto.setProdDesc((String) row.get("PRODUCT_DESC"));
            dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
            dto.setPhoneNumber((String) row.get("Phone"));
            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
            dto.setIntermediaryName((String) row.get("INTMDRY_NAME"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<MyPerformanceResponseDTO> getBranchPerformance(String start, String end, String branchCode) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);
        System.out.println("Formatted Start Date: " + formattedStart);
        System.out.println("Formatted End Date: " + formattedEnd);

        List<Map<String, Object>> rawData = myFiguresRepository.getClassPerformanceDataForPeriodForBranch(branchCode, formattedStart, formattedEnd);
        List<MyPerformanceResponseDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> data : rawData) {
            String classCode = (String) data.get("classCode");
            double target = (Double) data.get("target");
            double achieved = (Double) data.get("achieved");

            dtoList.add(new MyPerformanceResponseDTO(classCode, target, achieved));
        }
        return dtoList;
    }

    @Override
    public List<MonthWiseRegionGwpDTO> getMonthWiseBranchGwp(String branchCode, int year) {
        List<BranchGwpMonthly> branchGwpMonthlies = monthlyBranchRepo.findByBranchCodeAndYear(branchCode,year);

        return branchGwpMonthlies.stream()
                .map(region -> new MonthWiseRegionGwpDTO(region.getMonth(), region.getMonthlyGwp()))
                .toList();
    }

    @Override
    public List<GetAllBranchesFromDaily> getAllBranchesFromDaily() {
        return dailyBranchRepo.findAllBranches();    }

    @Override
    public List<DailyBranchGWPDTO> getTop10AccumulatedBranchesFromDaily() {
        List<BranchGwpDaily> topBranches = dailyBranchRepo.findTop10ByOrderByAccumulatedGwpDesc();

        return topBranches.stream()
                .map(b -> new DailyBranchGWPDTO(
                        b.getBranchCode(),
                        b.getBranchName(),
                        b.getCurrentMonthGwp(),
                        b.getAccumulatedGwp()
                ))
                .collect(Collectors.toList());
    }


}

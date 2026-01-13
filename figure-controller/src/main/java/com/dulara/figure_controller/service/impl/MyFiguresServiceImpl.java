package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.myFigure.*;
import com.dulara.figure_controller.repository.oracle.MyFiguresRepository;
import com.dulara.figure_controller.service.MyFiguresService;

import com.dulara.figure_controller.utils.dateConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyFiguresServiceImpl implements MyFiguresService {
    private final MyFiguresRepository myFiguresRepository;

    public MyFiguresServiceImpl(MyFiguresRepository myFiguresRepository) {
        this.myFiguresRepository = myFiguresRepository;
    }

    @Override
    public List<MyGWPResponseDTO> getMyGWP(String intermidiaryCode, String start, String end) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);

        return myFiguresRepository.getGWPData(
                intermidiaryCode,
                formattedStart,
                formattedEnd
        );
    }

    @Override
    public List<MyGWPDetailedResponseDTO> getMyGWPDetailed(String intermidiaryCode, String start, String end, String productCode, String claCode) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getGWPDetailedData(intermidiaryCode, formattedStart, formattedEnd,productCode,claCode);
        List<MyGWPDetailedResponseDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            MyGWPDetailedResponseDTO dto = new MyGWPDetailedResponseDTO();
            dto.setPolicyNumber((String) row.get("GLT_POLICY_NO"));
            dto.setProductCode((String) row.get("GLT_PRODUCT"));
            dto.setCustomerName((String) row.get("CUS_NAME"));
            dto.setReferenceNumber((String) row.get("GLT_REF_2"));
            dto.setBranchCode((String) row.get("GLT_SEG_4"));
            dto.setPolicyStartDate((String) row.get("DEB_POL_PERIOD_FROM"));
            dto.setPolicyEndDate((String) row.get("DEB_POL_PERIOD_TO"));
            dto.setTransactionType((String) row.get("DEB_TRAN_TYPE"));
            dto.setIssuedDate((String) row.get("GLT_REF_DATE"));
            dto.setSumInsured((Double) row.get("DEB_SUM_INSURED"));
            dto.setCreatedDate((String) row.get("DEB_TRN_DATE"));
            dto.setProductDescription((String) row.get("CLA_DESCRIPTION"));
            dto.setBasic((Double) row.get("PREMIUM"));
            dto.setSrcc((Double) row.get("SRCC"));
            dto.setTc((Double) row.get("TC"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<MyPerformanceResponseDTO> getClassPerformanceSummary(String intermediaryCode, String year, String month) {
        List<Map<String, Object>> rawData = myFiguresRepository.getClassPerformanceData(intermediaryCode, year, month);
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
    public List<MyPerformanceResponseDTO> getClassPerformanceSummaryPeriod(String intermediaryCode, String start, String end) {
        String formattedStart = dateConverter.toOracleFormat(start);
        String formattedEnd = dateConverter.toOracleFormat(end);
        System.out.println("Formatted Start Date: " + formattedStart);
        System.out.println("Formatted End Date: " + formattedEnd);

        List<Map<String, Object>> rawData = myFiguresRepository.getClassPerformanceDataForPeriod(intermediaryCode, formattedStart, formattedEnd);
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
    public List<MyCashCollectionDTO> getMyCashCollection(String intermediaryCode, String start, String end) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getCashCollection(intermediaryCode, formattedStart, formattedEnd);
        List<MyCashCollectionDTO> dtoList = new ArrayList<>();

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

            MyCashCollectionDTO dto = new MyCashCollectionDTO();
            dto.setBranchName((String) row.get("BRANCH"));
            dto.setCusName((String) row.get("CUS_NAME"));
            dto.setTranType(type);
            dto.setSettledAmount((Double) row.get("SETTLED_AMOUNT"));
            dto.setDebPolicyNo((String) row.get("DEB_POLICY_NO"));
            dto.setDebClaCode((String) row.get("DEB_CLA_CODE"));
            dto.setProDesc((String) row.get("PRO_DESC"));
            dtoList.add(dto);
        }
        return dtoList;

    }

    @Override
    public List<MyNewBuisnessDTO> getMyNewBusiness(String start, String end, String intermediaryCode) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getNewBusiness(formattedStart, formattedEnd, intermediaryCode);
        List<MyNewBuisnessDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {

            String statusCode = String.valueOf(row.get("STATUS"));

            String status = switch (statusCode) {
                case "0" -> "POLICY UN-AUTHORIZED";
                case "1" -> "POLICY EXAMINED";
                case "2" -> "POLICY FINAL AUTHORIZED";
                case "3" -> "POLICY REINSURANCE AUTHORIZED";
                case "4" -> "POLICY PAYMENT AUTHORIZED";
                case "5" -> "POLICY COVER NOTE";
                case "6" -> "POLICY SCHEDULE";
                case "8" -> "POLICY CLOSED";
                case "9" -> "POLICY CANCELLED";
                case "10" -> "POLICY QUOTATION RENEWED";
                default -> "UNKNOWN STATUS";
            };

            MyNewBuisnessDTO dto = new MyNewBuisnessDTO();
            dto.setPolicyNo((String) row.get("POLICY_NO"));
            dto.setClassDesc((String) row.get("CLASS_DESC"));
            dto.setProdDesc((String) row.get("PRODUCT_DESC"));
            dto.setCustomerName(String.valueOf(row.get("CUSTOMER_DESC")));
            dto.setPhoneNumber(String.valueOf(row.get("Phone")));
            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
            dto.setBranchName((String) row.get("BRANCH_NAME"));
            dto.setTransactionDesc((String) row.get("TRANSACTION_DESC"));
            dto.setStatus(status);

            dtoList.add(dto);
        }

        return dtoList;
    }


    @Override
    public List<MyNewBuisnessCountsByTransactionTypesDTO> getMyNewBusinessCountByTranType(String start, String end, String intermediaryCode) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getNewBusinessCountByTranType(formattedStart, formattedEnd, intermediaryCode);
        List<MyNewBuisnessCountsByTransactionTypesDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            MyNewBuisnessCountsByTransactionTypesDTO dto = new MyNewBuisnessCountsByTransactionTypesDTO();
            dto.setType((String) row.get("TRANSACTION_DESC"));
            dto.setCount(Integer.parseInt(String.valueOf(row.get("CNT"))));
            dtoList.add(dto);
        }

        return dtoList;

    }

    @Override
    public List<MyRenewalResponseDTO> getMyRenewal(String start, String end, String intermediaryCode) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getRenewal(formattedStart, formattedEnd, intermediaryCode);
        List<MyRenewalResponseDTO> dtoList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            MyRenewalResponseDTO dto = new MyRenewalResponseDTO();
            dto.setPolicyNo((String) row.get("POLICY_NO"));
            dto.setPhone(String.valueOf(row.get("Phone")));
            dto.setClassDesc((String) row.get("CLASS_DESC"));
            dto.setProdDesc((String) row.get("PRODUCT_DESC"));
            dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
            dto.setBranchName((String) row.get("BRANCH_NAME"));
            dtoList.add(dto);
        }

        return dtoList;

    }

    @Override
    public List<MyCancellationResponseDTO> getMyCancellation(String start, String end, String intermediaryCode) {
        String formattedStart = dateConverter.toIsoFormat(start);
        String formattedEnd = dateConverter.toIsoFormat(end);

        List<Map<String, Object>> rawData = myFiguresRepository.getCancellation(formattedStart, formattedEnd, intermediaryCode);
        List<MyCancellationResponseDTO> dtoList = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            MyCancellationResponseDTO dto = new MyCancellationResponseDTO();
            dto.setPolicyNo((String) row.get("POLICY_NO"));
            dto.setClassDesc((String) row.get("CLASS_DESC"));
            dto.setProdDesc((String) row.get("PRODUCT_DESC"));
            dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
            dto.setPhoneNumber(String.valueOf(row.get("Phone")));
            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
            dto.setBranchName((String) row.get("BRANCH_NAME"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<MyOutstandingResponseDTO> getMyOutstanding(String intermediaryCode) {
        List<Map<String, Object>> rawData = myFiguresRepository.getOutstanding(intermediaryCode);
        List<MyOutstandingResponseDTO> dtoList = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            String statusCode = String.valueOf(row.get("status"));

            String status = switch (statusCode) {
                case "0" -> "POLICY UN-AUTHORIZED";
                case "1" -> "POLICY EXAMINED";
                case "2" -> "POLICY FINAL AUTHORIZED";
                case "3" -> "POLICY REINSURANCE AUTHORIZED";
                case "4" -> "POLICY PAYMENT AUTHORIZED";
                case "5" -> "POLICY COVER NOTE";
                case "6" -> "POLICY SCHEDULE";
                case "8" -> "POLICY CLOSED";
                case "9" -> "POLICY CANCELLED";
                case "10" -> "POLICY QUOTATION RENEWED";
                default -> "UNKNOWN STATUS";
            };

            MyOutstandingResponseDTO dto = new MyOutstandingResponseDTO();
            dto.setPolicyNo((String) row.get("POLICY_NO"));
            dto.setClassDesc((String) row.get("CLASS_DESC"));
            dto.setProductDesc((String) row.get("PRODUCT_DESC"));
            dto.setCustomerName((String) row.get("CUSTOMER_DESC"));
            dto.setPhone(String.valueOf(row.get("Phone")));
            dto.setCreatedDate(String.valueOf(row.get("CREATED_DATE")));
            dto.setPeriodFrom(String.valueOf(row.get("PERIOD_FROM")));
            dto.setPeriodTo(String.valueOf(row.get("PERIOD_TO")));
            dto.setBranchName((String) row.get("BRANCH_NAME"));
            dto.setStatus(status);
            dto.setDebTotalAmount((Double) row.get("DEB_TOTAL_AMOUNT"));
            dto.setDebTotalSettled((Double) row.get("DEB_TOTAL_SETTLED"));
            dto.setDebAutoCanDayParam((String) row.get("DEB_AUTO_CAN_DAY_PARAM"));
            dto.setTransactionDesc((String) row.get("TRANSACTION_DESC"));
            dtoList.add(dto);
        }
        return dtoList;
    }



}

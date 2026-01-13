package com.dulara.figure_controller.service;

import com.dulara.figure_controller.dto.myFigure.*;

import java.util.List;

public interface MyFiguresService {
    List<MyGWPResponseDTO> getMyGWP(String intermidiaryCode, String start, String end);

    List<MyGWPDetailedResponseDTO> getMyGWPDetailed(String intermidiaryCode, String start, String end, String productCode, String claCode);

    List<MyPerformanceResponseDTO> getClassPerformanceSummary(String intermediaryCode, String year, String month);

    List<MyCashCollectionDTO> getMyCashCollection(String intermediaryCode, String start, String end);

    List<MyNewBuisnessDTO> getMyNewBusiness(String start, String end, String intermediaryCode);

    List<MyRenewalResponseDTO> getMyRenewal(String start, String end, String intermediaryCode);

    List<MyCancellationResponseDTO> getMyCancellation(String start, String end, String intermediaryCode);

    List<MyNewBuisnessCountsByTransactionTypesDTO> getMyNewBusinessCountByTranType(String start, String end, String intermediaryCode);

    List<MyOutstandingResponseDTO> getMyOutstanding(String intermediaryCode);

    List<MyPerformanceResponseDTO> getClassPerformanceSummaryPeriod(String intermediaryCode, String start, String end);
}

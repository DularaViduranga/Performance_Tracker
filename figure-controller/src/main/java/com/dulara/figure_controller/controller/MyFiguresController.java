package com.dulara.figure_controller.controller;

import com.dulara.figure_controller.dto.myFigure.*;
import com.dulara.figure_controller.service.MyFiguresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/myFigures")
public class MyFiguresController {
    private final MyFiguresService myFiguresService;

    public MyFiguresController(MyFiguresService myFiguresService) {
        this.myFiguresService = myFiguresService;
    }

    @GetMapping("/myGWP")
    public ResponseEntity<List<MyGWPResponseDTO>> getMyGWP( @RequestParam String intermediaryCode,
                                                            @RequestParam String start,
                                                            @RequestParam String end) {
        List<MyGWPResponseDTO> response = myFiguresService.getMyGWP(intermediaryCode, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myGWPDetailed")
    public ResponseEntity<List<MyGWPDetailedResponseDTO>> getMyGWPDetailed(@RequestParam String intermediaryCode,
                                                                           @RequestParam String start,
                                                                           @RequestParam String end,
                                                                           @RequestParam String productCode,
                                                                           @RequestParam String classCode) {
        List<MyGWPDetailedResponseDTO> response = myFiguresService.getMyGWPDetailed(intermediaryCode, start, end,productCode, classCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/performanceByClass")
    public ResponseEntity<List<MyPerformanceResponseDTO>> getPerformance(
            @RequestParam String intermediaryCode,
            @RequestParam String year,
            @RequestParam String month) {

        List<MyPerformanceResponseDTO> performance = myFiguresService.getClassPerformanceSummary(intermediaryCode, year, month);

        return ResponseEntity.ok(performance);
    }

    @GetMapping("/performanceByClassPeriod")
    public ResponseEntity<List<MyPerformanceResponseDTO>> getPerformanceByPeriod(
            @RequestParam String intermediaryCode,
            @RequestParam String start,
            @RequestParam String end) {
        System.out.println("Intermediary Code: " + intermediaryCode);
        System.out.println("Start Date: " + start);
        System.out.println("End Date: " + end);
        List<MyPerformanceResponseDTO> performance = myFiguresService.getClassPerformanceSummaryPeriod(intermediaryCode, start, end);

        return ResponseEntity.ok(performance);
    }

    @GetMapping("/myCashCollection")
    public ResponseEntity<List<MyCashCollectionDTO>> getMyCashCollection(@RequestParam String intermediaryCode,
                                                                         @RequestParam String start,
                                                                         @RequestParam String end) {
        List<MyCashCollectionDTO> response = myFiguresService.getMyCashCollection(intermediaryCode, start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myNewBusiness")
    public ResponseEntity<List<MyNewBuisnessDTO>> getMyNewBusiness(@RequestParam String start,
                                                                   @RequestParam String end,
                                                                   @RequestParam String intermediaryCode) {
        List<MyNewBuisnessDTO> response = myFiguresService.getMyNewBusiness(start,end,intermediaryCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myNewBusinessCountByTranType")
    public ResponseEntity<List<MyNewBuisnessCountsByTransactionTypesDTO>> getMyNewBusinessCountByTranType(@RequestParam String start,
                                                                   @RequestParam String end,
                                                                   @RequestParam String intermediaryCode) {
        List<MyNewBuisnessCountsByTransactionTypesDTO> response = myFiguresService.getMyNewBusinessCountByTranType(start,end,intermediaryCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myRenewal")
    public ResponseEntity<List<MyRenewalResponseDTO>> getMyRenewal(@RequestParam String start,
                                                                   @RequestParam String end,
                                                                   @RequestParam String intermediaryCode) {
        List<MyRenewalResponseDTO> response = myFiguresService.getMyRenewal(start,end,intermediaryCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myCancellation")
    public ResponseEntity<List<MyCancellationResponseDTO>> getMyCancellation(@RequestParam String start,
                                                                   @RequestParam String end,
                                                                   @RequestParam String intermediaryCode) {
        List<MyCancellationResponseDTO> response = myFiguresService.getMyCancellation(start,end,intermediaryCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myOutstanding")
    public ResponseEntity<List<MyOutstandingResponseDTO>> getMyOutstanding(@RequestParam String intermediaryCode) {
        List<MyOutstandingResponseDTO> response = myFiguresService.getMyOutstanding(intermediaryCode);
        return ResponseEntity.ok(response);
    }



}

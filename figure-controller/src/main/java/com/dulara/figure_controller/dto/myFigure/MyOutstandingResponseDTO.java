package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOutstandingResponseDTO {
    private String policyNo;
    private String classDesc;
    private String productDesc;
    private String customerName;
    private String phone;
    private String createdDate;
    private String periodFrom;
    private String periodTo;
    private String branchName;
    private String status;
    private Double debTotalAmount;
    private Double debTotalSettled;
    private String debAutoCanDayParam;
    private String transactionDesc;
}

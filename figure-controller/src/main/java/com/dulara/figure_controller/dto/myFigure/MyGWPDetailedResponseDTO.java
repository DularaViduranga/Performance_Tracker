package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyGWPDetailedResponseDTO {
    private String policyNumber;
    private String productCode;
    private String customerName;
    private String referenceNumber;
    private String branchCode;
    private String policyStartDate;
    private String policyEndDate;
    private String transactionType;
    private String issuedDate;
    private Double sumInsured;
    private String createdDate;
    private String productDescription;
    private Double basic;
    private Double srcc;
    private Double tc;
}

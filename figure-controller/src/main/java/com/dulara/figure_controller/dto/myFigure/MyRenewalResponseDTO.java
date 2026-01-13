package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRenewalResponseDTO {
    private String policyNo;
    private String phone;
    private String classDesc;
    private String prodDesc;
    private String customerName;
    private String createdDate;
    private String periodFrom;
    private String periodTo;
    private String branchName;
}

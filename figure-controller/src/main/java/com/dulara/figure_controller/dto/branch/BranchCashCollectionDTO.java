package com.dulara.figure_controller.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchCashCollectionDTO {
    private String cusName;
    private String tranType;
    private Double settledAmount;
    private String debPolicyNo;
    private String intermediaryName;
    private String debClaCode;
    private String proDesc;
}

package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCashCollectionDTO {
    private String branchName;
    private String cusName;
    private String tranType;
    private Double settledAmount;
    private String debPolicyNo;
    private String debClaCode;
    private String proDesc;
}

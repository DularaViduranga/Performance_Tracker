package com.dulara.figure_controller.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchCancellationResponseDTO {
    private String policyNo;
    private String classDesc;
    private String prodDesc;
    private String intermediaryName;
    private String customerName;
    private String phoneNumber;
    private String createdDate;
    private String periodFrom;
    private String periodTo;
}

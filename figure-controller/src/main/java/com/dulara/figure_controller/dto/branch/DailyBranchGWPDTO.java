package com.dulara.figure_controller.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBranchGWPDTO {
    private String branchCode;
    private BigDecimal currentMonthGwp;
    private BigDecimal accumulatedGwp;
}

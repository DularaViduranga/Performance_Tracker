package com.dulara.figure_controller.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchesByRegionDTO {
    private String brnCode;
    private String brnName;
    private String regionName;
    private BigDecimal currentMonthGwp;
    private BigDecimal accumulatedGwp;
}

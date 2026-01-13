package com.dulara.figure_controller.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthWiseRegionGwpDTO {
    private int month;
    private BigDecimal monthlyGwp;
}

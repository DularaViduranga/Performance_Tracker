package com.dulara.figure_controller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "region_gwp_monthly",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"region_code", "year", "month"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionGwpMonthly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regionCode;
    private Integer year;   // 2026
    private Integer month;  // 1 - 12

    private BigDecimal monthlyGwp;

    private LocalDate lastUpdated;
}

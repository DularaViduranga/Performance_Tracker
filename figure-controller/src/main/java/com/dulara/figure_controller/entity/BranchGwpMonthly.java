package com.dulara.figure_controller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "branch_gwp_monthly",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"branch_code", "year", "month"}))
public class BranchGwpMonthly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @Column(nullable = false)
    private Integer year;   // 2026

    @Column(nullable = false)
    private Integer month;  // 1 - 12

    @Column(nullable = false, precision = 24, scale = 2)
    private BigDecimal monthlyGwp;

    private LocalDate lastUpdated;
}

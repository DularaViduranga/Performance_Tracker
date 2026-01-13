package com.dulara.figure_controller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "branch_gwp_daily")
public class BranchGwpDaily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String branchCode;

    private BigDecimal currentMonthGwp;
    private BigDecimal accumulatedGwp;

    private LocalDate snapshotDate;

}

package com.dulara.figure_controller.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "branches")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id", unique = true)
    @JsonIgnoreProperties({"region", "branch"})
    private UserEntity branchManager;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"region", "branch"})
    private List<UserEntity> userEntities;
}

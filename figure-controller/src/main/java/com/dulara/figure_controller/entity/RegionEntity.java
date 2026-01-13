package com.dulara.figure_controller.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "regions")
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // One region has one RM (UserEntity)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rm_id", unique = true)
    @JsonIgnoreProperties({"region", "branch"})
    private UserEntity regionalManager;

    // One region can have many branches
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference // the "parent" side of Region → Branch
    private List<BranchEntity> branches;

    // Constructor without ID (for new entities)
    public RegionEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

}

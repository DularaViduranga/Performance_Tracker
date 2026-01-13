package com.dulara.figure_controller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "non_sales_users")
public class NonSalesUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(length = 255)
    private String sfcCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column( length = 255)
    private String branchCode;

    @Column( length = 255)
    private String branchName;

    @Column( length = 255)
    private String regionCode;

    @Column( length = 255)
    private String regionName;

}

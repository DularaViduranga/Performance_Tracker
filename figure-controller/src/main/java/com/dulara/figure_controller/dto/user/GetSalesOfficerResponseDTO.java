package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSalesOfficerResponseDTO {
    private Long id;
    private String userName;
    private String branchName;
    private String regionName;
}

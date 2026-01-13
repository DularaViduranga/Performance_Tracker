package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBMsResponseDTO {
    private Long id;
    private String username;
    private String branchName;
    private String regionName;
}

package com.dulara.figure_controller.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchUpdateRequestDTO {
    private String name;
    private String code;
}

package com.dulara.figure_controller.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionCreateRequestDTO {
    private String name;
    private String code;
}

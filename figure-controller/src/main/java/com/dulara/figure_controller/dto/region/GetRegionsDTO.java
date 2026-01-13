package com.dulara.figure_controller.dto.region;

import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NamedEntityGraph
public class GetRegionsDTO {
    private Long id;
    private String name;
    private String code;
}

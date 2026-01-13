package com.dulara.figure_controller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private String prdCode;
    private String prdDescription;
}

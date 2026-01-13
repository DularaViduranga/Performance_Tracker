package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyGWPResponseDTO {
    private String policyNumber;
    private Double basic;
    private Double srcc;
    private Double tc;
}

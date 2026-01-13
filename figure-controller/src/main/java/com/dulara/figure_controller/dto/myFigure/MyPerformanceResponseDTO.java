package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPerformanceResponseDTO {
    private String classCode;
    private double target;
    private double achieved;
    private double percentage;

    public MyPerformanceResponseDTO(String classCode, double target, double achieved) {
        this.classCode = classCode;
        this.target = target;
        this.achieved = achieved;
        this.percentage = target > 0 ? (achieved / target) * 100 : 0;
    }
}

package com.dulara.figure_controller.dto.myFigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyNewBuisnessCountsByTransactionTypesDTO {
    private String type;
    private int count;
}

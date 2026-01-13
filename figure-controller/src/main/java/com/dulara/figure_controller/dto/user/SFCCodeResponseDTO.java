package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SFCCodeResponseDTO {
    private String sfcCode;
    private String sfcName;
    private String brnName;
}

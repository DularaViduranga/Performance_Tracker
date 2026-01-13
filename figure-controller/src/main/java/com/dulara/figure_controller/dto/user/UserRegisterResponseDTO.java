package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDTO {
    private String message;
    private String error;
}

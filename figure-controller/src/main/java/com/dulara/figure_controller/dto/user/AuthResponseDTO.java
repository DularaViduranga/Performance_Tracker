package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String username;
    private String epf;
    private String level;
    private String location;
    private String company;
    private String dpt;
    private String empType;
    private String token; // we will ignore this
}

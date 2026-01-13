package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserLoginResponseDTO {
    private String token;
    private String username;
    private String empType;
    private String intermediaryCode;
    private String role;
    private String branchCode;// can be null or empty for some users
    private String regionCode;      // can be null or empty for some users
    private String branchName;// can be null or empty for some users
    private String regionName;      // can be null or empty for some users
}

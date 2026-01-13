package com.dulara.figure_controller.dto.user;

import com.dulara.figure_controller.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private LocalDateTime loginTime;
    private Role role;
    private Long branchId;
    private Long regionId;

    public LoginResponseDTO(String token, LocalDateTime now, String name, Long branchId, Long regionId) {
        this.token = token;
        this.loginTime = now;
        this.role = Role.valueOf(name);
        this.branchId = branchId;
        this.regionId = regionId;
    }
}

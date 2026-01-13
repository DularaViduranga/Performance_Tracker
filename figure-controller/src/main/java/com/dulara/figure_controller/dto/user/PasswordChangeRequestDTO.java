package com.dulara.figure_controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequestDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}

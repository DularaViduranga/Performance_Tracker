package com.dulara.figure_controller.service;

import com.dulara.figure_controller.dto.user.AllUserLoginResponseDTO;
import com.dulara.figure_controller.dto.user.LoginRequestDTO;

public interface AuthService {
    AllUserLoginResponseDTO login(LoginRequestDTO loginRequest);
}

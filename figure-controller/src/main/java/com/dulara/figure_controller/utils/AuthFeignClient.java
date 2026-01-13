package com.dulara.figure_controller.utils;

import com.dulara.figure_controller.dto.user.AuthResponseDTO;
import com.dulara.figure_controller.dto.user.LoginRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "auth-service", url = "http://172.20.11.177:8070")
public interface AuthFeignClient {

    @PostMapping("/auth/gen/login")
    AuthResponseDTO login(@RequestBody  LoginRequestDTO loginRequestDTO);



}

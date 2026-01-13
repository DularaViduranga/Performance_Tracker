package com.dulara.figure_controller.controller;

import com.dulara.figure_controller.dto.user.AllUserLoginResponseDTO;
import com.dulara.figure_controller.dto.user.LoginRequestDTO;
import com.dulara.figure_controller.exception.UnauthorizedException;
import com.dulara.figure_controller.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        System.out.println("Received login request for username: " + loginRequest.getUsername());
        try {
            AllUserLoginResponseDTO response = authService.login(loginRequest);
            System.out.println("Login successful for username: " + response);
            return ResponseEntity.ok(response);
        } catch (feign.FeignException ex) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid username or password"));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Login failed"));
        }
    }

}

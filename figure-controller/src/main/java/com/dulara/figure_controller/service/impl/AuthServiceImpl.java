package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.user.AllUserLoginResponseDTO;
import com.dulara.figure_controller.dto.user.AuthResponseDTO;
import com.dulara.figure_controller.dto.user.LoginRequestDTO;
import com.dulara.figure_controller.entity.NonSalesUserEntity;
import com.dulara.figure_controller.entity.Role;
import com.dulara.figure_controller.exception.UnauthorizedException;
import com.dulara.figure_controller.repository.mysql.AuthRepository;
import com.dulara.figure_controller.security.JwtUtil;
import com.dulara.figure_controller.service.AuthService;
import com.dulara.figure_controller.utils.AuthFeignClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final AuthFeignClient authFeignClient;
    private final JwtUtil jwtUtil;


    public AuthServiceImpl(AuthRepository authRepository, AuthFeignClient authFeignClient, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.authFeignClient = authFeignClient;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AllUserLoginResponseDTO login(LoginRequestDTO loginRequest) {
        AuthResponseDTO externalResponse = authFeignClient.login(loginRequest);
        System.out.println("External auth response: " + externalResponse);

//        AuthResponseDTO externalResponse1 = new AuthResponseDTO(
//                "john.doe",      // username
//                "E12345",        // epf
//                "Level 1",       // level
//                "Colombo",       // location
//                "ABC Corp",      // company
//                "Sales",         // dpt
//                "Sales",         // empType
//                "external"       // token
//        );
//
//        AuthResponseDTO externalResponse = new AuthResponseDTO(
//                "eve.techie",      // username
//                "E12345",        // epf
//                "Level 1",       // level
//                "Colombo",       // location
//                "ABC Corp",      // company
//                "Sales",         // dpt
//                "Non Sales",         // empType
//                "external"       // token
//        );

        String username = externalResponse.getUsername();
        String empType = externalResponse.getEmpType();

        if (empType == null || empType.trim().isEmpty()) {
            throw new UnauthorizedException("Employee type is not set for user: " + username);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("empType", empType);

        String intermediaryCode = null;
        Role userRole = Role.SALES_OFFICER; // default

        String branchName = null;
        String branchCode = null;
        String regionName = null;
        String regionCode = null;


        if ("Sales".equalsIgnoreCase(empType.trim())) {
            intermediaryCode = username;
            userRole = Role.SALES_OFFICER;;

        } else if ("Non Sales".equalsIgnoreCase(empType.trim())) {
            NonSalesUserEntity nonSalesUser = authRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException("Non-sales user not authorized"));

            intermediaryCode = nonSalesUser.getSfcCode();
            userRole =  nonSalesUser.getRole();

            branchCode = nonSalesUser.getBranchCode();
            branchName = nonSalesUser.getBranchName();
            regionCode = nonSalesUser.getRegionCode();
            regionName = nonSalesUser.getRegionName();

            claims.put("branchCode", branchCode);
            claims.put("branchName", branchName);
            claims.put("regionCode", regionCode);
            claims.put("regionName", regionName);
            claims.put("sfcCode", intermediaryCode);
        }

        claims.put("branchName", branchName);
        claims.put("regionName", regionName);

        String jwtToken = jwtUtil.createToken(
                claims,
                username
        );

        return new AllUserLoginResponseDTO(
                jwtToken,
                username,
                empType,
                intermediaryCode,
                userRole.toString(),
                branchCode,
                regionCode,
                branchName,
                regionName    // may be null
        );
    }
}

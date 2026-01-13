package com.dulara.figure_controller.service.impl;

import com.dulara.figure_controller.dto.user.*;
import com.dulara.figure_controller.entity.BranchEntity;
import com.dulara.figure_controller.entity.RegionEntity;
import com.dulara.figure_controller.entity.Role;
import com.dulara.figure_controller.entity.UserEntity;
import com.dulara.figure_controller.exception.InvalidPasswordException;
import com.dulara.figure_controller.exception.UserAlreadyExistsException;
import com.dulara.figure_controller.repository.mysql.BranchRepository;
import com.dulara.figure_controller.repository.mysql.RegionRepository;
import com.dulara.figure_controller.repository.mysql.UserRepository;
import com.dulara.figure_controller.repository.oracle.MyFiguresRepository;
import com.dulara.figure_controller.security.JwtUtil;
import com.dulara.figure_controller.security.MD5PasswordEncoder;
import com.dulara.figure_controller.service.UserService;
import com.dulara.figure_controller.utils.dateConverter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final BranchRepository branchRepository;
    private final MyFiguresRepository myFiguresRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final MD5PasswordEncoder md5PasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RegionRepository regionRepository,
                           BranchRepository branchRepository, MyFiguresRepository myFiguresRepository,
                           JwtUtil jwtUtil,
                           UserDetailsService userDetailsService,
                           MD5PasswordEncoder md5PasswordEncoder) {
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.branchRepository = branchRepository;
        this.myFiguresRepository = myFiguresRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.md5PasswordEncoder = md5PasswordEncoder;
    }



    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
//        UserEntity user = userRepository.findByUsername(loginRequestDTO.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found" ));
//
//        String hashedInputPassword = md5PasswordEncoder.encode(loginRequestDTO.getPassword());
//
//
//        if (!user.getPassword().equals(hashedInputPassword)) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
//
//        Long branchId = user.getBranchId();
//        Long regionId = user.getRegionId();
//
//        // Handle null values - convert to 0 or appropriate default values
//        long branchIdValue = branchId != null ? branchId : 0L;
//        long regionIdValue = regionId != null ? regionId : 0L;
//
//        String token = jwtUtil.createToken(userDetails, user.getId(), user.getRole().name(), regionIdValue, branchIdValue);
//
//        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
//                token, LocalDateTime.now(), user.getRole().name(), branchId, regionId
//        );
//
//        return loginResponseDTO;
        return null;
    }

    @Override
    public UserRegisterResponseDTO rmRegister(RMRegisterRequestDTO rmRegisterRequestDTO) {
        if(userRepository.existsByUsername(rmRegisterRequestDTO.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }

        RegionEntity region = regionRepository.findById(rmRegisterRequestDTO.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found with ID: " + rmRegisterRequestDTO.getRegionId()));

        if(region.getRegionalManager() == null){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(rmRegisterRequestDTO.getUsername());
            userEntity.setPassword(md5PasswordEncoder.encode(rmRegisterRequestDTO.getPassword()));
            userEntity.setRole(Role.RM);
            userEntity.setRegion(region);

            UserEntity savedUser = userRepository.save(userEntity);

            region.setRegionalManager(savedUser);
            regionRepository.save(region);

            regionRepository.save(region);

            return new UserRegisterResponseDTO("RM registered successfully", null);
        }else {
            throw new RuntimeException("Region already has a Regional Manager assigned");
        }


    }

    @Override
    public UserRegisterResponseDTO bmRegister(BMRegisterRequestDTO bmRegisterRequestDTO) {
        if(userRepository.existsByUsername(bmRegisterRequestDTO.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }


        BranchEntity branch = branchRepository.findById(bmRegisterRequestDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + bmRegisterRequestDTO.getBranchId()));

        if(branch.getBranchManager() == null){
            RegionEntity region = branch.getRegion();

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(bmRegisterRequestDTO.getUsername());
            userEntity.setPassword(md5PasswordEncoder.encode(bmRegisterRequestDTO.getPassword()));
            userEntity.setRole(Role.BM);
            userEntity.setBranch(branch);
            userEntity.setRegion(region);


            UserEntity savedUser = userRepository.save(userEntity);

            branch.setBranchManager(savedUser);
            branchRepository.save(branch);

            return new UserRegisterResponseDTO("BM registered successfully", null);
        } else {
            throw new RuntimeException("Branch already has a Branch Manager assigned");
        }
    }

    @Override
    public UserRegisterResponseDTO salesOfficerRegister(SalesOfficerSaveRequestDTO salesOfficerSaveRequestDTO) {
        if(userRepository.existsByUsername(salesOfficerSaveRequestDTO.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }

        BranchEntity branch = branchRepository.findById(salesOfficerSaveRequestDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + salesOfficerSaveRequestDTO.getBranchId()));

        RegionEntity region = branch.getRegion();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(salesOfficerSaveRequestDTO.getUsername());
        userEntity.setPassword(md5PasswordEncoder.encode(salesOfficerSaveRequestDTO.getPassword()));
        userEntity.setRole(Role.SALES_OFFICER);
        userEntity.setBranch(branch);
        userEntity.setRegion(region);

        userRepository.save(userEntity);

        return new UserRegisterResponseDTO("Sales Officer registered successfully", null);
    }

    @Override
    public List<GetRMsResponseDTO> getAllRMs() {
        List<UserEntity> rmEntities = userRepository.findByRole(Role.RM);
        return rmEntities.stream()
                .map(rm -> new GetRMsResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }

    @Override
    public List<GetBMsResponseDTO> getAllBMs() {
        List<UserEntity> rmEntities = userRepository.findByRole(Role.BM);
        return rmEntities.stream()
                .map(rm -> new GetBMsResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getBranch() != null ? rm.getBranch().getName() : null,
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }

    @Override
    public List<GetSalesOfficerResponseDTO> getAllSalesOfficers() {
        List<UserEntity> rmEntities = userRepository.findByRole(Role.SALES_OFFICER);
        return rmEntities.stream()
                .map(rm -> new GetSalesOfficerResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getBranch() != null ? rm.getBranch().getName() : null,
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }

    @Override
    public List<GetBMsResponseDTO> getBMbyRegion(Long regionId) {
        List<UserEntity> bmEntities = userRepository.findByRoleAndRegion_Id(Role.BM, regionId);
        return bmEntities.stream()
                .map(rm -> new GetBMsResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getBranch() != null ? rm.getBranch().getName() : null,
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }

    @Override
    public List<GetSalesOfficerResponseDTO> getSalesOfficerbyRegion(Long regionId) {
        List<UserEntity> salesOfficerEntities = userRepository.findByRoleAndRegion_Id(Role.SALES_OFFICER, regionId);
        return salesOfficerEntities.stream()
                .map(rm -> new GetSalesOfficerResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getBranch() != null ? rm.getBranch().getName() : null,
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }

    @Override
    public List<GetSalesOfficerResponseDTO> getSalesOfficerbyBranch(Long branchId) {
        List<UserEntity> salesOfficerEntities = userRepository.findByRoleAndBranch_Id(Role.SALES_OFFICER, branchId);
        return salesOfficerEntities.stream()
                .map(rm -> new GetSalesOfficerResponseDTO(
                        rm.getId(),
                        rm.getUsername(),
                        rm.getBranch() != null ? rm.getBranch().getName() : null,
                        rm.getRegion() != null ? rm.getRegion().getName() : null
                ))
                .toList();
    }


    @Override
    public void deleteRM(Long id) {
        if(!userRepository.existsById(id)){
            throw new UsernameNotFoundException("User not found ");
        }else  {

            // Find the region that this RM manages
            RegionEntity region = regionRepository.findByRegionalManager_Id(id)
                    .orElse(null);
            if (region != null) {
                region.setRegionalManager(null);
                regionRepository.save(region);
            }
            userRepository.deleteById(id);
        }
    }

    @Override
    public void deleteBM(Long id) {
        if(!userRepository.existsById(id)){
            throw new UsernameNotFoundException("User not found ");
        }else  {

            // Find the branch that this RM manages
            BranchEntity branch = branchRepository.findByBranchManager_Id(id)
                    .orElse(null);
            if (branch != null) {
                branch.setBranchManager(null);
                branchRepository.save(branch);
            }
            userRepository.deleteById(id);
        }
    }

    @Override
    public void deleteSalesOfficer(Long id) {
        if(!userRepository.existsById(id)){
            throw new UsernameNotFoundException("User not found ");
        }else  {
            userRepository.deleteById(id);
        }
    }


    @Override
    public PasswordChangeResponseDTO updatePassword(Long id, PasswordChangeRequestDTO passwordChangeRequestDTO) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String hashedOldPassword = md5PasswordEncoder.encode(passwordChangeRequestDTO.getOldPassword());

        if (!user.getPassword().equals(hashedOldPassword)) {
            throw new InvalidPasswordException("Old password does not match hashed password");
        }

        if (!passwordChangeRequestDTO.getNewPassword().equals(passwordChangeRequestDTO.getConfirmNewPassword())) {
            throw new InvalidPasswordException("New password and Confirm new password do not match");
        }

        user.setPassword(md5PasswordEncoder.encode(passwordChangeRequestDTO.getNewPassword()));
        userRepository.save(user);

        return new PasswordChangeResponseDTO("Password updated successfully", null);
    }

//    @Override
//    public List<SFCCodeResponseDTO> getSFCCodesBySFC(String sfcCode, String fromDate, String toDate) {
//        List<Map<String, Object>> results = myFiguresRepository.findSFCcodesBySFC(sfcCode, fromDate, toDate);
//        List<SFCCodeResponseDTO> dtos = new ArrayList<>();
//
//        for(Map<String, Object> row : results) {
//            SFCCodeResponseDTO dto = new SFCCodeResponseDTO();
//            dto.setSfcCode((String) row.get("SFC_CODE"));
//            dtos.add(dto);
//        }
//        return dtos;
//    }
//
    @Override
    public List<SFCCodeResponseDTO> getSFCCodesBySFC(String sfcCode, String fromDate, String toDate) {
        String formattedStart = dateConverter.toIsoFormat(fromDate);
        String formattedEnd = dateConverter.toIsoFormat(toDate);

        Set<String> allSfcCodes = new HashSet<>();
        Map<String, SFCCodeResponseDTO> sfcCodeNameBranchMap = new HashMap<>(); // Store code -> name
        Queue<String> toProcess = new LinkedList<>();

        // Start with the initial SFC code
        toProcess.add(sfcCode);
        allSfcCodes.add(sfcCode);

        // Process all codes level by level
        while (!toProcess.isEmpty()) {
            String currentSfc = toProcess.poll();

            // Get directly related SFC codes
            List<Map<String, Object>> results = myFiguresRepository.findSFCcodesBySFC(
                    currentSfc, formattedStart, formattedEnd
            );

            // Add new codes to process queue
            for (Map<String, Object> row : results) {
                String newSfcCode = (String) row.get("SFC_CODE");
                String sfcName = (String) row.get("SFC_NAME");
                String brnName = (String) row.get("BRN_CODE");

                // Only process if we haven't seen this code before (prevents infinite loops)
                if (!allSfcCodes.contains(newSfcCode)) {
                    allSfcCodes.add(newSfcCode);
                    toProcess.add(newSfcCode);

                    // Create DTO and store
                    SFCCodeResponseDTO dto = new SFCCodeResponseDTO();
                    dto.setSfcCode(newSfcCode);
                    dto.setSfcName(sfcName);
                    dto.setBrnName(brnName);

                    sfcCodeNameBranchMap.put(newSfcCode, dto);
                }
            }
        }

        // Convert to DTOs (excluding the original sfcCode if needed)
        List<SFCCodeResponseDTO> dtos = new ArrayList<>();
        for (String code : allSfcCodes) {
            if (!code.equals(sfcCode)) {  // Exclude the original code from results
                dtos.add(sfcCodeNameBranchMap.get(code));
            }
        }

        return dtos;
    }

    @Override
    public List<SalesOfficersByBranchResponseDTO> getSFCCodesByBranch(String brnCode, String fromDate, String toDate) {
        String formattedStart = dateConverter.toOracleFormat(fromDate);
        String formattedEnd = dateConverter.toOracleFormat(toDate);

        List<Map<String, Object>> rawData = myFiguresRepository.getSFCCodesByBranch(brnCode, formattedStart, formattedEnd);
        List<SalesOfficersByBranchResponseDTO> dtos = new ArrayList<>();

        for(Map<String, Object> row : rawData) {
            SalesOfficersByBranchResponseDTO dto = new SalesOfficersByBranchResponseDTO();
            dto.setSfcCode((String) row.get("SFC_CODE"));
            dto.setSfcName((String) row.get("SFC_NAME"));
            dtos.add(dto);
        }
        return dtos;

    }


}

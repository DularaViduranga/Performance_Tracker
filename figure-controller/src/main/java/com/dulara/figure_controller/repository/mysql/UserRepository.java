package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.entity.Role;
import com.dulara.figure_controller.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserEntity> findByRole(Role role);

    List<UserEntity> findByRoleAndRegion_Id(Role role, Long regionId);

    List<UserEntity> findByRoleAndBranch_Id(Role role, Long branchId);

}

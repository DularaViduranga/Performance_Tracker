package com.dulara.figure_controller.repository.mysql;

import com.dulara.figure_controller.entity.NonSalesUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthRepository extends JpaRepository<NonSalesUserEntity, Long> {

    Optional<NonSalesUserEntity> findByUsername(String username);
}

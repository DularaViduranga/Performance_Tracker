package com.dulara.figure_controller.repository.mysql;
import com.dulara.figure_controller.entity.BranchEntity;
import com.dulara.figure_controller.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BranchRepository extends JpaRepository<BranchEntity,Long> {
    Optional<BranchEntity> findByBranchManager_Id(Long id);

    boolean existsByName(String upperCase);

    @Query("SELECT b FROM BranchEntity b WHERE b.region.id = :id")
    List<BranchEntity> findByBranchByRegionId(Long id);
}

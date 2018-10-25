package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoals;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmGoals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalsRepository extends JpaRepository<PmGoals, Long> {
    List<PmGoals> findByIdEmployeeOwner(Long id);
    List<PmGoals> findByIdGoalTypeId(Long id);
    /*
    @Query("SELECT pg FROM PmGoals pg WHERE UPPER(?1) LIKE UPPER(pg.name)")
    List<PmGoals> findByNameIgnoringCase(String name);
    */
    List<PmGoals> findByNameContainingIgnoreCase(String name);
}

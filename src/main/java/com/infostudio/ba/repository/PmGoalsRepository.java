package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoals;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmGoals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalsRepository extends JpaRepository<PmGoals, Long> {
    List<PmGoals> findByArchived(String archived);
    PmGoals findByCode(String code);
    List<PmGoals> findByIdEmployeeOwner(Long id);
    List<PmGoals> findByIdGoalTypeId(Long id);

    List<PmGoals> findByNameContainingIgnoreCase(String name);

    List<PmGoals> findByIdGoalId(Long goalChildId);

    // Gets the Pm Goals that have no parent Pm Goal
    List<PmGoals> findAllByIdGoalIsNull();
}

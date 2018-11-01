package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmEmployeesGoals;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmEmployeesGoals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmEmployeesGoalsRepository extends JpaRepository<PmEmployeesGoals, Long> {
    List<PmEmployeesGoals> findByIdGoalId(Long id);
}

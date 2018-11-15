package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoalsEvaluations;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmGoalsEvaluations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalsEvaluationsRepository extends JpaRepository<PmGoalsEvaluations, Long> {
    List<PmGoalsEvaluations> findAllByIdEmployeeGoalId(Long id);
}

package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoalsEvaluations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the PmGoalsEvaluations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalsEvaluationsRepository extends JpaRepository<PmGoalsEvaluations, Long> {
    List<PmGoalsEvaluations> findAllByIdEmployeeGoalId(Long id);

    @Query("SELECT pm FROM PmGoalsEvaluations pm WHERE (?1 IS NULL OR pm.idEmployeeGoal.idEmployeeResponsible = ?1) AND "
			+ "(?2 IS NULL OR pm.idEvaluationState.id = ?2) AND "
			+ "(?3 IS NULL OR pm.evaluationPeriodFrom >= ?3) AND"
			+ "(?4 IS NULL OR pm.evaluationPeriodTo <= ?4)")
	Page<PmGoalsEvaluations> searchGoalEvaluations(Pageable pageable, Long employeeId, Long stateId, LocalDate evaluationFrom, LocalDate evaluationTo);
}

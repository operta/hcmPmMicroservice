package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmCorrectiveMeasures;
import com.infostudio.ba.domain.PmGoalsEvaluations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the PmCorrectiveMeasures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmCorrectiveMeasuresRepository extends JpaRepository<PmCorrectiveMeasures, Long> {
    List<PmCorrectiveMeasures> findAllByIdGoalEvaluationId(Long id);

	List<PmCorrectiveMeasures> findAllByEndDateBeforeAndIdCmStateIdNot(LocalDate date, Long stateId);


	@Query("SELECT pm FROM PmCorrectiveMeasures pm WHERE (?1 IS NULL OR pm.idGoalEvaluation.idEmployeeGoal.idEmployeeResponsible = ?1) AND "
			+ "(?2 IS NULL OR pm.endDate <= ?2) AND "
			+ "(?3 IS NULL OR pm.startDate >= ?3) AND "
			+ "(?4 IS NULL OR pm.idCmState.id = ?4) AND "
			+ "(?5 IS NULL OR pm.idCorrectiveMeasureType.id = ?5)")
	Page<PmCorrectiveMeasures> searchCorrectiveMeasure(Pageable pageable, Long employeeId, LocalDate endDate, LocalDate startDate,
													   Long stateId, Long typeId);
}

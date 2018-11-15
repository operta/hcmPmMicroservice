package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmCorrectiveMeasures;
import com.infostudio.ba.domain.PmGoalsEvaluations;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmCorrectiveMeasures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmCorrectiveMeasuresRepository extends JpaRepository<PmCorrectiveMeasures, Long> {
    List<PmCorrectiveMeasures> findAllByIdGoalEvaluationId(Long id);
}

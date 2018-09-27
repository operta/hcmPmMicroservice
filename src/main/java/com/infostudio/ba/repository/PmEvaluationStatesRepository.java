package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmEvaluationStates;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmEvaluationStates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmEvaluationStatesRepository extends JpaRepository<PmEvaluationStates, Long> {

}

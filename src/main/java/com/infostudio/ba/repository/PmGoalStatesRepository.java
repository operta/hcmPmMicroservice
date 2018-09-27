package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoalStates;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmGoalStates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalStatesRepository extends JpaRepository<PmGoalStates, Long> {

}

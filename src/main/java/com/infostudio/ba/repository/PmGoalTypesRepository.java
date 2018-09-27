package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoalTypes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmGoalTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalTypesRepository extends JpaRepository<PmGoalTypes, Long> {

}

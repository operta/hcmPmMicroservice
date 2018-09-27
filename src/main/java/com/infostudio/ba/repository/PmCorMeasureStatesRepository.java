package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmCorMeasureStates;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmCorMeasureStates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmCorMeasureStatesRepository extends JpaRepository<PmCorMeasureStates, Long> {

}

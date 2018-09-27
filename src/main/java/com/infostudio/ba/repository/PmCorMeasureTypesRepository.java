package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmCorMeasureTypes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmCorMeasureTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmCorMeasureTypesRepository extends JpaRepository<PmCorMeasureTypes, Long> {

}

package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmUnitOfMeasures;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmUnitOfMeasures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmUnitOfMeasuresRepository extends JpaRepository<PmUnitOfMeasures, Long> {

}

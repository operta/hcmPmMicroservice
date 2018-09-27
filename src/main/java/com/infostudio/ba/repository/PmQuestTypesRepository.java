package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestTypes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmQuestTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestTypesRepository extends JpaRepository<PmQuestTypes, Long> {

}

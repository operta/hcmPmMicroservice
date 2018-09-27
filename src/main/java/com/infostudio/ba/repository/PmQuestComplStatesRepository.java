package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestComplStates;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmQuestComplStates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestComplStatesRepository extends JpaRepository<PmQuestComplStates, Long> {

}

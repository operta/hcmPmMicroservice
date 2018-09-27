package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmGoals;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmGoals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmGoalsRepository extends JpaRepository<PmGoals, Long> {

}

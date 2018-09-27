package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestComplAnswers;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PmQuestComplAnswers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestComplAnswersRepository extends JpaRepository<PmQuestComplAnswers, Long> {

}

package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestCompletions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmQuestCompletions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestCompletionsRepository extends JpaRepository<PmQuestCompletions, Long> {
    List<PmQuestCompletions> findAllByIdQuestionnaireId(Long questionnaireId);
}

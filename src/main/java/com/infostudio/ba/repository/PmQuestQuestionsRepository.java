package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestQuestions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmQuestQuestions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestQuestionsRepository extends JpaRepository<PmQuestQuestions, Long> {
    List<PmQuestQuestions> findAllByIdQuestionnaire(Long id);
}

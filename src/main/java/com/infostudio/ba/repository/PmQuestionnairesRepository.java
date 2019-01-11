package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmQuestionnaires;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PmQuestionnaires entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmQuestionnairesRepository extends JpaRepository<PmQuestionnaires, Long> {
    List<PmQuestionnaires> findByIdHeader(Long headerId);
}

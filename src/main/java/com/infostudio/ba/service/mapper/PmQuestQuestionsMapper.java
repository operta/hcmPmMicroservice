package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestQuestionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestQuestions and its DTO PmQuestQuestionsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmQuestQuestionsMapper extends EntityMapper<PmQuestQuestionsDTO, PmQuestQuestions> {



    default PmQuestQuestions fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestQuestions pmQuestQuestions = new PmQuestQuestions();
        pmQuestQuestions.setId(id);
        return pmQuestQuestions;
    }
}

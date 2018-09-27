package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestComplAnswersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestComplAnswers and its DTO PmQuestComplAnswersDTO.
 */
@Mapper(componentModel = "spring", uses = {PmQuestQuestionsMapper.class, PmQuestCompletionsMapper.class})
public interface PmQuestComplAnswersMapper extends EntityMapper<PmQuestComplAnswersDTO, PmQuestComplAnswers> {

    @Mapping(source = "idQuestionnaireQuestion.id", target = "idQuestionnaireQuestionId")
    @Mapping(source = "idQuestionnaireCompletion.id", target = "idQuestionnaireCompletionId")
    PmQuestComplAnswersDTO toDto(PmQuestComplAnswers pmQuestComplAnswers);

    @Mapping(source = "idQuestionnaireQuestionId", target = "idQuestionnaireQuestion")
    @Mapping(source = "idQuestionnaireCompletionId", target = "idQuestionnaireCompletion")
    PmQuestComplAnswers toEntity(PmQuestComplAnswersDTO pmQuestComplAnswersDTO);

    default PmQuestComplAnswers fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestComplAnswers pmQuestComplAnswers = new PmQuestComplAnswers();
        pmQuestComplAnswers.setId(id);
        return pmQuestComplAnswers;
    }
}

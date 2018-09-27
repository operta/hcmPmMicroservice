package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestCompletionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestCompletions and its DTO PmQuestCompletionsDTO.
 */
@Mapper(componentModel = "spring", uses = {PmQuestComplStatesMapper.class, PmQuestionnairesMapper.class})
public interface PmQuestCompletionsMapper extends EntityMapper<PmQuestCompletionsDTO, PmQuestCompletions> {

    @Mapping(source = "idQuestCompletionState.id", target = "idQuestCompletionStateId")
    @Mapping(source = "idQuestionnaire.id", target = "idQuestionnaireId")
    PmQuestCompletionsDTO toDto(PmQuestCompletions pmQuestCompletions);

    @Mapping(source = "idQuestCompletionStateId", target = "idQuestCompletionState")
    @Mapping(source = "idQuestionnaireId", target = "idQuestionnaire")
    PmQuestCompletions toEntity(PmQuestCompletionsDTO pmQuestCompletionsDTO);

    default PmQuestCompletions fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestCompletions pmQuestCompletions = new PmQuestCompletions();
        pmQuestCompletions.setId(id);
        return pmQuestCompletions;
    }
}

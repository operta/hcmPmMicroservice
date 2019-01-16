package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestionnairesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestionnaires and its DTO PmQuestionnairesDTO.
 */
@Mapper(componentModel = "spring", uses = {PmQuestTypesMapper.class})
public interface PmQuestionnairesMapper extends EntityMapper<PmQuestionnairesDTO, PmQuestionnaires> {

    @Mapping(source = "idQuestionnaireType.id", target = "idQuestionnaireTypeId")
    @Mapping(source = "idQuestionnaireType.name", target = "idQuestionnaireTypeName")
    PmQuestionnairesDTO toDto(PmQuestionnaires pmQuestionnaires);

    @Mapping(source = "idQuestionnaireTypeId", target = "idQuestionnaireType")
    PmQuestionnaires toEntity(PmQuestionnairesDTO pmQuestionnairesDTO);

    default PmQuestionnaires fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestionnaires pmQuestionnaires = new PmQuestionnaires();
        pmQuestionnaires.setId(id);
        return pmQuestionnaires;
    }
}

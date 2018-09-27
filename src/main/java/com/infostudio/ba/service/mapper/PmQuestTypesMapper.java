package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestTypesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestTypes and its DTO PmQuestTypesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmQuestTypesMapper extends EntityMapper<PmQuestTypesDTO, PmQuestTypes> {



    default PmQuestTypes fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestTypes pmQuestTypes = new PmQuestTypes();
        pmQuestTypes.setId(id);
        return pmQuestTypes;
    }
}

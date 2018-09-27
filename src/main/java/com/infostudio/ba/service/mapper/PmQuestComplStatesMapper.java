package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmQuestComplStatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmQuestComplStates and its DTO PmQuestComplStatesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmQuestComplStatesMapper extends EntityMapper<PmQuestComplStatesDTO, PmQuestComplStates> {



    default PmQuestComplStates fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmQuestComplStates pmQuestComplStates = new PmQuestComplStates();
        pmQuestComplStates.setId(id);
        return pmQuestComplStates;
    }
}

package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmGoalStatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmGoalStates and its DTO PmGoalStatesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmGoalStatesMapper extends EntityMapper<PmGoalStatesDTO, PmGoalStates> {



    default PmGoalStates fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmGoalStates pmGoalStates = new PmGoalStates();
        pmGoalStates.setId(id);
        return pmGoalStates;
    }
}

package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmGoalTypesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmGoalTypes and its DTO PmGoalTypesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmGoalTypesMapper extends EntityMapper<PmGoalTypesDTO, PmGoalTypes> {



    default PmGoalTypes fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmGoalTypes pmGoalTypes = new PmGoalTypes();
        pmGoalTypes.setId(id);
        return pmGoalTypes;
    }
}

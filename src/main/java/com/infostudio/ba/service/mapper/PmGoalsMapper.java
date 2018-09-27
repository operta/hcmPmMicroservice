package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmGoalsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmGoals and its DTO PmGoalsDTO.
 */
@Mapper(componentModel = "spring", uses = {PmGoalTypesMapper.class})
public interface PmGoalsMapper extends EntityMapper<PmGoalsDTO, PmGoals> {

    @Mapping(source = "idGoalType.id", target = "idGoalTypeId")
    @Mapping(source = "idGoal.id", target = "idGoalId")
    PmGoalsDTO toDto(PmGoals pmGoals);

    @Mapping(source = "idGoalTypeId", target = "idGoalType")
    @Mapping(source = "idGoalId", target = "idGoal")
    PmGoals toEntity(PmGoalsDTO pmGoalsDTO);

    default PmGoals fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmGoals pmGoals = new PmGoals();
        pmGoals.setId(id);
        return pmGoals;
    }
}

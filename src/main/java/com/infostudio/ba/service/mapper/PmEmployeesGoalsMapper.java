package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmEmployeesGoalsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmEmployeesGoals and its DTO PmEmployeesGoalsDTO.
 */
@Mapper(componentModel = "spring", uses = {PmUnitOfMeasuresMapper.class, PmGoalStatesMapper.class, PmGoalsMapper.class})
public interface PmEmployeesGoalsMapper extends EntityMapper<PmEmployeesGoalsDTO, PmEmployeesGoals> {

    @Mapping(source = "idUnit.id", target = "idUnitId")
    @Mapping(source = "idUnit.name", target = "idUnitName")
    @Mapping(source = "idGoalState.id", target = "idGoalStateId")
    @Mapping(source = "idGoalState.name", target = "idGoalStateName")
    @Mapping(source = "idGoal.id", target = "idGoalId")
    PmEmployeesGoalsDTO toDto(PmEmployeesGoals pmEmployeesGoals);

    @Mapping(source = "idUnitId", target = "idUnit")
    @Mapping(source = "idGoalStateId", target = "idGoalState")
    @Mapping(source = "idGoalId", target = "idGoal")
    PmEmployeesGoals toEntity(PmEmployeesGoalsDTO pmEmployeesGoalsDTO);

    default PmEmployeesGoals fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmEmployeesGoals pmEmployeesGoals = new PmEmployeesGoals();
        pmEmployeesGoals.setId(id);
        return pmEmployeesGoals;
    }
}

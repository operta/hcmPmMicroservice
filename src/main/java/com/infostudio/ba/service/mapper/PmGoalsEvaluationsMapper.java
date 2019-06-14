package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmGoalsEvaluationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmGoalsEvaluations and its DTO PmGoalsEvaluationsDTO.
 */
@Mapper(componentModel = "spring", uses = {PmEmployeesGoalsMapper.class, PmEvaluationStatesMapper.class})
public interface PmGoalsEvaluationsMapper extends EntityMapper<PmGoalsEvaluationsDTO, PmGoalsEvaluations> {

    @Mapping(source = "idEmployeeGoal.id", target = "idEmployeeGoalId")
    @Mapping(source = "idEvaluationState.id", target = "idEvaluationStateId")
    @Mapping(source = "idEvaluationState.name", target = "idEvaluationStateName")
    @Mapping(source = "idEmployeeGoal.idEmployeeResponsible", target = "idEmployeeId")
    PmGoalsEvaluationsDTO toDto(PmGoalsEvaluations pmGoalsEvaluations);

    @Mapping(source = "idEmployeeGoalId", target = "idEmployeeGoal")
    @Mapping(source = "idEvaluationStateId", target = "idEvaluationState")
    PmGoalsEvaluations toEntity(PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO);

    default PmGoalsEvaluations fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmGoalsEvaluations pmGoalsEvaluations = new PmGoalsEvaluations();
        pmGoalsEvaluations.setId(id);
        return pmGoalsEvaluations;
    }
}

package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmCorrectiveMeasuresDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmCorrectiveMeasures and its DTO PmCorrectiveMeasuresDTO.
 */
@Mapper(componentModel = "spring", uses = {PmCorMeasureStatesMapper.class, PmCorMeasureTypesMapper.class, PmGoalsEvaluationsMapper.class})
public interface PmCorrectiveMeasuresMapper extends EntityMapper<PmCorrectiveMeasuresDTO, PmCorrectiveMeasures> {

    @Mapping(source = "idCmState.id", target = "idCmStateId")
    @Mapping(source = "idCorrectiveMeasureType.id", target = "idCorrectiveMeasureTypeId")
    @Mapping(source = "idGoalEvaluation.id", target = "idGoalEvaluationId")
    @Mapping(source = "idCorrectiveMeasureType.name", target = "idCorrectiveMeasureTypeName")
    @Mapping(source = "idCmState.name", target = "idCmStateName")
    @Mapping(source = "idGoalEvaluation.idEmployeeGoal.id", target = "idEmployeeGoalId")
    @Mapping(source = "idGoalEvaluation.idEmployeeGoal.idEmployeeResponsible", target = "idEmployeeId")
    PmCorrectiveMeasuresDTO toDto(PmCorrectiveMeasures pmCorrectiveMeasures);

    @Mapping(source = "idCmStateId", target = "idCmState")
    @Mapping(source = "idCorrectiveMeasureTypeId", target = "idCorrectiveMeasureType")
    @Mapping(source = "idGoalEvaluationId", target = "idGoalEvaluation")
    PmCorrectiveMeasures toEntity(PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO);

    default PmCorrectiveMeasures fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmCorrectiveMeasures pmCorrectiveMeasures = new PmCorrectiveMeasures();
        pmCorrectiveMeasures.setId(id);
        return pmCorrectiveMeasures;
    }
}

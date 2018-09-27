package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmGoalEvalQstComplDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmGoalEvalQstCompl and its DTO PmGoalEvalQstComplDTO.
 */
@Mapper(componentModel = "spring", uses = {PmQuestCompletionsMapper.class, PmGoalsEvaluationsMapper.class})
public interface PmGoalEvalQstComplMapper extends EntityMapper<PmGoalEvalQstComplDTO, PmGoalEvalQstCompl> {

    @Mapping(source = "idQuestionaireCompletion.id", target = "idQuestionaireCompletionId")
    @Mapping(source = "idGoalEvaluation.id", target = "idGoalEvaluationId")
    PmGoalEvalQstComplDTO toDto(PmGoalEvalQstCompl pmGoalEvalQstCompl);

    @Mapping(source = "idQuestionaireCompletionId", target = "idQuestionaireCompletion")
    @Mapping(source = "idGoalEvaluationId", target = "idGoalEvaluation")
    PmGoalEvalQstCompl toEntity(PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO);

    default PmGoalEvalQstCompl fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmGoalEvalQstCompl pmGoalEvalQstCompl = new PmGoalEvalQstCompl();
        pmGoalEvalQstCompl.setId(id);
        return pmGoalEvalQstCompl;
    }
}

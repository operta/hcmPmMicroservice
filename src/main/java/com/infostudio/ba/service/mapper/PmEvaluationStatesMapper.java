package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmEvaluationStatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmEvaluationStates and its DTO PmEvaluationStatesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmEvaluationStatesMapper extends EntityMapper<PmEvaluationStatesDTO, PmEvaluationStates> {



    default PmEvaluationStates fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmEvaluationStates pmEvaluationStates = new PmEvaluationStates();
        pmEvaluationStates.setId(id);
        return pmEvaluationStates;
    }
}

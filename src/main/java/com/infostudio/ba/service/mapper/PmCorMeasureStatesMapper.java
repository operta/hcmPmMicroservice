package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmCorMeasureStatesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmCorMeasureStates and its DTO PmCorMeasureStatesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmCorMeasureStatesMapper extends EntityMapper<PmCorMeasureStatesDTO, PmCorMeasureStates> {



    default PmCorMeasureStates fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmCorMeasureStates pmCorMeasureStates = new PmCorMeasureStates();
        pmCorMeasureStates.setId(id);
        return pmCorMeasureStates;
    }
}

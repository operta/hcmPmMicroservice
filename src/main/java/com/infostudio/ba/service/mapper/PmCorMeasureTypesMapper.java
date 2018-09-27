package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmCorMeasureTypesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmCorMeasureTypes and its DTO PmCorMeasureTypesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmCorMeasureTypesMapper extends EntityMapper<PmCorMeasureTypesDTO, PmCorMeasureTypes> {



    default PmCorMeasureTypes fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmCorMeasureTypes pmCorMeasureTypes = new PmCorMeasureTypes();
        pmCorMeasureTypes.setId(id);
        return pmCorMeasureTypes;
    }
}

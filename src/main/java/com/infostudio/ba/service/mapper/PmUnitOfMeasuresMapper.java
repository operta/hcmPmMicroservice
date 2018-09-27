package com.infostudio.ba.service.mapper;

import com.infostudio.ba.domain.*;
import com.infostudio.ba.service.dto.PmUnitOfMeasuresDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PmUnitOfMeasures and its DTO PmUnitOfMeasuresDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PmUnitOfMeasuresMapper extends EntityMapper<PmUnitOfMeasuresDTO, PmUnitOfMeasures> {



    default PmUnitOfMeasures fromId(Long id) {
        if (id == null) {
            return null;
        }
        PmUnitOfMeasures pmUnitOfMeasures = new PmUnitOfMeasures();
        pmUnitOfMeasures.setId(id);
        return pmUnitOfMeasures;
    }
}

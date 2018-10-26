package com.infostudio.ba.web.rest;

import org.apache.commons.lang.RandomStringUtils;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmCorMeasureTypes;

import com.infostudio.ba.repository.PmCorMeasureTypesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmCorMeasureTypesDTO;
import com.infostudio.ba.service.mapper.PmCorMeasureTypesMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PmCorMeasureTypes.
 */
@RestController
@RequestMapping("/api")
public class PmCorMeasureTypesResource {

    private final Logger log = LoggerFactory.getLogger(PmCorMeasureTypesResource.class);

    private static final String ENTITY_NAME = "pmCorMeasureTypes";

    private final PmCorMeasureTypesRepository pmCorMeasureTypesRepository;

    private final PmCorMeasureTypesMapper pmCorMeasureTypesMapper;

    public PmCorMeasureTypesResource(PmCorMeasureTypesRepository pmCorMeasureTypesRepository, PmCorMeasureTypesMapper pmCorMeasureTypesMapper) {
        this.pmCorMeasureTypesRepository = pmCorMeasureTypesRepository;
        this.pmCorMeasureTypesMapper = pmCorMeasureTypesMapper;
    }

    /**
     * POST  /pm-cor-measure-types : Create a new pmCorMeasureTypes.
     *
     * @param pmCorMeasureTypesDTO the pmCorMeasureTypesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmCorMeasureTypesDTO, or with status 400 (Bad Request) if the pmCorMeasureTypes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-cor-measure-types")
    @Timed
    public ResponseEntity<PmCorMeasureTypesDTO> createPmCorMeasureTypes(@Valid @RequestBody PmCorMeasureTypesDTO pmCorMeasureTypesDTO) throws URISyntaxException {
        log.debug("REST request to save PmCorMeasureTypes : {}", pmCorMeasureTypesDTO);
        if (pmCorMeasureTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmCorMeasureTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        while(pmCorMeasureTypesRepository.findByCode(newCode) != null){
            newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        }
        pmCorMeasureTypesDTO.setCode(newCode);
        PmCorMeasureTypes pmCorMeasureTypes = pmCorMeasureTypesMapper.toEntity(pmCorMeasureTypesDTO);
        pmCorMeasureTypes = pmCorMeasureTypesRepository.save(pmCorMeasureTypes);
        PmCorMeasureTypesDTO result = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);
        return ResponseEntity.created(new URI("/api/pm-cor-measure-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-cor-measure-types : Updates an existing pmCorMeasureTypes.
     *
     * @param pmCorMeasureTypesDTO the pmCorMeasureTypesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmCorMeasureTypesDTO,
     * or with status 400 (Bad Request) if the pmCorMeasureTypesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmCorMeasureTypesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-cor-measure-types")
    @Timed
    public ResponseEntity<PmCorMeasureTypesDTO> updatePmCorMeasureTypes(@Valid @RequestBody PmCorMeasureTypesDTO pmCorMeasureTypesDTO) throws URISyntaxException {
        log.debug("REST request to update PmCorMeasureTypes : {}", pmCorMeasureTypesDTO);
        if (pmCorMeasureTypesDTO.getId() == null) {
            return createPmCorMeasureTypes(pmCorMeasureTypesDTO);
        }
        PmCorMeasureTypes pmCorMeasureTypes = pmCorMeasureTypesMapper.toEntity(pmCorMeasureTypesDTO);
        pmCorMeasureTypes = pmCorMeasureTypesRepository.save(pmCorMeasureTypes);
        PmCorMeasureTypesDTO result = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmCorMeasureTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-cor-measure-types : get all the pmCorMeasureTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmCorMeasureTypes in body
     */
    @GetMapping("/pm-cor-measure-types")
    @Timed
    public ResponseEntity<List<PmCorMeasureTypesDTO>> getAllPmCorMeasureTypes(Pageable pageable) {
        log.debug("REST request to get a page of PmCorMeasureTypes");
        Page<PmCorMeasureTypes> page = pmCorMeasureTypesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-cor-measure-types");
        return new ResponseEntity<>(pmCorMeasureTypesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-cor-measure-types/:id : get the "id" pmCorMeasureTypes.
     *
     * @param id the id of the pmCorMeasureTypesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmCorMeasureTypesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-cor-measure-types/{id}")
    @Timed
    public ResponseEntity<PmCorMeasureTypesDTO> getPmCorMeasureTypes(@PathVariable Long id) {
        log.debug("REST request to get PmCorMeasureTypes : {}", id);
        PmCorMeasureTypes pmCorMeasureTypes = pmCorMeasureTypesRepository.findOne(id);
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmCorMeasureTypesDTO));
    }

    /**
     * DELETE  /pm-cor-measure-types/:id : delete the "id" pmCorMeasureTypes.
     *
     * @param id the id of the pmCorMeasureTypesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-cor-measure-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePmCorMeasureTypes(@PathVariable Long id) {
        log.debug("REST request to delete PmCorMeasureTypes : {}", id);
        pmCorMeasureTypesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

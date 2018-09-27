package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmGoalTypes;

import com.infostudio.ba.repository.PmGoalTypesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalTypesDTO;
import com.infostudio.ba.service.mapper.PmGoalTypesMapper;
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
 * REST controller for managing PmGoalTypes.
 */
@RestController
@RequestMapping("/api")
public class PmGoalTypesResource {

    private final Logger log = LoggerFactory.getLogger(PmGoalTypesResource.class);

    private static final String ENTITY_NAME = "pmGoalTypes";

    private final PmGoalTypesRepository pmGoalTypesRepository;

    private final PmGoalTypesMapper pmGoalTypesMapper;

    public PmGoalTypesResource(PmGoalTypesRepository pmGoalTypesRepository, PmGoalTypesMapper pmGoalTypesMapper) {
        this.pmGoalTypesRepository = pmGoalTypesRepository;
        this.pmGoalTypesMapper = pmGoalTypesMapper;
    }

    /**
     * POST  /pm-goal-types : Create a new pmGoalTypes.
     *
     * @param pmGoalTypesDTO the pmGoalTypesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalTypesDTO, or with status 400 (Bad Request) if the pmGoalTypes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-goal-types")
    @Timed
    public ResponseEntity<PmGoalTypesDTO> createPmGoalTypes(@Valid @RequestBody PmGoalTypesDTO pmGoalTypesDTO) throws URISyntaxException {
        log.debug("REST request to save PmGoalTypes : {}", pmGoalTypesDTO);
        if (pmGoalTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmGoalTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmGoalTypes pmGoalTypes = pmGoalTypesMapper.toEntity(pmGoalTypesDTO);
        pmGoalTypes = pmGoalTypesRepository.save(pmGoalTypes);
        PmGoalTypesDTO result = pmGoalTypesMapper.toDto(pmGoalTypes);
        return ResponseEntity.created(new URI("/api/pm-goal-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-goal-types : Updates an existing pmGoalTypes.
     *
     * @param pmGoalTypesDTO the pmGoalTypesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalTypesDTO,
     * or with status 400 (Bad Request) if the pmGoalTypesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmGoalTypesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-goal-types")
    @Timed
    public ResponseEntity<PmGoalTypesDTO> updatePmGoalTypes(@Valid @RequestBody PmGoalTypesDTO pmGoalTypesDTO) throws URISyntaxException {
        log.debug("REST request to update PmGoalTypes : {}", pmGoalTypesDTO);
        if (pmGoalTypesDTO.getId() == null) {
            return createPmGoalTypes(pmGoalTypesDTO);
        }
        PmGoalTypes pmGoalTypes = pmGoalTypesMapper.toEntity(pmGoalTypesDTO);
        pmGoalTypes = pmGoalTypesRepository.save(pmGoalTypes);
        PmGoalTypesDTO result = pmGoalTypesMapper.toDto(pmGoalTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-goal-types : get all the pmGoalTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmGoalTypes in body
     */
    @GetMapping("/pm-goal-types")
    @Timed
    public ResponseEntity<List<PmGoalTypesDTO>> getAllPmGoalTypes(Pageable pageable) {
        log.debug("REST request to get a page of PmGoalTypes");
        Page<PmGoalTypes> page = pmGoalTypesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goal-types");
        return new ResponseEntity<>(pmGoalTypesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-goal-types/:id : get the "id" pmGoalTypes.
     *
     * @param id the id of the pmGoalTypesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmGoalTypesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-goal-types/{id}")
    @Timed
    public ResponseEntity<PmGoalTypesDTO> getPmGoalTypes(@PathVariable Long id) {
        log.debug("REST request to get PmGoalTypes : {}", id);
        PmGoalTypes pmGoalTypes = pmGoalTypesRepository.findOne(id);
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalTypesDTO));
    }

    /**
     * DELETE  /pm-goal-types/:id : delete the "id" pmGoalTypes.
     *
     * @param id the id of the pmGoalTypesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-goal-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePmGoalTypes(@PathVariable Long id) {
        log.debug("REST request to delete PmGoalTypes : {}", id);
        pmGoalTypesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

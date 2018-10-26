package com.infostudio.ba.web.rest;


import org.apache.commons.lang.RandomStringUtils;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmGoalStates;

import com.infostudio.ba.repository.PmGoalStatesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalStatesDTO;
import com.infostudio.ba.service.mapper.PmGoalStatesMapper;
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
 * REST controller for managing PmGoalStates.
 */
@RestController
@RequestMapping("/api")
public class PmGoalStatesResource {

    private final Logger log = LoggerFactory.getLogger(PmGoalStatesResource.class);

    private static final String ENTITY_NAME = "pmGoalStates";

    private final PmGoalStatesRepository pmGoalStatesRepository;

    private final PmGoalStatesMapper pmGoalStatesMapper;

    public PmGoalStatesResource(PmGoalStatesRepository pmGoalStatesRepository, PmGoalStatesMapper pmGoalStatesMapper) {
        this.pmGoalStatesRepository = pmGoalStatesRepository;
        this.pmGoalStatesMapper = pmGoalStatesMapper;
    }

    /**
     * POST  /pm-goal-states : Create a new pmGoalStates.
     *
     * @param pmGoalStatesDTO the pmGoalStatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalStatesDTO, or with status 400 (Bad Request) if the pmGoalStates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-goal-states")
    @Timed
    public ResponseEntity<PmGoalStatesDTO> createPmGoalStates(@Valid @RequestBody PmGoalStatesDTO pmGoalStatesDTO) throws URISyntaxException {
        log.debug("REST request to save PmGoalStates : {}", pmGoalStatesDTO);
        if (pmGoalStatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmGoalStates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        while(pmGoalStatesRepository.findByCode(newCode) != null){
            newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        }
        pmGoalStatesDTO.setCode(newCode);
        PmGoalStates pmGoalStates = pmGoalStatesMapper.toEntity(pmGoalStatesDTO);
        pmGoalStates = pmGoalStatesRepository.save(pmGoalStates);
        PmGoalStatesDTO result = pmGoalStatesMapper.toDto(pmGoalStates);
        return ResponseEntity.created(new URI("/api/pm-goal-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-goal-states : Updates an existing pmGoalStates.
     *
     * @param pmGoalStatesDTO the pmGoalStatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalStatesDTO,
     * or with status 400 (Bad Request) if the pmGoalStatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmGoalStatesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-goal-states")
    @Timed
    public ResponseEntity<PmGoalStatesDTO> updatePmGoalStates(@Valid @RequestBody PmGoalStatesDTO pmGoalStatesDTO) throws URISyntaxException {
        log.debug("REST request to update PmGoalStates : {}", pmGoalStatesDTO);
        if (pmGoalStatesDTO.getId() == null) {
            return createPmGoalStates(pmGoalStatesDTO);
        }
        PmGoalStates pmGoalStates = pmGoalStatesMapper.toEntity(pmGoalStatesDTO);
        pmGoalStates = pmGoalStatesRepository.save(pmGoalStates);
        PmGoalStatesDTO result = pmGoalStatesMapper.toDto(pmGoalStates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalStatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-goal-states : get all the pmGoalStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmGoalStates in body
     */
    @GetMapping("/pm-goal-states")
    @Timed
    public ResponseEntity<List<PmGoalStatesDTO>> getAllPmGoalStates(Pageable pageable) {
        log.debug("REST request to get a page of PmGoalStates");
        Page<PmGoalStates> page = pmGoalStatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goal-states");
        return new ResponseEntity<>(pmGoalStatesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-goal-states/:id : get the "id" pmGoalStates.
     *
     * @param id the id of the pmGoalStatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmGoalStatesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-goal-states/{id}")
    @Timed
    public ResponseEntity<PmGoalStatesDTO> getPmGoalStates(@PathVariable Long id) {
        log.debug("REST request to get PmGoalStates : {}", id);
        PmGoalStates pmGoalStates = pmGoalStatesRepository.findOne(id);
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalStatesDTO));
    }

    /**
     * DELETE  /pm-goal-states/:id : delete the "id" pmGoalStates.
     *
     * @param id the id of the pmGoalStatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-goal-states/{id}")
    @Timed
    public ResponseEntity<Void> deletePmGoalStates(@PathVariable Long id) {
        log.debug("REST request to delete PmGoalStates : {}", id);
        pmGoalStatesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

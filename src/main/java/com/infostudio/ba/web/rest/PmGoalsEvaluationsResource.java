package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmGoalsEvaluations;

import com.infostudio.ba.repository.PmGoalsEvaluationsRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalsEvaluationsDTO;
import com.infostudio.ba.service.mapper.PmGoalsEvaluationsMapper;
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
 * REST controller for managing PmGoalsEvaluations.
 */
@RestController
@RequestMapping("/api")
public class PmGoalsEvaluationsResource {

    private final Logger log = LoggerFactory.getLogger(PmGoalsEvaluationsResource.class);

    private static final String ENTITY_NAME = "pmGoalsEvaluations";

    private final PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository;

    private final PmGoalsEvaluationsMapper pmGoalsEvaluationsMapper;

    public PmGoalsEvaluationsResource(PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository, PmGoalsEvaluationsMapper pmGoalsEvaluationsMapper) {
        this.pmGoalsEvaluationsRepository = pmGoalsEvaluationsRepository;
        this.pmGoalsEvaluationsMapper = pmGoalsEvaluationsMapper;
    }

    /**
     * POST  /pm-goals-evaluations : Create a new pmGoalsEvaluations.
     *
     * @param pmGoalsEvaluationsDTO the pmGoalsEvaluationsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalsEvaluationsDTO, or with status 400 (Bad Request) if the pmGoalsEvaluations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-goals-evaluations")
    @Timed
    public ResponseEntity<PmGoalsEvaluationsDTO> createPmGoalsEvaluations(@Valid @RequestBody PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO) throws URISyntaxException {
        log.debug("REST request to save PmGoalsEvaluations : {}", pmGoalsEvaluationsDTO);
        if (pmGoalsEvaluationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmGoalsEvaluations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsMapper.toEntity(pmGoalsEvaluationsDTO);
        pmGoalsEvaluations = pmGoalsEvaluationsRepository.save(pmGoalsEvaluations);
        PmGoalsEvaluationsDTO result = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
        return ResponseEntity.created(new URI("/api/pm-goals-evaluations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-goals-evaluations : Updates an existing pmGoalsEvaluations.
     *
     * @param pmGoalsEvaluationsDTO the pmGoalsEvaluationsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalsEvaluationsDTO,
     * or with status 400 (Bad Request) if the pmGoalsEvaluationsDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmGoalsEvaluationsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-goals-evaluations")
    @Timed
    public ResponseEntity<PmGoalsEvaluationsDTO> updatePmGoalsEvaluations(@Valid @RequestBody PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO) throws URISyntaxException {
        log.debug("REST request to update PmGoalsEvaluations : {}", pmGoalsEvaluationsDTO);
        if (pmGoalsEvaluationsDTO.getId() == null) {
            return createPmGoalsEvaluations(pmGoalsEvaluationsDTO);
        }
        PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsMapper.toEntity(pmGoalsEvaluationsDTO);
        pmGoalsEvaluations = pmGoalsEvaluationsRepository.save(pmGoalsEvaluations);
        PmGoalsEvaluationsDTO result = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalsEvaluationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-goals-evaluations : get all the pmGoalsEvaluations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmGoalsEvaluations in body
     */
    @GetMapping("/pm-goals-evaluations")
    @Timed
    public ResponseEntity<List<PmGoalsEvaluationsDTO>> getAllPmGoalsEvaluations(Pageable pageable) {
        log.debug("REST request to get a page of PmGoalsEvaluations");
        Page<PmGoalsEvaluations> page = pmGoalsEvaluationsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goals-evaluations");
        return new ResponseEntity<>(pmGoalsEvaluationsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-goals-evaluations/:id : get the "id" pmGoalsEvaluations.
     *
     * @param id the id of the pmGoalsEvaluationsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmGoalsEvaluationsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-goals-evaluations/{id}")
    @Timed
    public ResponseEntity<PmGoalsEvaluationsDTO> getPmGoalsEvaluations(@PathVariable Long id) {
        log.debug("REST request to get PmGoalsEvaluations : {}", id);
        PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsRepository.findOne(id);
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalsEvaluationsDTO));
    }

    /**
     * DELETE  /pm-goals-evaluations/:id : delete the "id" pmGoalsEvaluations.
     *
     * @param id the id of the pmGoalsEvaluationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-goals-evaluations/{id}")
    @Timed
    public ResponseEntity<Void> deletePmGoalsEvaluations(@PathVariable Long id) {
        log.debug("REST request to delete PmGoalsEvaluations : {}", id);
        pmGoalsEvaluationsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

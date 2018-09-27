package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmGoals;

import com.infostudio.ba.repository.PmGoalsRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalsDTO;
import com.infostudio.ba.service.mapper.PmGoalsMapper;
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
 * REST controller for managing PmGoals.
 */
@RestController
@RequestMapping("/api")
public class PmGoalsResource {

    private final Logger log = LoggerFactory.getLogger(PmGoalsResource.class);

    private static final String ENTITY_NAME = "pmGoals";

    private final PmGoalsRepository pmGoalsRepository;

    private final PmGoalsMapper pmGoalsMapper;

    public PmGoalsResource(PmGoalsRepository pmGoalsRepository, PmGoalsMapper pmGoalsMapper) {
        this.pmGoalsRepository = pmGoalsRepository;
        this.pmGoalsMapper = pmGoalsMapper;
    }

    /**
     * POST  /pm-goals : Create a new pmGoals.
     *
     * @param pmGoalsDTO the pmGoalsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalsDTO, or with status 400 (Bad Request) if the pmGoals has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-goals")
    @Timed
    public ResponseEntity<PmGoalsDTO> createPmGoals(@Valid @RequestBody PmGoalsDTO pmGoalsDTO) throws URISyntaxException {
        log.debug("REST request to save PmGoals : {}", pmGoalsDTO);
        if (pmGoalsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmGoals cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmGoals pmGoals = pmGoalsMapper.toEntity(pmGoalsDTO);
        pmGoals = pmGoalsRepository.save(pmGoals);
        PmGoalsDTO result = pmGoalsMapper.toDto(pmGoals);
        return ResponseEntity.created(new URI("/api/pm-goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-goals : Updates an existing pmGoals.
     *
     * @param pmGoalsDTO the pmGoalsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalsDTO,
     * or with status 400 (Bad Request) if the pmGoalsDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmGoalsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-goals")
    @Timed
    public ResponseEntity<PmGoalsDTO> updatePmGoals(@Valid @RequestBody PmGoalsDTO pmGoalsDTO) throws URISyntaxException {
        log.debug("REST request to update PmGoals : {}", pmGoalsDTO);
        if (pmGoalsDTO.getId() == null) {
            return createPmGoals(pmGoalsDTO);
        }
        PmGoals pmGoals = pmGoalsMapper.toEntity(pmGoalsDTO);
        pmGoals = pmGoalsRepository.save(pmGoals);
        PmGoalsDTO result = pmGoalsMapper.toDto(pmGoals);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-goals : get all the pmGoals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmGoals in body
     */
    @GetMapping("/pm-goals")
    @Timed
    public ResponseEntity<List<PmGoalsDTO>> getAllPmGoals(Pageable pageable) {
        log.debug("REST request to get a page of PmGoals");
        Page<PmGoals> page = pmGoalsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goals");
        return new ResponseEntity<>(pmGoalsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-goals/:id : get the "id" pmGoals.
     *
     * @param id the id of the pmGoalsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmGoalsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-goals/{id}")
    @Timed
    public ResponseEntity<PmGoalsDTO> getPmGoals(@PathVariable Long id) {
        log.debug("REST request to get PmGoals : {}", id);
        PmGoals pmGoals = pmGoalsRepository.findOne(id);
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalsDTO));
    }

    /**
     * DELETE  /pm-goals/:id : delete the "id" pmGoals.
     *
     * @param id the id of the pmGoalsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-goals/{id}")
    @Timed
    public ResponseEntity<Void> deletePmGoals(@PathVariable Long id) {
        log.debug("REST request to delete PmGoals : {}", id);
        pmGoalsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

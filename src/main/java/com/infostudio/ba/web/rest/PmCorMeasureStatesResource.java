package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmCorMeasureStates;

import com.infostudio.ba.repository.PmCorMeasureStatesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmCorMeasureStatesDTO;
import com.infostudio.ba.service.mapper.PmCorMeasureStatesMapper;
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
 * REST controller for managing PmCorMeasureStates.
 */
@RestController
@RequestMapping("/api")
public class PmCorMeasureStatesResource {

    private final Logger log = LoggerFactory.getLogger(PmCorMeasureStatesResource.class);

    private static final String ENTITY_NAME = "pmCorMeasureStates";

    private final PmCorMeasureStatesRepository pmCorMeasureStatesRepository;

    private final PmCorMeasureStatesMapper pmCorMeasureStatesMapper;

    public PmCorMeasureStatesResource(PmCorMeasureStatesRepository pmCorMeasureStatesRepository, PmCorMeasureStatesMapper pmCorMeasureStatesMapper) {
        this.pmCorMeasureStatesRepository = pmCorMeasureStatesRepository;
        this.pmCorMeasureStatesMapper = pmCorMeasureStatesMapper;
    }

    /**
     * POST  /pm-cor-measure-states : Create a new pmCorMeasureStates.
     *
     * @param pmCorMeasureStatesDTO the pmCorMeasureStatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmCorMeasureStatesDTO, or with status 400 (Bad Request) if the pmCorMeasureStates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-cor-measure-states")
    @Timed
    public ResponseEntity<PmCorMeasureStatesDTO> createPmCorMeasureStates(@Valid @RequestBody PmCorMeasureStatesDTO pmCorMeasureStatesDTO) throws URISyntaxException {
        log.debug("REST request to save PmCorMeasureStates : {}", pmCorMeasureStatesDTO);
        if (pmCorMeasureStatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmCorMeasureStates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmCorMeasureStates pmCorMeasureStates = pmCorMeasureStatesMapper.toEntity(pmCorMeasureStatesDTO);
        pmCorMeasureStates = pmCorMeasureStatesRepository.save(pmCorMeasureStates);
        PmCorMeasureStatesDTO result = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);
        return ResponseEntity.created(new URI("/api/pm-cor-measure-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-cor-measure-states : Updates an existing pmCorMeasureStates.
     *
     * @param pmCorMeasureStatesDTO the pmCorMeasureStatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmCorMeasureStatesDTO,
     * or with status 400 (Bad Request) if the pmCorMeasureStatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmCorMeasureStatesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-cor-measure-states")
    @Timed
    public ResponseEntity<PmCorMeasureStatesDTO> updatePmCorMeasureStates(@Valid @RequestBody PmCorMeasureStatesDTO pmCorMeasureStatesDTO) throws URISyntaxException {
        log.debug("REST request to update PmCorMeasureStates : {}", pmCorMeasureStatesDTO);
        if (pmCorMeasureStatesDTO.getId() == null) {
            return createPmCorMeasureStates(pmCorMeasureStatesDTO);
        }
        PmCorMeasureStates pmCorMeasureStates = pmCorMeasureStatesMapper.toEntity(pmCorMeasureStatesDTO);
        pmCorMeasureStates = pmCorMeasureStatesRepository.save(pmCorMeasureStates);
        PmCorMeasureStatesDTO result = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmCorMeasureStatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-cor-measure-states : get all the pmCorMeasureStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmCorMeasureStates in body
     */
    @GetMapping("/pm-cor-measure-states")
    @Timed
    public ResponseEntity<List<PmCorMeasureStatesDTO>> getAllPmCorMeasureStates(Pageable pageable) {
        log.debug("REST request to get a page of PmCorMeasureStates");
        Page<PmCorMeasureStates> page = pmCorMeasureStatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-cor-measure-states");
        return new ResponseEntity<>(pmCorMeasureStatesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-cor-measure-states/:id : get the "id" pmCorMeasureStates.
     *
     * @param id the id of the pmCorMeasureStatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmCorMeasureStatesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-cor-measure-states/{id}")
    @Timed
    public ResponseEntity<PmCorMeasureStatesDTO> getPmCorMeasureStates(@PathVariable Long id) {
        log.debug("REST request to get PmCorMeasureStates : {}", id);
        PmCorMeasureStates pmCorMeasureStates = pmCorMeasureStatesRepository.findOne(id);
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmCorMeasureStatesDTO));
    }

    /**
     * DELETE  /pm-cor-measure-states/:id : delete the "id" pmCorMeasureStates.
     *
     * @param id the id of the pmCorMeasureStatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-cor-measure-states/{id}")
    @Timed
    public ResponseEntity<Void> deletePmCorMeasureStates(@PathVariable Long id) {
        log.debug("REST request to delete PmCorMeasureStates : {}", id);
        pmCorMeasureStatesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

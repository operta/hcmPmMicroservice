package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmEvaluationStates;

import com.infostudio.ba.repository.PmEvaluationStatesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmEvaluationStatesDTO;
import com.infostudio.ba.service.mapper.PmEvaluationStatesMapper;
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
 * REST controller for managing PmEvaluationStates.
 */
@RestController
@RequestMapping("/api")
public class PmEvaluationStatesResource {

    private final Logger log = LoggerFactory.getLogger(PmEvaluationStatesResource.class);

    private static final String ENTITY_NAME = "pmEvaluationStates";

    private final PmEvaluationStatesRepository pmEvaluationStatesRepository;

    private final PmEvaluationStatesMapper pmEvaluationStatesMapper;

    public PmEvaluationStatesResource(PmEvaluationStatesRepository pmEvaluationStatesRepository, PmEvaluationStatesMapper pmEvaluationStatesMapper) {
        this.pmEvaluationStatesRepository = pmEvaluationStatesRepository;
        this.pmEvaluationStatesMapper = pmEvaluationStatesMapper;
    }

    /**
     * POST  /pm-evaluation-states : Create a new pmEvaluationStates.
     *
     * @param pmEvaluationStatesDTO the pmEvaluationStatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmEvaluationStatesDTO, or with status 400 (Bad Request) if the pmEvaluationStates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-evaluation-states")
    @Timed
    public ResponseEntity<PmEvaluationStatesDTO> createPmEvaluationStates(@Valid @RequestBody PmEvaluationStatesDTO pmEvaluationStatesDTO) throws URISyntaxException {
        log.debug("REST request to save PmEvaluationStates : {}", pmEvaluationStatesDTO);
        if (pmEvaluationStatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmEvaluationStates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmEvaluationStates pmEvaluationStates = pmEvaluationStatesMapper.toEntity(pmEvaluationStatesDTO);
        pmEvaluationStates = pmEvaluationStatesRepository.save(pmEvaluationStates);
        PmEvaluationStatesDTO result = pmEvaluationStatesMapper.toDto(pmEvaluationStates);
        return ResponseEntity.created(new URI("/api/pm-evaluation-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-evaluation-states : Updates an existing pmEvaluationStates.
     *
     * @param pmEvaluationStatesDTO the pmEvaluationStatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmEvaluationStatesDTO,
     * or with status 400 (Bad Request) if the pmEvaluationStatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmEvaluationStatesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-evaluation-states")
    @Timed
    public ResponseEntity<PmEvaluationStatesDTO> updatePmEvaluationStates(@Valid @RequestBody PmEvaluationStatesDTO pmEvaluationStatesDTO) throws URISyntaxException {
        log.debug("REST request to update PmEvaluationStates : {}", pmEvaluationStatesDTO);
        if (pmEvaluationStatesDTO.getId() == null) {
            return createPmEvaluationStates(pmEvaluationStatesDTO);
        }
        PmEvaluationStates pmEvaluationStates = pmEvaluationStatesMapper.toEntity(pmEvaluationStatesDTO);
        pmEvaluationStates = pmEvaluationStatesRepository.save(pmEvaluationStates);
        PmEvaluationStatesDTO result = pmEvaluationStatesMapper.toDto(pmEvaluationStates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmEvaluationStatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-evaluation-states : get all the pmEvaluationStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmEvaluationStates in body
     */
    @GetMapping("/pm-evaluation-states")
    @Timed
    public ResponseEntity<List<PmEvaluationStatesDTO>> getAllPmEvaluationStates(Pageable pageable) {
        log.debug("REST request to get a page of PmEvaluationStates");
        Page<PmEvaluationStates> page = pmEvaluationStatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-evaluation-states");
        return new ResponseEntity<>(pmEvaluationStatesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-evaluation-states/:id : get the "id" pmEvaluationStates.
     *
     * @param id the id of the pmEvaluationStatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmEvaluationStatesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-evaluation-states/{id}")
    @Timed
    public ResponseEntity<PmEvaluationStatesDTO> getPmEvaluationStates(@PathVariable Long id) {
        log.debug("REST request to get PmEvaluationStates : {}", id);
        PmEvaluationStates pmEvaluationStates = pmEvaluationStatesRepository.findOne(id);
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmEvaluationStatesDTO));
    }

    /**
     * DELETE  /pm-evaluation-states/:id : delete the "id" pmEvaluationStates.
     *
     * @param id the id of the pmEvaluationStatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-evaluation-states/{id}")
    @Timed
    public ResponseEntity<Void> deletePmEvaluationStates(@PathVariable Long id) {
        log.debug("REST request to delete PmEvaluationStates : {}", id);
        pmEvaluationStatesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

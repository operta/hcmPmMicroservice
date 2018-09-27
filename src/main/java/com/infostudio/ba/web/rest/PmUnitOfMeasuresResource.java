package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmUnitOfMeasures;

import com.infostudio.ba.repository.PmUnitOfMeasuresRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmUnitOfMeasuresDTO;
import com.infostudio.ba.service.mapper.PmUnitOfMeasuresMapper;
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
 * REST controller for managing PmUnitOfMeasures.
 */
@RestController
@RequestMapping("/api")
public class PmUnitOfMeasuresResource {

    private final Logger log = LoggerFactory.getLogger(PmUnitOfMeasuresResource.class);

    private static final String ENTITY_NAME = "pmUnitOfMeasures";

    private final PmUnitOfMeasuresRepository pmUnitOfMeasuresRepository;

    private final PmUnitOfMeasuresMapper pmUnitOfMeasuresMapper;

    public PmUnitOfMeasuresResource(PmUnitOfMeasuresRepository pmUnitOfMeasuresRepository, PmUnitOfMeasuresMapper pmUnitOfMeasuresMapper) {
        this.pmUnitOfMeasuresRepository = pmUnitOfMeasuresRepository;
        this.pmUnitOfMeasuresMapper = pmUnitOfMeasuresMapper;
    }

    /**
     * POST  /pm-unit-of-measures : Create a new pmUnitOfMeasures.
     *
     * @param pmUnitOfMeasuresDTO the pmUnitOfMeasuresDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmUnitOfMeasuresDTO, or with status 400 (Bad Request) if the pmUnitOfMeasures has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-unit-of-measures")
    @Timed
    public ResponseEntity<PmUnitOfMeasuresDTO> createPmUnitOfMeasures(@Valid @RequestBody PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO) throws URISyntaxException {
        log.debug("REST request to save PmUnitOfMeasures : {}", pmUnitOfMeasuresDTO);
        if (pmUnitOfMeasuresDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmUnitOfMeasures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmUnitOfMeasures pmUnitOfMeasures = pmUnitOfMeasuresMapper.toEntity(pmUnitOfMeasuresDTO);
        pmUnitOfMeasures = pmUnitOfMeasuresRepository.save(pmUnitOfMeasures);
        PmUnitOfMeasuresDTO result = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);
        return ResponseEntity.created(new URI("/api/pm-unit-of-measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-unit-of-measures : Updates an existing pmUnitOfMeasures.
     *
     * @param pmUnitOfMeasuresDTO the pmUnitOfMeasuresDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmUnitOfMeasuresDTO,
     * or with status 400 (Bad Request) if the pmUnitOfMeasuresDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmUnitOfMeasuresDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-unit-of-measures")
    @Timed
    public ResponseEntity<PmUnitOfMeasuresDTO> updatePmUnitOfMeasures(@Valid @RequestBody PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO) throws URISyntaxException {
        log.debug("REST request to update PmUnitOfMeasures : {}", pmUnitOfMeasuresDTO);
        if (pmUnitOfMeasuresDTO.getId() == null) {
            return createPmUnitOfMeasures(pmUnitOfMeasuresDTO);
        }
        PmUnitOfMeasures pmUnitOfMeasures = pmUnitOfMeasuresMapper.toEntity(pmUnitOfMeasuresDTO);
        pmUnitOfMeasures = pmUnitOfMeasuresRepository.save(pmUnitOfMeasures);
        PmUnitOfMeasuresDTO result = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmUnitOfMeasuresDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-unit-of-measures : get all the pmUnitOfMeasures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmUnitOfMeasures in body
     */
    @GetMapping("/pm-unit-of-measures")
    @Timed
    public ResponseEntity<List<PmUnitOfMeasuresDTO>> getAllPmUnitOfMeasures(Pageable pageable) {
        log.debug("REST request to get a page of PmUnitOfMeasures");
        Page<PmUnitOfMeasures> page = pmUnitOfMeasuresRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-unit-of-measures");
        return new ResponseEntity<>(pmUnitOfMeasuresMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-unit-of-measures/:id : get the "id" pmUnitOfMeasures.
     *
     * @param id the id of the pmUnitOfMeasuresDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmUnitOfMeasuresDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-unit-of-measures/{id}")
    @Timed
    public ResponseEntity<PmUnitOfMeasuresDTO> getPmUnitOfMeasures(@PathVariable Long id) {
        log.debug("REST request to get PmUnitOfMeasures : {}", id);
        PmUnitOfMeasures pmUnitOfMeasures = pmUnitOfMeasuresRepository.findOne(id);
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmUnitOfMeasuresDTO));
    }

    /**
     * DELETE  /pm-unit-of-measures/:id : delete the "id" pmUnitOfMeasures.
     *
     * @param id the id of the pmUnitOfMeasuresDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-unit-of-measures/{id}")
    @Timed
    public ResponseEntity<Void> deletePmUnitOfMeasures(@PathVariable Long id) {
        log.debug("REST request to delete PmUnitOfMeasures : {}", id);
        pmUnitOfMeasuresRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

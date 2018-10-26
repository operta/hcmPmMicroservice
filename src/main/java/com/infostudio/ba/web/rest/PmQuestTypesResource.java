package com.infostudio.ba.web.rest;


import org.apache.commons.lang.RandomStringUtils;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmQuestTypes;

import com.infostudio.ba.repository.PmQuestTypesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestTypesDTO;
import com.infostudio.ba.service.mapper.PmQuestTypesMapper;
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
 * REST controller for managing PmQuestTypes.
 */
@RestController
@RequestMapping("/api")
public class PmQuestTypesResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestTypesResource.class);

    private static final String ENTITY_NAME = "pmQuestTypes";

    private final PmQuestTypesRepository pmQuestTypesRepository;

    private final PmQuestTypesMapper pmQuestTypesMapper;

    public PmQuestTypesResource(PmQuestTypesRepository pmQuestTypesRepository, PmQuestTypesMapper pmQuestTypesMapper) {
        this.pmQuestTypesRepository = pmQuestTypesRepository;
        this.pmQuestTypesMapper = pmQuestTypesMapper;
    }

    /**
     * POST  /pm-quest-types : Create a new pmQuestTypes.
     *
     * @param pmQuestTypesDTO the pmQuestTypesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestTypesDTO, or with status 400 (Bad Request) if the pmQuestTypes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-quest-types")
    @Timed
    public ResponseEntity<PmQuestTypesDTO> createPmQuestTypes(@Valid @RequestBody PmQuestTypesDTO pmQuestTypesDTO) throws URISyntaxException {
        log.debug("REST request to save PmQuestTypes : {}", pmQuestTypesDTO);
        if (pmQuestTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        while(pmQuestTypesRepository.findByCode(newCode) != null){
            newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        }
        pmQuestTypesDTO.setCode(newCode);
        PmQuestTypes pmQuestTypes = pmQuestTypesMapper.toEntity(pmQuestTypesDTO);
        pmQuestTypes = pmQuestTypesRepository.save(pmQuestTypes);
        PmQuestTypesDTO result = pmQuestTypesMapper.toDto(pmQuestTypes);
        return ResponseEntity.created(new URI("/api/pm-quest-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-quest-types : Updates an existing pmQuestTypes.
     *
     * @param pmQuestTypesDTO the pmQuestTypesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestTypesDTO,
     * or with status 400 (Bad Request) if the pmQuestTypesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestTypesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-quest-types")
    @Timed
    public ResponseEntity<PmQuestTypesDTO> updatePmQuestTypes(@Valid @RequestBody PmQuestTypesDTO pmQuestTypesDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestTypes : {}", pmQuestTypesDTO);
        if (pmQuestTypesDTO.getId() == null) {
            return createPmQuestTypes(pmQuestTypesDTO);
        }
        PmQuestTypes pmQuestTypes = pmQuestTypesMapper.toEntity(pmQuestTypesDTO);
        pmQuestTypes = pmQuestTypesRepository.save(pmQuestTypes);
        PmQuestTypesDTO result = pmQuestTypesMapper.toDto(pmQuestTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-quest-types : get all the pmQuestTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestTypes in body
     */
    @GetMapping("/pm-quest-types")
    @Timed
    public ResponseEntity<List<PmQuestTypesDTO>> getAllPmQuestTypes(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestTypes");
        Page<PmQuestTypes> page = pmQuestTypesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-quest-types");
        return new ResponseEntity<>(pmQuestTypesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-quest-types/:id : get the "id" pmQuestTypes.
     *
     * @param id the id of the pmQuestTypesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestTypesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-quest-types/{id}")
    @Timed
    public ResponseEntity<PmQuestTypesDTO> getPmQuestTypes(@PathVariable Long id) {
        log.debug("REST request to get PmQuestTypes : {}", id);
        PmQuestTypes pmQuestTypes = pmQuestTypesRepository.findOne(id);
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestTypesDTO));
    }

    /**
     * DELETE  /pm-quest-types/:id : delete the "id" pmQuestTypes.
     *
     * @param id the id of the pmQuestTypesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-quest-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestTypes(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestTypes : {}", id);
        pmQuestTypesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

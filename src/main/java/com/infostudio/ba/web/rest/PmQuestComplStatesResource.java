package com.infostudio.ba.web.rest;

import org.apache.commons.lang.RandomStringUtils;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmQuestComplStates;

import com.infostudio.ba.repository.PmQuestComplStatesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestComplStatesDTO;
import com.infostudio.ba.service.mapper.PmQuestComplStatesMapper;
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
 * REST controller for managing PmQuestComplStates.
 */
@RestController
@RequestMapping("/api")
public class PmQuestComplStatesResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestComplStatesResource.class);

    private static final String ENTITY_NAME = "pmQuestComplStates";

    private final PmQuestComplStatesRepository pmQuestComplStatesRepository;

    private final PmQuestComplStatesMapper pmQuestComplStatesMapper;

    public PmQuestComplStatesResource(PmQuestComplStatesRepository pmQuestComplStatesRepository, PmQuestComplStatesMapper pmQuestComplStatesMapper) {
        this.pmQuestComplStatesRepository = pmQuestComplStatesRepository;
        this.pmQuestComplStatesMapper = pmQuestComplStatesMapper;
    }

    /**
     * POST  /pm-quest-compl-states : Create a new pmQuestComplStates.
     *
     * @param pmQuestComplStatesDTO the pmQuestComplStatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestComplStatesDTO, or with status 400 (Bad Request) if the pmQuestComplStates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-quest-compl-states")
    @Timed
    public ResponseEntity<PmQuestComplStatesDTO> createPmQuestComplStates(@Valid @RequestBody PmQuestComplStatesDTO pmQuestComplStatesDTO) throws URISyntaxException {
        log.debug("REST request to save PmQuestComplStates : {}", pmQuestComplStatesDTO);
        if (pmQuestComplStatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestComplStates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        while(pmQuestComplStatesRepository.findByCode(newCode) != null){
            newCode = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        }
        pmQuestComplStatesDTO.setCode(newCode);
        PmQuestComplStates pmQuestComplStates = pmQuestComplStatesMapper.toEntity(pmQuestComplStatesDTO);
        pmQuestComplStates = pmQuestComplStatesRepository.save(pmQuestComplStates);
        PmQuestComplStatesDTO result = pmQuestComplStatesMapper.toDto(pmQuestComplStates);
        return ResponseEntity.created(new URI("/api/pm-quest-compl-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-quest-compl-states : Updates an existing pmQuestComplStates.
     *
     * @param pmQuestComplStatesDTO the pmQuestComplStatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestComplStatesDTO,
     * or with status 400 (Bad Request) if the pmQuestComplStatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestComplStatesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-quest-compl-states")
    @Timed
    public ResponseEntity<PmQuestComplStatesDTO> updatePmQuestComplStates(@Valid @RequestBody PmQuestComplStatesDTO pmQuestComplStatesDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestComplStates : {}", pmQuestComplStatesDTO);
        if (pmQuestComplStatesDTO.getId() == null) {
            return createPmQuestComplStates(pmQuestComplStatesDTO);
        }
        PmQuestComplStates pmQuestComplStates = pmQuestComplStatesMapper.toEntity(pmQuestComplStatesDTO);
        pmQuestComplStates = pmQuestComplStatesRepository.save(pmQuestComplStates);
        PmQuestComplStatesDTO result = pmQuestComplStatesMapper.toDto(pmQuestComplStates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestComplStatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-quest-compl-states : get all the pmQuestComplStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestComplStates in body
     */
    @GetMapping("/pm-quest-compl-states")
    @Timed
    public ResponseEntity<List<PmQuestComplStatesDTO>> getAllPmQuestComplStates(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestComplStates");
        Page<PmQuestComplStates> page = pmQuestComplStatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-quest-compl-states");
        return new ResponseEntity<>(pmQuestComplStatesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-quest-compl-states/:id : get the "id" pmQuestComplStates.
     *
     * @param id the id of the pmQuestComplStatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestComplStatesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-quest-compl-states/{id}")
    @Timed
    public ResponseEntity<PmQuestComplStatesDTO> getPmQuestComplStates(@PathVariable Long id) {
        log.debug("REST request to get PmQuestComplStates : {}", id);
        PmQuestComplStates pmQuestComplStates = pmQuestComplStatesRepository.findOne(id);
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestComplStatesDTO));
    }

    /**
     * DELETE  /pm-quest-compl-states/:id : delete the "id" pmQuestComplStates.
     *
     * @param id the id of the pmQuestComplStatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-quest-compl-states/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestComplStates(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestComplStates : {}", id);
        pmQuestComplStatesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

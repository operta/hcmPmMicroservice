package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import com.infostudio.ba.domain.PmQuestionnaires;

import com.infostudio.ba.repository.PmQuestionnairesRepository;
import com.infostudio.ba.service.dto.PmQuestCompletionsDTO;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestionnairesDTO;
import com.infostudio.ba.service.mapper.PmQuestionnairesMapper;
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
 * REST controller for managing PmQuestionnaires.
 */
@RestController
@RequestMapping("/api")
public class PmQuestionnairesResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestionnairesResource.class);

    private static final String ENTITY_NAME = "pmQuestionnaires";

    private final PmQuestionnairesRepository pmQuestionnairesRepository;

    private final PmQuestionnairesMapper pmQuestionnairesMapper;

    public PmQuestionnairesResource(PmQuestionnairesRepository pmQuestionnairesRepository, PmQuestionnairesMapper pmQuestionnairesMapper) {
        this.pmQuestionnairesRepository = pmQuestionnairesRepository;
        this.pmQuestionnairesMapper = pmQuestionnairesMapper;
    }

    /**
     * POST  /pm-questionnaires : Create a new pmQuestionnaires.
     *
     * @param pmQuestionnairesDTO the pmQuestionnairesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestionnairesDTO, or with status 400 (Bad Request) if the pmQuestionnaires has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-questionnaires")
    @Timed
    public ResponseEntity<PmQuestionnairesDTO> createPmQuestionnaires(@Valid @RequestBody PmQuestionnairesDTO pmQuestionnairesDTO) throws URISyntaxException {
        log.debug("REST request to save PmQuestionnaires : {}", pmQuestionnairesDTO);
        if (pmQuestionnairesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestionnaires cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmQuestionnaires pmQuestionnaires = pmQuestionnairesMapper.toEntity(pmQuestionnairesDTO);
        pmQuestionnaires = pmQuestionnairesRepository.save(pmQuestionnaires);
        PmQuestionnairesDTO result = pmQuestionnairesMapper.toDto(pmQuestionnaires);
        return ResponseEntity.created(new URI("/api/pm-questionnaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-questionnaires : Updates an existing pmQuestionnaires.
     *
     * @param pmQuestionnairesDTO the pmQuestionnairesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestionnairesDTO,
     * or with status 400 (Bad Request) if the pmQuestionnairesDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestionnairesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-questionnaires")
    @Timed
    public ResponseEntity<PmQuestionnairesDTO> updatePmQuestionnaires(@Valid @RequestBody PmQuestionnairesDTO pmQuestionnairesDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestionnaires : {}", pmQuestionnairesDTO);
        if (pmQuestionnairesDTO.getId() == null) {
            return createPmQuestionnaires(pmQuestionnairesDTO);
        }
        PmQuestionnaires pmQuestionnaires = pmQuestionnairesMapper.toEntity(pmQuestionnairesDTO);
        pmQuestionnaires = pmQuestionnairesRepository.save(pmQuestionnaires);
        PmQuestionnairesDTO result = pmQuestionnairesMapper.toDto(pmQuestionnaires);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestionnairesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-questionnaires : get all the pmQuestionnaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestionnaires in body
     */
    @GetMapping("/pm-questionnaires")
    @Timed
    public ResponseEntity<List<PmQuestionnairesDTO>> getAllPmQuestionnaires(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestionnaires");
        Page<PmQuestionnaires> page = pmQuestionnairesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-questionnaires");
        return new ResponseEntity<>(pmQuestionnairesMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-questionnaires/:id : get the "id" pmQuestionnaires.
     *
     * @param id the id of the pmQuestionnairesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestionnairesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-questionnaires/{id}")
    @Timed
    public ResponseEntity<PmQuestionnairesDTO> getPmQuestionnaires(@PathVariable Long id) {
        log.debug("REST request to get PmQuestionnaires : {}", id);
        PmQuestionnaires pmQuestionnaires = pmQuestionnairesRepository.findOne(id);
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestionnairesDTO));
    }

    @GetMapping("/pm-questionnaires/header/{id}")
    @Timed
    public ResponseEntity<List<PmQuestionnairesDTO>> getPmQuestionnairesByHeaderId(@PathVariable Long id){
        log.debug("REST request to get PmQuestionnaires by Header id: {}", id);
        List<PmQuestionnaires> pmQuestionnaires = pmQuestionnairesRepository.findByIdHeader(id);
        List<PmQuestionnairesDTO> pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);
        return ResponseEntity.ok(pmQuestionnairesDTO);
    }

    /**
     * DELETE  /pm-questionnaires/:id : delete the "id" pmQuestionnaires.
     *
     * @param id the id of the pmQuestionnairesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-questionnaires/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestionnaires(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestionnaires : {}", id);
        pmQuestionnairesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

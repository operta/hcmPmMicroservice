package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmGoalEvalQstCompl;

import com.infostudio.ba.repository.PmGoalEvalQstComplRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalEvalQstComplDTO;
import com.infostudio.ba.service.mapper.PmGoalEvalQstComplMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PmGoalEvalQstCompl.
 */
@RestController
@RequestMapping("/api")
public class PmGoalEvalQstComplResource {

    private final Logger log = LoggerFactory.getLogger(PmGoalEvalQstComplResource.class);

    private static final String ENTITY_NAME = "pmGoalEvalQstCompl";

    private final PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository;

    private final PmGoalEvalQstComplMapper pmGoalEvalQstComplMapper;

    public PmGoalEvalQstComplResource(PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository, PmGoalEvalQstComplMapper pmGoalEvalQstComplMapper) {
        this.pmGoalEvalQstComplRepository = pmGoalEvalQstComplRepository;
        this.pmGoalEvalQstComplMapper = pmGoalEvalQstComplMapper;
    }

    /**
     * POST  /pm-goal-eval-qst-compls : Create a new pmGoalEvalQstCompl.
     *
     * @param pmGoalEvalQstComplDTO the pmGoalEvalQstComplDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalEvalQstComplDTO, or with status 400 (Bad Request) if the pmGoalEvalQstCompl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-goal-eval-qst-compls")
    @Timed
    public ResponseEntity<PmGoalEvalQstComplDTO> createPmGoalEvalQstCompl(@RequestBody PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO) throws URISyntaxException {
        log.debug("REST request to save PmGoalEvalQstCompl : {}", pmGoalEvalQstComplDTO);
        if (pmGoalEvalQstComplDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmGoalEvalQstCompl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmGoalEvalQstCompl pmGoalEvalQstCompl = pmGoalEvalQstComplMapper.toEntity(pmGoalEvalQstComplDTO);
        pmGoalEvalQstCompl = pmGoalEvalQstComplRepository.save(pmGoalEvalQstCompl);
        PmGoalEvalQstComplDTO result = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);
        return ResponseEntity.created(new URI("/api/pm-goal-eval-qst-compls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-goal-eval-qst-compls : Updates an existing pmGoalEvalQstCompl.
     *
     * @param pmGoalEvalQstComplDTO the pmGoalEvalQstComplDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalEvalQstComplDTO,
     * or with status 400 (Bad Request) if the pmGoalEvalQstComplDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmGoalEvalQstComplDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-goal-eval-qst-compls")
    @Timed
    public ResponseEntity<PmGoalEvalQstComplDTO> updatePmGoalEvalQstCompl(@RequestBody PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO) throws URISyntaxException {
        log.debug("REST request to update PmGoalEvalQstCompl : {}", pmGoalEvalQstComplDTO);
        if (pmGoalEvalQstComplDTO.getId() == null) {
            return createPmGoalEvalQstCompl(pmGoalEvalQstComplDTO);
        }
        PmGoalEvalQstCompl pmGoalEvalQstCompl = pmGoalEvalQstComplMapper.toEntity(pmGoalEvalQstComplDTO);
        pmGoalEvalQstCompl = pmGoalEvalQstComplRepository.save(pmGoalEvalQstCompl);
        PmGoalEvalQstComplDTO result = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalEvalQstComplDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-goal-eval-qst-compls : get all the pmGoalEvalQstCompls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmGoalEvalQstCompls in body
     */
    @GetMapping("/pm-goal-eval-qst-compls")
    @Timed
    public ResponseEntity<List<PmGoalEvalQstComplDTO>> getAllPmGoalEvalQstCompls(Pageable pageable) {
        log.debug("REST request to get a page of PmGoalEvalQstCompls");
        Page<PmGoalEvalQstCompl> page = pmGoalEvalQstComplRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goal-eval-qst-compls");
        return new ResponseEntity<>(pmGoalEvalQstComplMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping("/pm-goal-eval-qst-compls/quest-completions/{id}")
    public ResponseEntity<List<PmGoalEvalQstComplDTO>> getAllByIdQuestCompletions(@PathVariable Long id){
        log.debug("REST requst to get all PmGoalEvalQstCompls by PmQuestCompletions id: {}", id);
        List<PmGoalEvalQstCompl> pmGoalEvalQstCompls = pmGoalEvalQstComplRepository.findByIdQuestionaireCompletionId(id);

        return ResponseEntity.ok(pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompls));
    }

    @GetMapping("/pm-goal-eval-qst-compls/goal-evaluation/{id}")
    public ResponseEntity<List<PmGoalEvalQstComplDTO>> getAllPmGoalEvalQstComplsByGoalEvalId(@PathVariable Long id){
        log.debug("REST request to get all the PmGoalEvalQstCompls by idGoalEvaluationId: ", id);
        List<PmGoalEvalQstCompl> allPmGoalEvalQstCompl = pmGoalEvalQstComplRepository.findByIdGoalEvaluationId(id);
        return new ResponseEntity<>(pmGoalEvalQstComplMapper.toDto(allPmGoalEvalQstCompl), HttpStatus.OK);
    }

    /**
     * GET  /pm-goal-eval-qst-compls/:id : get the "id" pmGoalEvalQstCompl.
     *
     * @param id the id of the pmGoalEvalQstComplDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmGoalEvalQstComplDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-goal-eval-qst-compls/{id}")
    @Timed
    public ResponseEntity<PmGoalEvalQstComplDTO> getPmGoalEvalQstCompl(@PathVariable Long id) {
        log.debug("REST request to get PmGoalEvalQstCompl : {}", id);
        PmGoalEvalQstCompl pmGoalEvalQstCompl = pmGoalEvalQstComplRepository.findOne(id);
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalEvalQstComplDTO));
    }

    /**
     * DELETE  /pm-goal-eval-qst-compls/:id : delete the "id" pmGoalEvalQstCompl.
     *
     * @param id the id of the pmGoalEvalQstComplDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-goal-eval-qst-compls/{id}")
    @Timed
    public ResponseEntity<Void> deletePmGoalEvalQstCompl(@PathVariable Long id) {
        log.debug("REST request to delete PmGoalEvalQstCompl : {}", id);
        pmGoalEvalQstComplRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

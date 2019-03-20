package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.Action;
import com.infostudio.ba.domain.PmGoalEvalQstCompl;
import com.infostudio.ba.domain.PmGoalsEvaluations;
import com.infostudio.ba.domain.PmQuestCompletions;

import com.infostudio.ba.repository.PmGoalEvalQstComplRepository;
import com.infostudio.ba.repository.PmGoalsEvaluationsRepository;
import com.infostudio.ba.repository.PmQuestCompletionsRepository;
import com.infostudio.ba.service.mapper.PmGoalEvalQstComplMapper;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.AuditUtil;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestCompletionsDTO;
import com.infostudio.ba.service.mapper.PmQuestCompletionsMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
 * REST controller for managing PmQuestCompletions.
 */
@RestController
@RequestMapping("/api")
public class PmQuestCompletionsResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestCompletionsResource.class);

    private static final String ENTITY_NAME = "pmQuestCompletions";

    private final PmQuestCompletionsRepository pmQuestCompletionsRepository;

    private final PmQuestCompletionsMapper pmQuestCompletionsMapper;

    private final PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository;

    private final PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public PmQuestCompletionsResource(PmQuestCompletionsRepository pmQuestCompletionsRepository,
                                      PmQuestCompletionsMapper pmQuestCompletionsMapper,
                                      PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository,
                                      PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository,
                                      ApplicationEventPublisher applicationEventPublisher) {
        this.pmQuestCompletionsRepository = pmQuestCompletionsRepository;
        this.pmQuestCompletionsMapper = pmQuestCompletionsMapper;
        this.pmGoalEvalQstComplRepository = pmGoalEvalQstComplRepository;
        this.pmGoalsEvaluationsRepository = pmGoalsEvaluationsRepository;
        this.applicationEventPublisher = applicationEventPublisher;

    }

    private PmQuestCompletionsDTO basicPostCheckForPmQuestCompletions(PmQuestCompletionsDTO pmQuestCompletionsDTO){
        log.debug("REST request to save PmQuestCompletions : {}", pmQuestCompletionsDTO);
        if (pmQuestCompletionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestCompletions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmQuestCompletions pmQuestCompletions = pmQuestCompletionsMapper.toEntity(pmQuestCompletionsDTO);
        pmQuestCompletions = pmQuestCompletionsRepository.save(pmQuestCompletions);
        PmQuestCompletionsDTO result = pmQuestCompletionsMapper.toDto(pmQuestCompletions);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                result.getCreatedBy(),
                "performance",
                ENTITY_NAME,
                result.getId().toString(),
                Action.POST
            )
        );
        return result;
    }

    /**
     * POST  /pm-quest-completions : Create a new pmQuestCompletions.
     *
     * @param pmQuestCompletionsDTO the pmQuestCompletionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestCompletionsDTO, or with status 400 (Bad Request) if the pmQuestCompletions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-quest-completions")
    public ResponseEntity<PmQuestCompletionsDTO> createPmQuestCompletions(@Valid @RequestBody PmQuestCompletionsDTO pmQuestCompletionsDTO) throws URISyntaxException {
        PmQuestCompletionsDTO result = basicPostCheckForPmQuestCompletions(pmQuestCompletionsDTO);
        return ResponseEntity.created(new URI("/api/pm-quest-completions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/pm-quest-completions/goal-evaluation/{id}")
    @Timed
    public ResponseEntity<PmQuestCompletionsDTO> createPmQuestCompletions(@PathVariable Long id, @Valid @RequestBody PmQuestCompletionsDTO pmQuestCompletionsDTO) throws URISyntaxException {
        PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsRepository.findOne(id);
        if(pmGoalsEvaluations == null){
            throw new BadRequestAlertException("Goal evaluation with id " + id + " does not exist",
                ENTITY_NAME, "goalevaluationdoesnotexist");
        }

        PmQuestCompletionsDTO result = basicPostCheckForPmQuestCompletions(pmQuestCompletionsDTO);


        PmGoalEvalQstCompl pmGoalEvalQstCompl = new PmGoalEvalQstCompl();
        pmGoalEvalQstCompl.setIdGoalEvaluation(new PmGoalsEvaluations().withId(id));
        pmGoalEvalQstCompl.setIdQuestionaireCompletion(new PmQuestCompletions().withId(result.getId()));
        pmGoalEvalQstCompl.setDescription("Automatically created!");

        log.debug("Chain request to save PmGoalEvalQstCompl: {}", pmGoalEvalQstCompl);
        pmGoalEvalQstComplRepository.save(pmGoalEvalQstCompl);

        return ResponseEntity.created(new URI("/api/pm-quest-completions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-quest-completions : Updates an existing pmQuestCompletions.
     *
     * @param pmQuestCompletionsDTO the pmQuestCompletionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestCompletionsDTO,
     * or with status 400 (Bad Request) if the pmQuestCompletionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestCompletionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-quest-completions")
    @Timed
    public ResponseEntity<PmQuestCompletionsDTO> updatePmQuestCompletions(@Valid @RequestBody PmQuestCompletionsDTO pmQuestCompletionsDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestCompletions : {}", pmQuestCompletionsDTO);
        if (pmQuestCompletionsDTO.getId() == null) {
            return createPmQuestCompletions(pmQuestCompletionsDTO);
        }
        PmQuestCompletions pmQuestCompletions = pmQuestCompletionsMapper.toEntity(pmQuestCompletionsDTO);
        pmQuestCompletions = pmQuestCompletionsRepository.save(pmQuestCompletions);
        PmQuestCompletionsDTO result = pmQuestCompletionsMapper.toDto(pmQuestCompletions);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                result.getUpdatedBy(),
                "performance",
                ENTITY_NAME,
                result.getId().toString(),
                Action.PUT
            )
        );
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestCompletionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-quest-completions : get all the pmQuestCompletions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestCompletions in body
     */
    @GetMapping("/pm-quest-completions")
    @Timed
    public ResponseEntity<List<PmQuestCompletionsDTO>> getAllPmQuestCompletions(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestCompletions");
        Page<PmQuestCompletions> page = pmQuestCompletionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-quest-completions");
        return new ResponseEntity<>(pmQuestCompletionsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-quest-completions/:id : get the "id" pmQuestCompletions.
     *
     * @param id the id of the pmQuestCompletionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestCompletionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-quest-completions/{id}")
    @Timed
    public ResponseEntity<PmQuestCompletionsDTO> getPmQuestCompletions(@PathVariable Long id) {
        log.debug("REST request to get PmQuestCompletions : {}", id);
        PmQuestCompletions pmQuestCompletions = pmQuestCompletionsRepository.findOne(id);
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestCompletionsDTO));
    }

    /**
     * DELETE  /pm-quest-completions/:id : delete the "id" pmQuestCompletions.
     *
     * @param id the id of the pmQuestCompletionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-quest-completions/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestCompletions(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestCompletions : {}", id);
        PmQuestCompletions questCompletion = pmQuestCompletionsRepository.findOne(id);
        PmQuestCompletionsDTO questCompletionsDTO = pmQuestCompletionsMapper.toDto(questCompletion);
        pmQuestCompletionsRepository.delete(id);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                questCompletionsDTO.getUpdatedBy(),
                "performance",
                ENTITY_NAME,
                id.toString(),
                Action.DELETE
            )
        );
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

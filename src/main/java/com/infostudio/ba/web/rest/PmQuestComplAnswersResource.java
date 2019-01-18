package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmQuestComplAnswers;

import com.infostudio.ba.domain.PmQuestCompletions;
import com.infostudio.ba.domain.PmQuestQuestions;
import com.infostudio.ba.repository.PmQuestComplAnswersRepository;
import com.infostudio.ba.repository.PmQuestCompletionsRepository;
import com.infostudio.ba.repository.PmQuestQuestionsRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestComplAnswersDTO;
import com.infostudio.ba.service.mapper.PmQuestComplAnswersMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.checkerframework.checker.units.qual.Time;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PmQuestComplAnswers.
 */
@RestController
@RequestMapping("/api")
public class PmQuestComplAnswersResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestComplAnswersResource.class);

    private static final String ENTITY_NAME = "pmQuestComplAnswers";

    private final PmQuestComplAnswersRepository pmQuestComplAnswersRepository;

    private final PmQuestComplAnswersMapper pmQuestComplAnswersMapper;

    private final PmQuestQuestionsRepository pmQuestQuestionsRepository;

    private final PmQuestCompletionsRepository pmQuestCompletionsRepository;

    public PmQuestComplAnswersResource(PmQuestComplAnswersRepository pmQuestComplAnswersRepository, PmQuestComplAnswersMapper pmQuestComplAnswersMapper,
                                       PmQuestQuestionsRepository pmQuestQuestionsRepository,
                                       PmQuestCompletionsRepository pmQuestCompletionsRepository) {
        this.pmQuestComplAnswersRepository = pmQuestComplAnswersRepository;
        this.pmQuestComplAnswersMapper = pmQuestComplAnswersMapper;
        this.pmQuestQuestionsRepository = pmQuestQuestionsRepository;
        this.pmQuestCompletionsRepository = pmQuestCompletionsRepository;
    }

    private void pmQuestComplAnswerCreationCheck(PmQuestComplAnswersDTO pmQuestComplAnswersDTO) {
        if (pmQuestComplAnswersDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestComplAnswers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Long questionId = pmQuestComplAnswersDTO.getIdQuestionnaireQuestionId();
        if (questionId == null) {
            throw new BadRequestAlertException("A new pmQuestComplAnswers must have a Question associated with it",
                ENTITY_NAME, "questionidnull");
        }
        Long questionnaireCompletionId = pmQuestComplAnswersDTO.getIdQuestionnaireCompletionId();
        if (questionnaireCompletionId == null) {
            throw new BadRequestAlertException("A new pmQuestComplAnswers must have a completed questionnaire associated with it",
                ENTITY_NAME, "questionnairecompletiondidnull");
        }

        PmQuestQuestions question = pmQuestQuestionsRepository.findOne(questionId);
        String errorMessage = null;
        if (question == null) {
            errorMessage = "Question with id " + questionId + " does not exist";
            throw new BadRequestAlertException(errorMessage, ENTITY_NAME, "questiondoesnotexist");
        }

        PmQuestCompletions questCompletions = pmQuestCompletionsRepository.findOne(questionnaireCompletionId);
        if(questCompletions == null) {
            errorMessage = "Questionnaire completion with id " + questionnaireCompletionId + " does not exist";
            throw new BadRequestAlertException(errorMessage, ENTITY_NAME, "questionnairecompletiondoesnotexist");
        }
    }

    /**
     * POST  /pm-quest-compl-answers : Create a new pmQuestComplAnswers.
     *
     * @param pmQuestComplAnswersDTO the pmQuestComplAnswersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestComplAnswersDTO, or with status 400 (Bad Request) if the pmQuestComplAnswers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-quest-compl-answers")
    @Timed
    public ResponseEntity<PmQuestComplAnswersDTO> createPmQuestComplAnswers(@Valid @RequestBody PmQuestComplAnswersDTO pmQuestComplAnswersDTO) throws URISyntaxException {
        log.debug("REST request to save PmQuestComplAnswers : {}", pmQuestComplAnswersDTO);

        this.pmQuestComplAnswerCreationCheck(pmQuestComplAnswersDTO);

        PmQuestComplAnswers pmQuestComplAnswers = pmQuestComplAnswersMapper.toEntity(pmQuestComplAnswersDTO);
        pmQuestComplAnswers = pmQuestComplAnswersRepository.save(pmQuestComplAnswers);
        PmQuestComplAnswersDTO result = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);
        return ResponseEntity.created(new URI("/api/pm-quest-compl-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/pm-quest-compl-answers/multiple")
    @Time
    public ResponseEntity<List<PmQuestComplAnswersDTO>> createPmQuestComplAnswersFromAList(@RequestBody List<PmQuestComplAnswersDTO> pmQuestComplAnswers) throws URISyntaxException {
        log.debug("REST request to save PmQuestCompLAnswers list: {}", pmQuestComplAnswers);

        List<PmQuestComplAnswersDTO> clientOutput = new ArrayList<>();
        for (PmQuestComplAnswersDTO answers : pmQuestComplAnswers) {
            this.pmQuestComplAnswerCreationCheck(answers);
        }
        for (PmQuestComplAnswersDTO answers : pmQuestComplAnswers) {
            clientOutput.add(createPmQuestComplAnswers(answers).getBody());
        }
        return ResponseEntity.created(URI.create("/api/pm-quest-compl-answers"))
            .body(clientOutput);
    }

    /**
     * PUT  /pm-quest-compl-answers : Updates an existing pmQuestComplAnswers.
     *
     * @param pmQuestComplAnswersDTO the pmQuestComplAnswersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestComplAnswersDTO,
     * or with status 400 (Bad Request) if the pmQuestComplAnswersDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestComplAnswersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-quest-compl-answers")
    @Timed
    public ResponseEntity<PmQuestComplAnswersDTO> updatePmQuestComplAnswers(@Valid @RequestBody PmQuestComplAnswersDTO pmQuestComplAnswersDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestComplAnswers : {}", pmQuestComplAnswersDTO);
        if (pmQuestComplAnswersDTO.getId() == null) {
            return createPmQuestComplAnswers(pmQuestComplAnswersDTO);
        }
        PmQuestComplAnswers pmQuestComplAnswers = pmQuestComplAnswersMapper.toEntity(pmQuestComplAnswersDTO);
        pmQuestComplAnswers = pmQuestComplAnswersRepository.save(pmQuestComplAnswers);
        PmQuestComplAnswersDTO result = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestComplAnswersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-quest-compl-answers : get all the pmQuestComplAnswers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestComplAnswers in body
     */
    @GetMapping("/pm-quest-compl-answers")
    @Timed
    public ResponseEntity<List<PmQuestComplAnswersDTO>> getAllPmQuestComplAnswers(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestComplAnswers");
        Page<PmQuestComplAnswers> page = pmQuestComplAnswersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-quest-compl-answers");
        return new ResponseEntity<>(pmQuestComplAnswersMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-quest-compl-answers/:id : get the "id" pmQuestComplAnswers.
     *
     * @param id the id of the pmQuestComplAnswersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestComplAnswersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-quest-compl-answers/{id}")
    @Timed
    public ResponseEntity<PmQuestComplAnswersDTO> getPmQuestComplAnswers(@PathVariable Long id) {
        log.debug("REST request to get PmQuestComplAnswers : {}", id);
        PmQuestComplAnswers pmQuestComplAnswers = pmQuestComplAnswersRepository.findOne(id);
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestComplAnswersDTO));
    }

    /**
     * DELETE  /pm-quest-compl-answers/:id : delete the "id" pmQuestComplAnswers.
     *
     * @param id the id of the pmQuestComplAnswersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-quest-compl-answers/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestComplAnswers(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestComplAnswers : {}", id);
        pmQuestComplAnswersRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

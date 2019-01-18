package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmQuestQuestions;

import com.infostudio.ba.domain.PmQuestionnaires;
import com.infostudio.ba.repository.PmQuestQuestionsRepository;
import com.infostudio.ba.repository.PmQuestionnairesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmQuestQuestionsDTO;
import com.infostudio.ba.service.mapper.PmQuestQuestionsMapper;
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
 * REST controller for managing PmQuestQuestions.
 */
@RestController
@RequestMapping("/api")
public class PmQuestQuestionsResource {

    private final Logger log = LoggerFactory.getLogger(PmQuestQuestionsResource.class);

    private static final String ENTITY_NAME = "pmQuestQuestions";

    private final PmQuestQuestionsRepository pmQuestQuestionsRepository;

    private final PmQuestQuestionsMapper pmQuestQuestionsMapper;

    private final PmQuestionnairesRepository pmQuestionnairesRepository;

    public PmQuestQuestionsResource(PmQuestQuestionsRepository pmQuestQuestionsRepository, PmQuestQuestionsMapper pmQuestQuestionsMapper,
                                    PmQuestionnairesRepository pmQuestionnairesRepository) {
        this.pmQuestQuestionsRepository = pmQuestQuestionsRepository;
        this.pmQuestQuestionsMapper = pmQuestQuestionsMapper;
        this.pmQuestionnairesRepository = pmQuestionnairesRepository;
    }

    /**
     * POST  /pm-quest-questions : Create a new pmQuestQuestions.
     *
     * @param pmQuestQuestionsDTO the pmQuestQuestionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmQuestQuestionsDTO, or with status 400 (Bad Request) if the pmQuestQuestions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-quest-questions")
    @Timed
    public ResponseEntity<PmQuestQuestionsDTO> createPmQuestQuestions(@Valid @RequestBody PmQuestQuestionsDTO pmQuestQuestionsDTO) throws URISyntaxException {
        log.debug("REST request to save PmQuestQuestions : {}", pmQuestQuestionsDTO);
        if (pmQuestQuestionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmQuestQuestions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmQuestQuestions pmQuestQuestions = pmQuestQuestionsMapper.toEntity(pmQuestQuestionsDTO);
        pmQuestQuestions = pmQuestQuestionsRepository.save(pmQuestQuestions);
        PmQuestQuestionsDTO result = pmQuestQuestionsMapper.toDto(pmQuestQuestions);
        return ResponseEntity.created(new URI("/api/pm-quest-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-quest-questions : Updates an existing pmQuestQuestions.
     *
     * @param pmQuestQuestionsDTO the pmQuestQuestionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmQuestQuestionsDTO,
     * or with status 400 (Bad Request) if the pmQuestQuestionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmQuestQuestionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-quest-questions")
    @Timed
    public ResponseEntity<PmQuestQuestionsDTO> updatePmQuestQuestions(@Valid @RequestBody PmQuestQuestionsDTO pmQuestQuestionsDTO) throws URISyntaxException {
        log.debug("REST request to update PmQuestQuestions : {}", pmQuestQuestionsDTO);
        if (pmQuestQuestionsDTO.getId() == null) {
            return createPmQuestQuestions(pmQuestQuestionsDTO);
        }
        PmQuestQuestions pmQuestQuestions = pmQuestQuestionsMapper.toEntity(pmQuestQuestionsDTO);
        pmQuestQuestions = pmQuestQuestionsRepository.save(pmQuestQuestions);
        PmQuestQuestionsDTO result = pmQuestQuestionsMapper.toDto(pmQuestQuestions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmQuestQuestionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-quest-questions : get all the pmQuestQuestions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmQuestQuestions in body
     */
    @GetMapping("/pm-quest-questions")
    @Timed
    public ResponseEntity<List<PmQuestQuestionsDTO>> getAllPmQuestQuestions(Pageable pageable) {
        log.debug("REST request to get a page of PmQuestQuestions");
        Page<PmQuestQuestions> page = pmQuestQuestionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-quest-questions");
        return new ResponseEntity<>(pmQuestQuestionsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping("/pm-quest-questions/questionnaire/{id}")
    @Timed
    public ResponseEntity<List<PmQuestQuestionsDTO>> getAllPmQuestQuestionsByQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to get all PmQuestQuestions by Questionnaire id: {}", id);

        PmQuestionnaires pmQuestionnaires = pmQuestionnairesRepository.findOne(id);
        if (pmQuestionnaires == null) {
            throw new BadRequestAlertException("PmQuestionnaire with id " + id + " does not exist",
                ENTITY_NAME, "questionnairedoesnotexist");
        }
        List<PmQuestQuestions> questionsEntities = pmQuestQuestionsRepository.findAllByIdQuestionnaire(id);
        List<PmQuestQuestionsDTO> outputToClient = pmQuestQuestionsMapper.toDto(questionsEntities);

        return ResponseEntity.ok(outputToClient);
    }

    /**
     * GET  /pm-quest-questions/:id : get the "id" pmQuestQuestions.
     *
     * @param id the id of the pmQuestQuestionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmQuestQuestionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-quest-questions/{id}")
    @Timed
    public ResponseEntity<PmQuestQuestionsDTO> getPmQuestQuestions(@PathVariable Long id) {
        log.debug("REST request to get PmQuestQuestions : {}", id);
        PmQuestQuestions pmQuestQuestions = pmQuestQuestionsRepository.findOne(id);
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmQuestQuestionsDTO));
    }

    /**
     * DELETE  /pm-quest-questions/:id : delete the "id" pmQuestQuestions.
     *
     * @param id the id of the pmQuestQuestionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-quest-questions/{id}")
    @Timed
    public ResponseEntity<Void> deletePmQuestQuestions(@PathVariable Long id) {
        log.debug("REST request to delete PmQuestQuestions : {}", id);
        pmQuestQuestionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

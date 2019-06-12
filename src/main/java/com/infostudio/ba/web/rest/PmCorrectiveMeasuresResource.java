package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.Action;
import com.infostudio.ba.domain.PmCorrectiveMeasures;

import com.infostudio.ba.repository.PmCorrectiveMeasuresRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.AuditUtil;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmCorrectiveMeasuresDTO;
import com.infostudio.ba.service.mapper.PmCorrectiveMeasuresMapper;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PmCorrectiveMeasures.
 */
@RestController
@RequestMapping("/api")
public class PmCorrectiveMeasuresResource {

    private final Logger log = LoggerFactory.getLogger(PmCorrectiveMeasuresResource.class);

    private static final String ENTITY_NAME = "pmCorrectiveMeasures";

    private final PmCorrectiveMeasuresRepository pmCorrectiveMeasuresRepository;

    private final PmCorrectiveMeasuresMapper pmCorrectiveMeasuresMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public PmCorrectiveMeasuresResource(PmCorrectiveMeasuresRepository pmCorrectiveMeasuresRepository,
                                        PmCorrectiveMeasuresMapper pmCorrectiveMeasuresMapper,
                                        ApplicationEventPublisher applicationEventPublisher
                                        ) {
        this.pmCorrectiveMeasuresRepository = pmCorrectiveMeasuresRepository;
        this.pmCorrectiveMeasuresMapper = pmCorrectiveMeasuresMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * POST  /pm-corrective-measures : Create a new pmCorrectiveMeasures.
     *
     * @param pmCorrectiveMeasuresDTO the pmCorrectiveMeasuresDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmCorrectiveMeasuresDTO, or with status 400 (Bad Request) if the pmCorrectiveMeasures has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-corrective-measures")
    @Timed
    public ResponseEntity<PmCorrectiveMeasuresDTO> createPmCorrectiveMeasures(@Valid @RequestBody PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO) throws URISyntaxException {
        log.debug("REST request to save PmCorrectiveMeasures : {}", pmCorrectiveMeasuresDTO);
        if (pmCorrectiveMeasuresDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmCorrectiveMeasures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmCorrectiveMeasures pmCorrectiveMeasures = pmCorrectiveMeasuresMapper.toEntity(pmCorrectiveMeasuresDTO);
        pmCorrectiveMeasures = pmCorrectiveMeasuresRepository.save(pmCorrectiveMeasures);
        PmCorrectiveMeasuresDTO result = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                ENTITY_NAME,
                result.getId().toString(),
                Action.POST
            )
        );
        return ResponseEntity.created(new URI("/api/pm-corrective-measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-corrective-measures : Updates an existing pmCorrectiveMeasures.
     *
     * @param pmCorrectiveMeasuresDTO the pmCorrectiveMeasuresDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmCorrectiveMeasuresDTO,
     * or with status 400 (Bad Request) if the pmCorrectiveMeasuresDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmCorrectiveMeasuresDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-corrective-measures")
    @Timed
    public ResponseEntity<PmCorrectiveMeasuresDTO> updatePmCorrectiveMeasures(@Valid @RequestBody PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO) throws URISyntaxException {
        log.debug("REST request to update PmCorrectiveMeasures : {}", pmCorrectiveMeasuresDTO);
        if (pmCorrectiveMeasuresDTO.getId() == null) {
            return createPmCorrectiveMeasures(pmCorrectiveMeasuresDTO);
        }
        PmCorrectiveMeasures pmCorrectiveMeasures = pmCorrectiveMeasuresMapper.toEntity(pmCorrectiveMeasuresDTO);
        pmCorrectiveMeasures = pmCorrectiveMeasuresRepository.save(pmCorrectiveMeasures);
        PmCorrectiveMeasuresDTO result = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                ENTITY_NAME,
                result.getId().toString(),
                Action.PUT
            )
        );
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmCorrectiveMeasuresDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-corrective-measures : get all the pmCorrectiveMeasures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmCorrectiveMeasures in body
     */
    @GetMapping("/pm-corrective-measures")
    @Timed
    public ResponseEntity<List<PmCorrectiveMeasuresDTO>> getAllPmCorrectiveMeasures(Pageable pageable,
																					@RequestParam(name = "employee-id", required = false) Long employeeId,
																					@RequestParam(name = "end-date", required = false) LocalDate endDate,
																					@RequestParam(name = "start-date", required = false) LocalDate startDate,
																					@RequestParam(name = "state-id", required = false) Long stateId,
																					@RequestParam(name = "type-id", required = false) Long typeId) {
        log.debug("REST request to get a page of PmCorrectiveMeasures");
        Page<PmCorrectiveMeasures> page = null;
        if (employeeId == null && endDate == null && startDate == null && stateId == null && typeId == null) {
        	page = pmCorrectiveMeasuresRepository.findAll(pageable);
		} else {
			page = pmCorrectiveMeasuresRepository.searchCorrectiveMeasure(pageable, employeeId, endDate, startDate, stateId, typeId);
		}
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-corrective-measures");
        return new ResponseEntity<>(pmCorrectiveMeasuresMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-corrective-measures/goal-evaluation/:id
     *
     * @param id the id of the GoalEvaluation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the List<pmCorrectiveMeasuresDTO>
     */
    @GetMapping("/pm-corrective-measures/goal-evaluation/{id}")
    public ResponseEntity<List<PmCorrectiveMeasuresDTO>> getAllPmCorrectiveMeasuresByGoalEvaluation(@PathVariable Long id){
        log.debug("REST request to get PmCorrectiveMeasures by GoalEvaluation : {}", id);
        List<PmCorrectiveMeasures> pmCorrectiveMeasures = pmCorrectiveMeasuresRepository
            .findAllByIdGoalEvaluationId(id);
        return ResponseEntity.ok(pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures));
    }

    /**
     * GET  /pm-corrective-measures/:id : get the "id" pmCorrectiveMeasures.
     *
     * @param id the id of the pmCorrectiveMeasuresDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmCorrectiveMeasuresDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-corrective-measures/{id}")
    @Timed
    public ResponseEntity<PmCorrectiveMeasuresDTO> getPmCorrectiveMeasures(@PathVariable Long id) {
        log.debug("REST request to get PmCorrectiveMeasures : {}", id);
        PmCorrectiveMeasures pmCorrectiveMeasures = pmCorrectiveMeasuresRepository.findOne(id);
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmCorrectiveMeasuresDTO));
    }

    /**
     * DELETE  /pm-corrective-measures/:id : delete the "id" pmCorrectiveMeasures.
     *
     * @param id the id of the pmCorrectiveMeasuresDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-corrective-measures/{id}")
    @Timed
    public ResponseEntity<Void> deletePmCorrectiveMeasures(@PathVariable Long id) {
        log.debug("REST request to delete PmCorrectiveMeasures : {}", id);
        pmCorrectiveMeasuresRepository.delete(id);
        applicationEventPublisher.publishEvent(
            AuditUtil.createAuditEvent(
                ENTITY_NAME,
                id.toString(),
                Action.DELETE
            )
        );
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

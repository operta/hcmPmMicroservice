package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.infostudio.ba.domain.PmEmployeesGoals;

import com.infostudio.ba.domain.PmGoalStates;
import com.infostudio.ba.repository.PmEmployeesGoalsRepository;
import com.infostudio.ba.repository.PmGoalStatesRepository;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmEmployeesGoalsDTO;
import com.infostudio.ba.service.mapper.PmEmployeesGoalsMapper;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PmEmployeesGoals.
 */
@RestController
@RequestMapping("/api")
public class PmEmployeesGoalsResource {

    private final Logger log = LoggerFactory.getLogger(PmEmployeesGoalsResource.class);

    private static final String ENTITY_NAME = "pmEmployeesGoals";

    private final PmEmployeesGoalsRepository pmEmployeesGoalsRepository;

    private final PmEmployeesGoalsMapper pmEmployeesGoalsMapper;

    private final PmGoalStatesRepository pmGoalStatesRepository;

    public PmEmployeesGoalsResource(PmEmployeesGoalsRepository pmEmployeesGoalsRepository, PmEmployeesGoalsMapper pmEmployeesGoalsMapper,
                                    PmGoalStatesRepository pmGoalStatesRepository) {
        this.pmEmployeesGoalsRepository = pmEmployeesGoalsRepository;
        this.pmEmployeesGoalsMapper = pmEmployeesGoalsMapper;
        this.pmGoalStatesRepository = pmGoalStatesRepository;
    }

    /**
     * POST  /pm-employees-goals : Create a new pmEmployeesGoals.
     *
     * @param pmEmployeesGoalsDTO the pmEmployeesGoalsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pmEmployeesGoalsDTO, or with status 400 (Bad Request) if the pmEmployeesGoals has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pm-employees-goals")
    @Timed
    public ResponseEntity<PmEmployeesGoalsDTO> createPmEmployeesGoals(@Valid @RequestBody PmEmployeesGoalsDTO pmEmployeesGoalsDTO) throws URISyntaxException {
        log.debug("REST request to save PmEmployeesGoals : {}", pmEmployeesGoalsDTO);
        if (pmEmployeesGoalsDTO.getId() != null) {
            throw new BadRequestAlertException("A new pmEmployeesGoals cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PmEmployeesGoals pmEmployeesGoals = pmEmployeesGoalsMapper.toEntity(pmEmployeesGoalsDTO);
        pmEmployeesGoals = pmEmployeesGoalsRepository.save(pmEmployeesGoals);
        PmEmployeesGoalsDTO result = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);
        return ResponseEntity.created(new URI("/api/pm-employees-goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pm-employees-goals : Updates an existing pmEmployeesGoals.
     *
     * @param pmEmployeesGoalsDTO the pmEmployeesGoalsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pmEmployeesGoalsDTO,
     * or with status 400 (Bad Request) if the pmEmployeesGoalsDTO is not valid,
     * or with status 500 (Internal Server Error) if the pmEmployeesGoalsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pm-employees-goals")
    @Timed
    public ResponseEntity<PmEmployeesGoalsDTO> updatePmEmployeesGoals(@Valid @RequestBody PmEmployeesGoalsDTO pmEmployeesGoalsDTO) throws URISyntaxException {
        log.debug("REST request to update PmEmployeesGoals : {}", pmEmployeesGoalsDTO);
        if (pmEmployeesGoalsDTO.getId() == null) {
            return createPmEmployeesGoals(pmEmployeesGoalsDTO);
        }
        PmEmployeesGoals employeeGoalFromDB = pmEmployeesGoalsRepository.findOne(pmEmployeesGoalsDTO.getId());
        if (employeeGoalFromDB == null) {
            throw new BadRequestAlertException("Employee goal with id " + pmEmployeesGoalsDTO.getId() + " does not exist",
                ENTITY_NAME, "employeegoaldoesnotexist");
        }
        PmGoalStates goalStateFromDB = pmGoalStatesRepository.findOne(pmEmployeesGoalsDTO.getIdGoalStateId());
        if (goalStateFromDB == null) {
            throw new BadRequestAlertException("Goal state with id " + pmEmployeesGoalsDTO.getIdGoalStateId() + "does not exist",
                ENTITY_NAME, "goalstatedoesnotexist");
        }
        if (!pmEmployeesGoalsDTO.getIdGoalStateId().equals(employeeGoalFromDB.getIdGoalState().getId())) {
            pmEmployeesGoalsDTO.setStateDate(LocalDate.now());
            log.debug("I AM HERE");
        }
        PmEmployeesGoals pmEmployeesGoals = pmEmployeesGoalsMapper.toEntity(pmEmployeesGoalsDTO);
        pmEmployeesGoals = pmEmployeesGoalsRepository.save(pmEmployeesGoals);
        PmEmployeesGoalsDTO result = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmEmployeesGoalsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pm-employees-goals : get all the pmEmployeesGoals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pmEmployeesGoals in body
     */
    @GetMapping("/pm-employees-goals")
    @Timed
    public ResponseEntity<List<PmEmployeesGoalsDTO>> getAllPmEmployeesGoals(Pageable pageable) {
        log.debug("REST request to get a page of PmEmployeesGoals");
        Page<PmEmployeesGoals> page = pmEmployeesGoalsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-employees-goals");
        return new ResponseEntity<>(pmEmployeesGoalsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pm-employees-goals/:id : get the "id" pmEmployeesGoals.
     *
     * @param id the id of the pmEmployeesGoalsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pmEmployeesGoalsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pm-employees-goals/{id}")
    @Timed
    public ResponseEntity<PmEmployeesGoalsDTO> getPmEmployeesGoals(@PathVariable Long id) {
        log.debug("REST request to get PmEmployeesGoals : {}", id);
        PmEmployeesGoals pmEmployeesGoals = pmEmployeesGoalsRepository.findOne(id);
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmEmployeesGoalsDTO));
    }

    @GetMapping("/pm-employees-goals/goal/{id}")
    @Timed
    public ResponseEntity<List<PmEmployeesGoalsDTO>> getPmEmployeesGoalsByGoalId(@PathVariable Long id) {
        log.debug("REST request to get PmEmployeesGoals by goalId : {}", id);
        List<PmEmployeesGoals> pmEmployeesGoals = pmEmployeesGoalsRepository.findByIdGoalId(id);
        List<PmEmployeesGoalsDTO> pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmEmployeesGoalsDTO));
    }

    /**
     * DELETE  /pm-employees-goals/:id : delete the "id" pmEmployeesGoals.
     *
     * @param id the id of the pmEmployeesGoalsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pm-employees-goals/{id}")
    @Timed
    public ResponseEntity<Void> deletePmEmployeesGoals(@PathVariable Long id) {
        log.debug("REST request to delete PmEmployeesGoals : {}", id);
        pmEmployeesGoalsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

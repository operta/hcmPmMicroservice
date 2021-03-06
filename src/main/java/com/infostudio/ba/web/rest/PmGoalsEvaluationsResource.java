package com.infostudio.ba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infostudio.ba.domain.Action;
import com.infostudio.ba.domain.PmEmployeesGoals;
import com.infostudio.ba.domain.PmGoalStates;
import com.infostudio.ba.domain.PmGoalsEvaluations;

import com.infostudio.ba.repository.PmEmployeesGoalsRepository;
import com.infostudio.ba.repository.PmGoalStatesRepository;
import com.infostudio.ba.repository.PmGoalsEvaluationsRepository;
import com.infostudio.ba.service.ConstantService;
import com.infostudio.ba.service.helper.model.ApConstants;
import com.infostudio.ba.service.helper.model.EmEmployees;
import com.infostudio.ba.service.helper.model.UserNotifications;
import com.infostudio.ba.service.proxy.CoreMicroserviceProxy;
import com.infostudio.ba.service.proxy.EmployeeMicroserviceProxy;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import com.infostudio.ba.web.rest.util.AuditUtil;
import com.infostudio.ba.web.rest.util.HeaderUtil;
import com.infostudio.ba.web.rest.util.PaginationUtil;
import com.infostudio.ba.service.dto.PmGoalsEvaluationsDTO;
import com.infostudio.ba.service.mapper.PmGoalsEvaluationsMapper;
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

import static com.infostudio.ba.config.Constants.AUTHORIZATION_HEADER;

/**
 * REST controller for managing PmGoalsEvaluations.
 */
@RestController
@RequestMapping("/api")
public class PmGoalsEvaluationsResource {

	private final Logger log = LoggerFactory.getLogger(PmGoalsEvaluationsResource.class);

	private static final String ENTITY_NAME = "pmGoalsEvaluations";

	private final PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository;

	private final PmGoalsEvaluationsMapper pmGoalsEvaluationsMapper;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final CoreMicroserviceProxy coreMicroserviceProxy;

	private final EmployeeMicroserviceProxy employeeMicroserviceProxy;

	private final String EVALUATION_STATE_ID_KEY = "evaluationGradedState";

	private final String EVALUATION_STATE_APPROVED_ID_KEY = "evaluationApprovedState";

	private final String NOTIFICATION_TEMPLATE_APPROVE_EVALUATION_KEY = "notificationApproveEvaluation";

	private final String EMPLOYEE_GOAL_EVALUATION_FINISHED_ID_KEY = "employeeGoalEvaluationFinished";

	private final PmEmployeesGoalsRepository pmEmployeesGoalsRepository;

	private final PmGoalStatesRepository pmGoalStatesRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final ConstantService constantService;

	public PmGoalsEvaluationsResource(PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository,
			PmGoalsEvaluationsMapper pmGoalsEvaluationsMapper,
			ApplicationEventPublisher applicationEventPublisher,
			CoreMicroserviceProxy coreMicroserviceProxy,
			EmployeeMicroserviceProxy employeeMicroserviceProxy,
			PmEmployeesGoalsRepository pmEmployeesGoalsRepository,
			PmGoalStatesRepository pmGoalStatesRepository,
			ConstantService constantService) {
		this.pmGoalsEvaluationsRepository = pmGoalsEvaluationsRepository;
		this.pmGoalsEvaluationsMapper = pmGoalsEvaluationsMapper;
		this.applicationEventPublisher = applicationEventPublisher;
		this.coreMicroserviceProxy = coreMicroserviceProxy;
		this.pmEmployeesGoalsRepository = pmEmployeesGoalsRepository;
		this.employeeMicroserviceProxy = employeeMicroserviceProxy;
		this.constantService = constantService;
		this.pmGoalStatesRepository = pmGoalStatesRepository;
	}

	/**
	 * POST  /pm-goals-evaluations : Create a new pmGoalsEvaluations.
	 *
	 * @param pmGoalsEvaluationsDTO the pmGoalsEvaluationsDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new pmGoalsEvaluationsDTO, or with status 400 (Bad Request) if the pmGoalsEvaluations has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/pm-goals-evaluations")
	@Timed
	public ResponseEntity<PmGoalsEvaluationsDTO> createPmGoalsEvaluations(@Valid @RequestBody PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO) throws URISyntaxException {
		log.debug("REST request to save PmGoalsEvaluations : {}", pmGoalsEvaluationsDTO);
		if (pmGoalsEvaluationsDTO.getId() != null) {
			throw new BadRequestAlertException("A new pmGoalsEvaluations cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsMapper.toEntity(pmGoalsEvaluationsDTO);
		pmGoalsEvaluations = pmGoalsEvaluationsRepository.save(pmGoalsEvaluations);
		PmGoalsEvaluationsDTO result = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
		applicationEventPublisher.publishEvent(
				AuditUtil.createAuditEvent(
						ENTITY_NAME,
						result.getId().toString(),
						Action.POST
				)
		);
		return ResponseEntity.created(new URI("/api/pm-goals-evaluations/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}


	/**
	 * PUT  /pm-goals-evaluations : Updates an existing pmGoalsEvaluations.
	 *
	 * @param pmGoalsEvaluationsDTO the pmGoalsEvaluationsDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated pmGoalsEvaluationsDTO,
	 * or with status 400 (Bad Request) if the pmGoalsEvaluationsDTO is not valid,
	 * or with status 500 (Internal Server Error) if the pmGoalsEvaluationsDTO couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/pm-goals-evaluations")
	@Timed
	public ResponseEntity<PmGoalsEvaluationsDTO> updatePmGoalsEvaluations(@Valid @RequestBody PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO,
																		  @RequestHeader(AUTHORIZATION_HEADER) String token) throws URISyntaxException {
		log.debug("REST request to update PmGoalsEvaluations : {}", pmGoalsEvaluationsDTO);
		if (pmGoalsEvaluationsDTO.getId() == null) {
			return createPmGoalsEvaluations(pmGoalsEvaluationsDTO);
		}
		PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsMapper.toEntity(pmGoalsEvaluationsDTO);
		Long evaluationId = constantService.getApConstantValueByName(EVALUATION_STATE_ID_KEY, token, ENTITY_NAME);
		Long evaluationApprovedId = constantService.getApConstantValueByName(EVALUATION_STATE_APPROVED_ID_KEY, token, ENTITY_NAME);

		if (pmGoalsEvaluations.getIdEvaluationState() != null) {
			if (pmGoalsEvaluations.getIdEvaluationState().getId().equals(evaluationId)) {
				pmGoalsEvaluations = pmGoalsEvaluationsRepository.save(pmGoalsEvaluations);
				sendNotificationForGradedEvaluation(token, pmGoalsEvaluations);
			} else if (pmGoalsEvaluations.getIdEvaluationState().getId().equals(evaluationApprovedId)) {
				log.debug("EVALUATION APPROVED");
				setEvaluationOfEmployeeGoalToFinished(pmGoalsEvaluations, token);
				pmGoalsEvaluations = pmGoalsEvaluationsRepository.save(pmGoalsEvaluations);
			}
		}
		PmGoalsEvaluationsDTO result = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
		applicationEventPublisher.publishEvent(
				AuditUtil.createAuditEvent(
						ENTITY_NAME,
						result.getId().toString(),
						Action.PUT
				)
		);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pmGoalsEvaluationsDTO.getId().toString()))
				.body(result);
	}

	private void setEvaluationOfEmployeeGoalToFinished(PmGoalsEvaluations pmGoalsEvaluations, String token) {
		if (pmGoalsEvaluations.getIdEmployeeGoal() == null) {
			throw new BadRequestAlertException("You need to provide the id employee goal",
					ENTITY_NAME, "idEmployeeGoalNull");
		}
		PmEmployeesGoals employeeGoal = pmEmployeesGoalsRepository.findOne(pmGoalsEvaluations.getIdEmployeeGoal().getId());
		if (employeeGoal == null) {
			throw new BadRequestAlertException("Employee goal with id " + pmGoalsEvaluations.getIdEmployeeGoal().getId() + " does not exist.",
					ENTITY_NAME, "employeeGoalDoesNotExist");
		}
		Long employeeGoalFinishedStateId = constantService.getApConstantValueByName(EMPLOYEE_GOAL_EVALUATION_FINISHED_ID_KEY, token, ENTITY_NAME);
		log.debug("EMPLOYEE GOAL FINISHED STATE ID: {}", employeeGoalFinishedStateId);
		PmGoalStates goalState = pmGoalStatesRepository.findOne(employeeGoalFinishedStateId);
		if (goalState == null) {
			throw new BadRequestAlertException("Goal state with id " + employeeGoalFinishedStateId + " does not exist.",
					ENTITY_NAME, "goalStateDoesNotExist");
		}
		employeeGoal.setIdGoalState(goalState);
		employeeGoal = pmEmployeesGoalsRepository.save(employeeGoal);
		pmGoalsEvaluations.setAchievedValue(employeeGoal.getCurrentValue());
		log.debug("ACHIEVED VALUE: {}", pmGoalsEvaluations.getAchievedValue());
	}

	private void sendNotificationForGradedEvaluation(String token, PmGoalsEvaluations pmGoalsEvaluations) {
		Long notificationTemplateId = constantService.getApConstantValueByName(NOTIFICATION_TEMPLATE_APPROVE_EVALUATION_KEY, token, ENTITY_NAME);
		UserNotifications notification = new UserNotifications();
		if (pmGoalsEvaluations.getIdEmployeeGoal() == null) {
			return ;
		}
		PmEmployeesGoals goals = pmEmployeesGoalsRepository.findOne(pmGoalsEvaluations.getIdEmployeeGoal().getId());
		if (goals == null) {
			return ;
		}
		EmEmployees employee = employeeMicroserviceProxy.getEmployeeById(goals.getIdEmployeeResponsible(), token).getBody();
		if (employee == null) {
			return ;
		}
		Integer userId = employee.getIdUser();
		if (userId == null) {
			return ;
		}
		notification.setIdUser(userId.longValue());
		notification.setNotification_templatesId(notificationTemplateId);
		notification.setIs_read("F");

		class EvaluationNotificationData {
			private Long evaluationId;

			public EvaluationNotificationData() {}

			public EvaluationNotificationData(Long evaluationId) {
				this.evaluationId = evaluationId;
			}

			public Long getEvaluationId() {
				return evaluationId;
			}

			public void setEvaluationId(Long evaluationId) {
				this.evaluationId = evaluationId;
			}
		}

		String evaluationData = null;
		try {
			evaluationData = objectMapper.writeValueAsString(new EvaluationNotificationData(pmGoalsEvaluations.getId()));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new BadRequestAlertException("Error while converting object to json",
					ENTITY_NAME, "conversionToJsonError");
		}
		notification.setNotificationData(evaluationData);
		coreMicroserviceProxy.createNotification(notification, token);
	}

	/**
	 * GET  /pm-goals-evaluations : get all the pmGoalsEvaluations.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of pmGoalsEvaluations in body
	 */
	@GetMapping("/pm-goals-evaluations")
	@Timed
	public ResponseEntity<List<PmGoalsEvaluationsDTO>> getAllPmGoalsEvaluations(Pageable pageable,
			@RequestParam(name = "employee-id", required = false) Long employeeId,
			@RequestParam(name = "state-id", required = false) Long stateId,
			@RequestParam(name = "evaluation-from", required = false) LocalDate evaluationFrom,
			@RequestParam(name = "evaluation-to", required = false)LocalDate evaluationTo) {
		log.debug("REST request to get a page of PmGoalsEvaluations");
		Page<PmGoalsEvaluations> page = pmGoalsEvaluationsRepository.searchGoalEvaluations(pageable, employeeId, stateId, evaluationFrom, evaluationTo);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pm-goals-evaluations");
		return new ResponseEntity<>(pmGoalsEvaluationsMapper.toDto(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /pm-goals-evaluations/:id : get the "id" pmGoalsEvaluations.
	 *
	 * @param id the id of the pmGoalsEvaluationsDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the pmGoalsEvaluationsDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/pm-goals-evaluations/{id}")
	@Timed
	public ResponseEntity<PmGoalsEvaluationsDTO> getPmGoalsEvaluations(@PathVariable Long id) {
		log.debug("REST request to get PmGoalsEvaluations : {}", id);
		PmGoalsEvaluations pmGoalsEvaluations = pmGoalsEvaluationsRepository.findOne(id);
		PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pmGoalsEvaluationsDTO));
	}

	/**
	 * GET  /pm-goals-evaluations/employee-goal/:id
	 *
	 * @param id the id of the employeeGoal for which the pm-goals-evaluations are retrieved
	 * @return the ResponseEntity with status 200 (OK) and with body the List<pmGoalsEvaluationsDTO>, or with status 404 (Not Found)
	 */
	@GetMapping("/pm-goals-evaluations/employee-goal/{id}")
	public ResponseEntity<List<PmGoalsEvaluationsDTO>> getPmGoalsEvaluationsByEmployeeGoal(@PathVariable Long id){
		log.debug("REST request to get PmGoalsEvaluations by EmployeeGoalId: {}", id);
		List<PmGoalsEvaluations> pmGoalsEvaluations = pmGoalsEvaluationsRepository.findAllByIdEmployeeGoalId(id);
		return ResponseEntity.ok(pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations));
	}

	/**
	 * DELETE  /pm-goals-evaluations/:id : delete the "id" pmGoalsEvaluations.
	 *
	 * @param id the id of the pmGoalsEvaluationsDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/pm-goals-evaluations/{id}")
	@Timed
	public ResponseEntity<Void> deletePmGoalsEvaluations(@PathVariable Long id) {
		log.debug("REST request to delete PmGoalsEvaluations : {}", id);
		pmGoalsEvaluationsRepository.delete(id);
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

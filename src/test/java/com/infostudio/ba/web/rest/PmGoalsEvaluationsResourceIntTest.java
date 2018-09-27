package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmGoalsEvaluations;
import com.infostudio.ba.repository.PmGoalsEvaluationsRepository;
import com.infostudio.ba.service.dto.PmGoalsEvaluationsDTO;
import com.infostudio.ba.service.mapper.PmGoalsEvaluationsMapper;
import com.infostudio.ba.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.infostudio.ba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PmGoalsEvaluationsResource REST controller.
 *
 * @see PmGoalsEvaluationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmGoalsEvaluationsResourceIntTest {

    private static final LocalDate DEFAULT_EVALUATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ID_EMPLOYEE_EVALUATOR = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_EVALUATOR = 2L;

    private static final Long DEFAULT_ID_EMPLOYEE_APPROVING = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_APPROVING = 2L;

    private static final LocalDate DEFAULT_EVALUATION_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EVALUATION_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATION_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ACHIEVED_VALUE = 1L;
    private static final Long UPDATED_ACHIEVED_VALUE = 2L;

    private static final LocalDate DEFAULT_STATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository;

    @Autowired
    private PmGoalsEvaluationsMapper pmGoalsEvaluationsMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmGoalsEvaluationsMockMvc;

    private PmGoalsEvaluations pmGoalsEvaluations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmGoalsEvaluationsResource pmGoalsEvaluationsResource = new PmGoalsEvaluationsResource(pmGoalsEvaluationsRepository, pmGoalsEvaluationsMapper);
        this.restPmGoalsEvaluationsMockMvc = MockMvcBuilders.standaloneSetup(pmGoalsEvaluationsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PmGoalsEvaluations createEntity(EntityManager em) {
        PmGoalsEvaluations pmGoalsEvaluations = new PmGoalsEvaluations()
            .evaluationDate(DEFAULT_EVALUATION_DATE)
            .idEmployeeEvaluator(DEFAULT_ID_EMPLOYEE_EVALUATOR)
            .idEmployeeApproving(DEFAULT_ID_EMPLOYEE_APPROVING)
            .evaluationPeriodFrom(DEFAULT_EVALUATION_PERIOD_FROM)
            .evaluationPeriodTo(DEFAULT_EVALUATION_PERIOD_TO)
            .achievedValue(DEFAULT_ACHIEVED_VALUE)
            .stateDate(DEFAULT_STATE_DATE);
        return pmGoalsEvaluations;
    }

    @Before
    public void initTest() {
        pmGoalsEvaluations = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmGoalsEvaluations() throws Exception {
        int databaseSizeBeforeCreate = pmGoalsEvaluationsRepository.findAll().size();

        // Create the PmGoalsEvaluations
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);
        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalsEvaluations in the database
        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeCreate + 1);
        PmGoalsEvaluations testPmGoalsEvaluations = pmGoalsEvaluationsList.get(pmGoalsEvaluationsList.size() - 1);
        assertThat(testPmGoalsEvaluations.getEvaluationDate()).isEqualTo(DEFAULT_EVALUATION_DATE);
        assertThat(testPmGoalsEvaluations.getIdEmployeeEvaluator()).isEqualTo(DEFAULT_ID_EMPLOYEE_EVALUATOR);
        assertThat(testPmGoalsEvaluations.getIdEmployeeApproving()).isEqualTo(DEFAULT_ID_EMPLOYEE_APPROVING);
        assertThat(testPmGoalsEvaluations.getEvaluationPeriodFrom()).isEqualTo(DEFAULT_EVALUATION_PERIOD_FROM);
        assertThat(testPmGoalsEvaluations.getEvaluationPeriodTo()).isEqualTo(DEFAULT_EVALUATION_PERIOD_TO);
        assertThat(testPmGoalsEvaluations.getAchievedValue()).isEqualTo(DEFAULT_ACHIEVED_VALUE);
        assertThat(testPmGoalsEvaluations.getStateDate()).isEqualTo(DEFAULT_STATE_DATE);
    }

    @Test
    @Transactional
    public void createPmGoalsEvaluationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmGoalsEvaluationsRepository.findAll().size();

        // Create the PmGoalsEvaluations with an existing ID
        pmGoalsEvaluations.setId(1L);
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmGoalsEvaluations in the database
        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEvaluationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setEvaluationDate(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdEmployeeEvaluatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setIdEmployeeEvaluator(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdEmployeeApprovingIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setIdEmployeeApproving(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEvaluationPeriodFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setEvaluationPeriodFrom(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEvaluationPeriodToIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setEvaluationPeriodTo(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAchievedValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setAchievedValue(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsEvaluationsRepository.findAll().size();
        // set the field null
        pmGoalsEvaluations.setStateDate(null);

        // Create the PmGoalsEvaluations, which fails.
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(post("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmGoalsEvaluations() throws Exception {
        // Initialize the database
        pmGoalsEvaluationsRepository.saveAndFlush(pmGoalsEvaluations);

        // Get all the pmGoalsEvaluationsList
        restPmGoalsEvaluationsMockMvc.perform(get("/api/pm-goals-evaluations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmGoalsEvaluations.getId().intValue())))
            .andExpect(jsonPath("$.[*].evaluationDate").value(hasItem(DEFAULT_EVALUATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].idEmployeeEvaluator").value(hasItem(DEFAULT_ID_EMPLOYEE_EVALUATOR.intValue())))
            .andExpect(jsonPath("$.[*].idEmployeeApproving").value(hasItem(DEFAULT_ID_EMPLOYEE_APPROVING.intValue())))
            .andExpect(jsonPath("$.[*].evaluationPeriodFrom").value(hasItem(DEFAULT_EVALUATION_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].evaluationPeriodTo").value(hasItem(DEFAULT_EVALUATION_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].achievedValue").value(hasItem(DEFAULT_ACHIEVED_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(DEFAULT_STATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPmGoalsEvaluations() throws Exception {
        // Initialize the database
        pmGoalsEvaluationsRepository.saveAndFlush(pmGoalsEvaluations);

        // Get the pmGoalsEvaluations
        restPmGoalsEvaluationsMockMvc.perform(get("/api/pm-goals-evaluations/{id}", pmGoalsEvaluations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmGoalsEvaluations.getId().intValue()))
            .andExpect(jsonPath("$.evaluationDate").value(DEFAULT_EVALUATION_DATE.toString()))
            .andExpect(jsonPath("$.idEmployeeEvaluator").value(DEFAULT_ID_EMPLOYEE_EVALUATOR.intValue()))
            .andExpect(jsonPath("$.idEmployeeApproving").value(DEFAULT_ID_EMPLOYEE_APPROVING.intValue()))
            .andExpect(jsonPath("$.evaluationPeriodFrom").value(DEFAULT_EVALUATION_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.evaluationPeriodTo").value(DEFAULT_EVALUATION_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.achievedValue").value(DEFAULT_ACHIEVED_VALUE.intValue()))
            .andExpect(jsonPath("$.stateDate").value(DEFAULT_STATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmGoalsEvaluations() throws Exception {
        // Get the pmGoalsEvaluations
        restPmGoalsEvaluationsMockMvc.perform(get("/api/pm-goals-evaluations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmGoalsEvaluations() throws Exception {
        // Initialize the database
        pmGoalsEvaluationsRepository.saveAndFlush(pmGoalsEvaluations);
        int databaseSizeBeforeUpdate = pmGoalsEvaluationsRepository.findAll().size();

        // Update the pmGoalsEvaluations
        PmGoalsEvaluations updatedPmGoalsEvaluations = pmGoalsEvaluationsRepository.findOne(pmGoalsEvaluations.getId());
        // Disconnect from session so that the updates on updatedPmGoalsEvaluations are not directly saved in db
        em.detach(updatedPmGoalsEvaluations);
        updatedPmGoalsEvaluations
            .evaluationDate(UPDATED_EVALUATION_DATE)
            .idEmployeeEvaluator(UPDATED_ID_EMPLOYEE_EVALUATOR)
            .idEmployeeApproving(UPDATED_ID_EMPLOYEE_APPROVING)
            .evaluationPeriodFrom(UPDATED_EVALUATION_PERIOD_FROM)
            .evaluationPeriodTo(UPDATED_EVALUATION_PERIOD_TO)
            .achievedValue(UPDATED_ACHIEVED_VALUE)
            .stateDate(UPDATED_STATE_DATE);
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(updatedPmGoalsEvaluations);

        restPmGoalsEvaluationsMockMvc.perform(put("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isOk());

        // Validate the PmGoalsEvaluations in the database
        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeUpdate);
        PmGoalsEvaluations testPmGoalsEvaluations = pmGoalsEvaluationsList.get(pmGoalsEvaluationsList.size() - 1);
        assertThat(testPmGoalsEvaluations.getEvaluationDate()).isEqualTo(UPDATED_EVALUATION_DATE);
        assertThat(testPmGoalsEvaluations.getIdEmployeeEvaluator()).isEqualTo(UPDATED_ID_EMPLOYEE_EVALUATOR);
        assertThat(testPmGoalsEvaluations.getIdEmployeeApproving()).isEqualTo(UPDATED_ID_EMPLOYEE_APPROVING);
        assertThat(testPmGoalsEvaluations.getEvaluationPeriodFrom()).isEqualTo(UPDATED_EVALUATION_PERIOD_FROM);
        assertThat(testPmGoalsEvaluations.getEvaluationPeriodTo()).isEqualTo(UPDATED_EVALUATION_PERIOD_TO);
        assertThat(testPmGoalsEvaluations.getAchievedValue()).isEqualTo(UPDATED_ACHIEVED_VALUE);
        assertThat(testPmGoalsEvaluations.getStateDate()).isEqualTo(UPDATED_STATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPmGoalsEvaluations() throws Exception {
        int databaseSizeBeforeUpdate = pmGoalsEvaluationsRepository.findAll().size();

        // Create the PmGoalsEvaluations
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO = pmGoalsEvaluationsMapper.toDto(pmGoalsEvaluations);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmGoalsEvaluationsMockMvc.perform(put("/api/pm-goals-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsEvaluationsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalsEvaluations in the database
        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmGoalsEvaluations() throws Exception {
        // Initialize the database
        pmGoalsEvaluationsRepository.saveAndFlush(pmGoalsEvaluations);
        int databaseSizeBeforeDelete = pmGoalsEvaluationsRepository.findAll().size();

        // Get the pmGoalsEvaluations
        restPmGoalsEvaluationsMockMvc.perform(delete("/api/pm-goals-evaluations/{id}", pmGoalsEvaluations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmGoalsEvaluations> pmGoalsEvaluationsList = pmGoalsEvaluationsRepository.findAll();
        assertThat(pmGoalsEvaluationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalsEvaluations.class);
        PmGoalsEvaluations pmGoalsEvaluations1 = new PmGoalsEvaluations();
        pmGoalsEvaluations1.setId(1L);
        PmGoalsEvaluations pmGoalsEvaluations2 = new PmGoalsEvaluations();
        pmGoalsEvaluations2.setId(pmGoalsEvaluations1.getId());
        assertThat(pmGoalsEvaluations1).isEqualTo(pmGoalsEvaluations2);
        pmGoalsEvaluations2.setId(2L);
        assertThat(pmGoalsEvaluations1).isNotEqualTo(pmGoalsEvaluations2);
        pmGoalsEvaluations1.setId(null);
        assertThat(pmGoalsEvaluations1).isNotEqualTo(pmGoalsEvaluations2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalsEvaluationsDTO.class);
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO1 = new PmGoalsEvaluationsDTO();
        pmGoalsEvaluationsDTO1.setId(1L);
        PmGoalsEvaluationsDTO pmGoalsEvaluationsDTO2 = new PmGoalsEvaluationsDTO();
        assertThat(pmGoalsEvaluationsDTO1).isNotEqualTo(pmGoalsEvaluationsDTO2);
        pmGoalsEvaluationsDTO2.setId(pmGoalsEvaluationsDTO1.getId());
        assertThat(pmGoalsEvaluationsDTO1).isEqualTo(pmGoalsEvaluationsDTO2);
        pmGoalsEvaluationsDTO2.setId(2L);
        assertThat(pmGoalsEvaluationsDTO1).isNotEqualTo(pmGoalsEvaluationsDTO2);
        pmGoalsEvaluationsDTO1.setId(null);
        assertThat(pmGoalsEvaluationsDTO1).isNotEqualTo(pmGoalsEvaluationsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmGoalsEvaluationsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmGoalsEvaluationsMapper.fromId(null)).isNull();
    }
}

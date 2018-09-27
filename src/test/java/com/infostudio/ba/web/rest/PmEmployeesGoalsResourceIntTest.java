package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmEmployeesGoals;
import com.infostudio.ba.repository.PmEmployeesGoalsRepository;
import com.infostudio.ba.service.dto.PmEmployeesGoalsDTO;
import com.infostudio.ba.service.mapper.PmEmployeesGoalsMapper;
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
 * Test class for the PmEmployeesGoalsResource REST controller.
 *
 * @see PmEmployeesGoalsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmEmployeesGoalsResourceIntTest {

    private static final Long DEFAULT_ID_EMPLOYEE_RESPONSIBLE = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_RESPONSIBLE = 2L;

    private static final Long DEFAULT_ID_EMPLOYEE_SET_BY = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_SET_BY = 2L;

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CURRENT_VALUE = 1L;
    private static final Long UPDATED_CURRENT_VALUE = 2L;

    private static final Long DEFAULT_TARGET_VALUE = 1L;
    private static final Long UPDATED_TARGET_VALUE = 2L;

    private static final Long DEFAULT_INITIAL_VALUE = 1L;
    private static final Long UPDATED_INITIAL_VALUE = 2L;

    private static final LocalDate DEFAULT_STATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_GOAL_SET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GOAL_SET_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmEmployeesGoalsRepository pmEmployeesGoalsRepository;

    @Autowired
    private PmEmployeesGoalsMapper pmEmployeesGoalsMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmEmployeesGoalsMockMvc;

    private PmEmployeesGoals pmEmployeesGoals;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmEmployeesGoalsResource pmEmployeesGoalsResource = new PmEmployeesGoalsResource(pmEmployeesGoalsRepository, pmEmployeesGoalsMapper);
        this.restPmEmployeesGoalsMockMvc = MockMvcBuilders.standaloneSetup(pmEmployeesGoalsResource)
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
    public static PmEmployeesGoals createEntity(EntityManager em) {
        PmEmployeesGoals pmEmployeesGoals = new PmEmployeesGoals()
            .idEmployeeResponsible(DEFAULT_ID_EMPLOYEE_RESPONSIBLE)
            .idEmployeeSetBy(DEFAULT_ID_EMPLOYEE_SET_BY)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .currentValue(DEFAULT_CURRENT_VALUE)
            .targetValue(DEFAULT_TARGET_VALUE)
            .initialValue(DEFAULT_INITIAL_VALUE)
            .stateDate(DEFAULT_STATE_DATE)
            .goalSetDate(DEFAULT_GOAL_SET_DATE)
            .description(DEFAULT_DESCRIPTION);
        return pmEmployeesGoals;
    }

    @Before
    public void initTest() {
        pmEmployeesGoals = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmEmployeesGoals() throws Exception {
        int databaseSizeBeforeCreate = pmEmployeesGoalsRepository.findAll().size();

        // Create the PmEmployeesGoals
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);
        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmEmployeesGoals in the database
        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeCreate + 1);
        PmEmployeesGoals testPmEmployeesGoals = pmEmployeesGoalsList.get(pmEmployeesGoalsList.size() - 1);
        assertThat(testPmEmployeesGoals.getIdEmployeeResponsible()).isEqualTo(DEFAULT_ID_EMPLOYEE_RESPONSIBLE);
        assertThat(testPmEmployeesGoals.getIdEmployeeSetBy()).isEqualTo(DEFAULT_ID_EMPLOYEE_SET_BY);
        assertThat(testPmEmployeesGoals.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testPmEmployeesGoals.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testPmEmployeesGoals.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
        assertThat(testPmEmployeesGoals.getTargetValue()).isEqualTo(DEFAULT_TARGET_VALUE);
        assertThat(testPmEmployeesGoals.getInitialValue()).isEqualTo(DEFAULT_INITIAL_VALUE);
        assertThat(testPmEmployeesGoals.getStateDate()).isEqualTo(DEFAULT_STATE_DATE);
        assertThat(testPmEmployeesGoals.getGoalSetDate()).isEqualTo(DEFAULT_GOAL_SET_DATE);
        assertThat(testPmEmployeesGoals.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmEmployeesGoalsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmEmployeesGoalsRepository.findAll().size();

        // Create the PmEmployeesGoals with an existing ID
        pmEmployeesGoals.setId(1L);
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmEmployeesGoals in the database
        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIdEmployeeResponsibleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setIdEmployeeResponsible(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdEmployeeSetByIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setIdEmployeeSetBy(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setFromDate(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setToDate(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setCurrentValue(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTargetValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setTargetValue(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInitialValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setInitialValue(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setStateDate(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGoalSetDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEmployeesGoalsRepository.findAll().size();
        // set the field null
        pmEmployeesGoals.setGoalSetDate(null);

        // Create the PmEmployeesGoals, which fails.
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(post("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmEmployeesGoals() throws Exception {
        // Initialize the database
        pmEmployeesGoalsRepository.saveAndFlush(pmEmployeesGoals);

        // Get all the pmEmployeesGoalsList
        restPmEmployeesGoalsMockMvc.perform(get("/api/pm-employees-goals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmEmployeesGoals.getId().intValue())))
            .andExpect(jsonPath("$.[*].idEmployeeResponsible").value(hasItem(DEFAULT_ID_EMPLOYEE_RESPONSIBLE.intValue())))
            .andExpect(jsonPath("$.[*].idEmployeeSetBy").value(hasItem(DEFAULT_ID_EMPLOYEE_SET_BY.intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].targetValue").value(hasItem(DEFAULT_TARGET_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].initialValue").value(hasItem(DEFAULT_INITIAL_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(DEFAULT_STATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].goalSetDate").value(hasItem(DEFAULT_GOAL_SET_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmEmployeesGoals() throws Exception {
        // Initialize the database
        pmEmployeesGoalsRepository.saveAndFlush(pmEmployeesGoals);

        // Get the pmEmployeesGoals
        restPmEmployeesGoalsMockMvc.perform(get("/api/pm-employees-goals/{id}", pmEmployeesGoals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmEmployeesGoals.getId().intValue()))
            .andExpect(jsonPath("$.idEmployeeResponsible").value(DEFAULT_ID_EMPLOYEE_RESPONSIBLE.intValue()))
            .andExpect(jsonPath("$.idEmployeeSetBy").value(DEFAULT_ID_EMPLOYEE_SET_BY.intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.intValue()))
            .andExpect(jsonPath("$.targetValue").value(DEFAULT_TARGET_VALUE.intValue()))
            .andExpect(jsonPath("$.initialValue").value(DEFAULT_INITIAL_VALUE.intValue()))
            .andExpect(jsonPath("$.stateDate").value(DEFAULT_STATE_DATE.toString()))
            .andExpect(jsonPath("$.goalSetDate").value(DEFAULT_GOAL_SET_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmEmployeesGoals() throws Exception {
        // Get the pmEmployeesGoals
        restPmEmployeesGoalsMockMvc.perform(get("/api/pm-employees-goals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmEmployeesGoals() throws Exception {
        // Initialize the database
        pmEmployeesGoalsRepository.saveAndFlush(pmEmployeesGoals);
        int databaseSizeBeforeUpdate = pmEmployeesGoalsRepository.findAll().size();

        // Update the pmEmployeesGoals
        PmEmployeesGoals updatedPmEmployeesGoals = pmEmployeesGoalsRepository.findOne(pmEmployeesGoals.getId());
        // Disconnect from session so that the updates on updatedPmEmployeesGoals are not directly saved in db
        em.detach(updatedPmEmployeesGoals);
        updatedPmEmployeesGoals
            .idEmployeeResponsible(UPDATED_ID_EMPLOYEE_RESPONSIBLE)
            .idEmployeeSetBy(UPDATED_ID_EMPLOYEE_SET_BY)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .currentValue(UPDATED_CURRENT_VALUE)
            .targetValue(UPDATED_TARGET_VALUE)
            .initialValue(UPDATED_INITIAL_VALUE)
            .stateDate(UPDATED_STATE_DATE)
            .goalSetDate(UPDATED_GOAL_SET_DATE)
            .description(UPDATED_DESCRIPTION);
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(updatedPmEmployeesGoals);

        restPmEmployeesGoalsMockMvc.perform(put("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isOk());

        // Validate the PmEmployeesGoals in the database
        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeUpdate);
        PmEmployeesGoals testPmEmployeesGoals = pmEmployeesGoalsList.get(pmEmployeesGoalsList.size() - 1);
        assertThat(testPmEmployeesGoals.getIdEmployeeResponsible()).isEqualTo(UPDATED_ID_EMPLOYEE_RESPONSIBLE);
        assertThat(testPmEmployeesGoals.getIdEmployeeSetBy()).isEqualTo(UPDATED_ID_EMPLOYEE_SET_BY);
        assertThat(testPmEmployeesGoals.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testPmEmployeesGoals.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testPmEmployeesGoals.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
        assertThat(testPmEmployeesGoals.getTargetValue()).isEqualTo(UPDATED_TARGET_VALUE);
        assertThat(testPmEmployeesGoals.getInitialValue()).isEqualTo(UPDATED_INITIAL_VALUE);
        assertThat(testPmEmployeesGoals.getStateDate()).isEqualTo(UPDATED_STATE_DATE);
        assertThat(testPmEmployeesGoals.getGoalSetDate()).isEqualTo(UPDATED_GOAL_SET_DATE);
        assertThat(testPmEmployeesGoals.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmEmployeesGoals() throws Exception {
        int databaseSizeBeforeUpdate = pmEmployeesGoalsRepository.findAll().size();

        // Create the PmEmployeesGoals
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO = pmEmployeesGoalsMapper.toDto(pmEmployeesGoals);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmEmployeesGoalsMockMvc.perform(put("/api/pm-employees-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEmployeesGoalsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmEmployeesGoals in the database
        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmEmployeesGoals() throws Exception {
        // Initialize the database
        pmEmployeesGoalsRepository.saveAndFlush(pmEmployeesGoals);
        int databaseSizeBeforeDelete = pmEmployeesGoalsRepository.findAll().size();

        // Get the pmEmployeesGoals
        restPmEmployeesGoalsMockMvc.perform(delete("/api/pm-employees-goals/{id}", pmEmployeesGoals.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmEmployeesGoals> pmEmployeesGoalsList = pmEmployeesGoalsRepository.findAll();
        assertThat(pmEmployeesGoalsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmEmployeesGoals.class);
        PmEmployeesGoals pmEmployeesGoals1 = new PmEmployeesGoals();
        pmEmployeesGoals1.setId(1L);
        PmEmployeesGoals pmEmployeesGoals2 = new PmEmployeesGoals();
        pmEmployeesGoals2.setId(pmEmployeesGoals1.getId());
        assertThat(pmEmployeesGoals1).isEqualTo(pmEmployeesGoals2);
        pmEmployeesGoals2.setId(2L);
        assertThat(pmEmployeesGoals1).isNotEqualTo(pmEmployeesGoals2);
        pmEmployeesGoals1.setId(null);
        assertThat(pmEmployeesGoals1).isNotEqualTo(pmEmployeesGoals2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmEmployeesGoalsDTO.class);
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO1 = new PmEmployeesGoalsDTO();
        pmEmployeesGoalsDTO1.setId(1L);
        PmEmployeesGoalsDTO pmEmployeesGoalsDTO2 = new PmEmployeesGoalsDTO();
        assertThat(pmEmployeesGoalsDTO1).isNotEqualTo(pmEmployeesGoalsDTO2);
        pmEmployeesGoalsDTO2.setId(pmEmployeesGoalsDTO1.getId());
        assertThat(pmEmployeesGoalsDTO1).isEqualTo(pmEmployeesGoalsDTO2);
        pmEmployeesGoalsDTO2.setId(2L);
        assertThat(pmEmployeesGoalsDTO1).isNotEqualTo(pmEmployeesGoalsDTO2);
        pmEmployeesGoalsDTO1.setId(null);
        assertThat(pmEmployeesGoalsDTO1).isNotEqualTo(pmEmployeesGoalsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmEmployeesGoalsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmEmployeesGoalsMapper.fromId(null)).isNull();
    }
}

package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmCorrectiveMeasures;
import com.infostudio.ba.repository.PmCorrectiveMeasuresRepository;
import com.infostudio.ba.service.dto.PmCorrectiveMeasuresDTO;
import com.infostudio.ba.service.mapper.PmCorrectiveMeasuresMapper;
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
 * Test class for the PmCorrectiveMeasuresResource REST controller.
 *
 * @see PmCorrectiveMeasuresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmCorrectiveMeasuresResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MEASURE_SUCCESS_RATE = 1D;
    private static final Double UPDATED_MEASURE_SUCCESS_RATE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PmCorrectiveMeasuresRepository pmCorrectiveMeasuresRepository;

    @Autowired
    private PmCorrectiveMeasuresMapper pmCorrectiveMeasuresMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmCorrectiveMeasuresMockMvc;

    private PmCorrectiveMeasures pmCorrectiveMeasures;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmCorrectiveMeasuresResource pmCorrectiveMeasuresResource = new PmCorrectiveMeasuresResource(pmCorrectiveMeasuresRepository, pmCorrectiveMeasuresMapper);
        this.restPmCorrectiveMeasuresMockMvc = MockMvcBuilders.standaloneSetup(pmCorrectiveMeasuresResource)
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
    public static PmCorrectiveMeasures createEntity(EntityManager em) {
        PmCorrectiveMeasures pmCorrectiveMeasures = new PmCorrectiveMeasures()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .measureSuccessRate(DEFAULT_MEASURE_SUCCESS_RATE)
            .description(DEFAULT_DESCRIPTION)
            .stateDate(DEFAULT_STATE_DATE);
        return pmCorrectiveMeasures;
    }

    @Before
    public void initTest() {
        pmCorrectiveMeasures = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmCorrectiveMeasures() throws Exception {
        int databaseSizeBeforeCreate = pmCorrectiveMeasuresRepository.findAll().size();

        // Create the PmCorrectiveMeasures
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);
        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorrectiveMeasures in the database
        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeCreate + 1);
        PmCorrectiveMeasures testPmCorrectiveMeasures = pmCorrectiveMeasuresList.get(pmCorrectiveMeasuresList.size() - 1);
        assertThat(testPmCorrectiveMeasures.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPmCorrectiveMeasures.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPmCorrectiveMeasures.getMeasureSuccessRate()).isEqualTo(DEFAULT_MEASURE_SUCCESS_RATE);
        assertThat(testPmCorrectiveMeasures.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPmCorrectiveMeasures.getStateDate()).isEqualTo(DEFAULT_STATE_DATE);
    }

    @Test
    @Transactional
    public void createPmCorrectiveMeasuresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmCorrectiveMeasuresRepository.findAll().size();

        // Create the PmCorrectiveMeasures with an existing ID
        pmCorrectiveMeasures.setId(1L);
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmCorrectiveMeasures in the database
        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorrectiveMeasuresRepository.findAll().size();
        // set the field null
        pmCorrectiveMeasures.setStartDate(null);

        // Create the PmCorrectiveMeasures, which fails.
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorrectiveMeasuresRepository.findAll().size();
        // set the field null
        pmCorrectiveMeasures.setEndDate(null);

        // Create the PmCorrectiveMeasures, which fails.
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMeasureSuccessRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorrectiveMeasuresRepository.findAll().size();
        // set the field null
        pmCorrectiveMeasures.setMeasureSuccessRate(null);

        // Create the PmCorrectiveMeasures, which fails.
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorrectiveMeasuresRepository.findAll().size();
        // set the field null
        pmCorrectiveMeasures.setStateDate(null);

        // Create the PmCorrectiveMeasures, which fails.
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        restPmCorrectiveMeasuresMockMvc.perform(post("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmCorrectiveMeasures() throws Exception {
        // Initialize the database
        pmCorrectiveMeasuresRepository.saveAndFlush(pmCorrectiveMeasures);

        // Get all the pmCorrectiveMeasuresList
        restPmCorrectiveMeasuresMockMvc.perform(get("/api/pm-corrective-measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmCorrectiveMeasures.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].measureSuccessRate").value(hasItem(DEFAULT_MEASURE_SUCCESS_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(DEFAULT_STATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPmCorrectiveMeasures() throws Exception {
        // Initialize the database
        pmCorrectiveMeasuresRepository.saveAndFlush(pmCorrectiveMeasures);

        // Get the pmCorrectiveMeasures
        restPmCorrectiveMeasuresMockMvc.perform(get("/api/pm-corrective-measures/{id}", pmCorrectiveMeasures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmCorrectiveMeasures.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.measureSuccessRate").value(DEFAULT_MEASURE_SUCCESS_RATE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.stateDate").value(DEFAULT_STATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmCorrectiveMeasures() throws Exception {
        // Get the pmCorrectiveMeasures
        restPmCorrectiveMeasuresMockMvc.perform(get("/api/pm-corrective-measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmCorrectiveMeasures() throws Exception {
        // Initialize the database
        pmCorrectiveMeasuresRepository.saveAndFlush(pmCorrectiveMeasures);
        int databaseSizeBeforeUpdate = pmCorrectiveMeasuresRepository.findAll().size();

        // Update the pmCorrectiveMeasures
        PmCorrectiveMeasures updatedPmCorrectiveMeasures = pmCorrectiveMeasuresRepository.findOne(pmCorrectiveMeasures.getId());
        // Disconnect from session so that the updates on updatedPmCorrectiveMeasures are not directly saved in db
        em.detach(updatedPmCorrectiveMeasures);
        updatedPmCorrectiveMeasures
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .measureSuccessRate(UPDATED_MEASURE_SUCCESS_RATE)
            .description(UPDATED_DESCRIPTION)
            .stateDate(UPDATED_STATE_DATE);
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(updatedPmCorrectiveMeasures);

        restPmCorrectiveMeasuresMockMvc.perform(put("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isOk());

        // Validate the PmCorrectiveMeasures in the database
        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeUpdate);
        PmCorrectiveMeasures testPmCorrectiveMeasures = pmCorrectiveMeasuresList.get(pmCorrectiveMeasuresList.size() - 1);
        assertThat(testPmCorrectiveMeasures.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPmCorrectiveMeasures.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPmCorrectiveMeasures.getMeasureSuccessRate()).isEqualTo(UPDATED_MEASURE_SUCCESS_RATE);
        assertThat(testPmCorrectiveMeasures.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPmCorrectiveMeasures.getStateDate()).isEqualTo(UPDATED_STATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPmCorrectiveMeasures() throws Exception {
        int databaseSizeBeforeUpdate = pmCorrectiveMeasuresRepository.findAll().size();

        // Create the PmCorrectiveMeasures
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO = pmCorrectiveMeasuresMapper.toDto(pmCorrectiveMeasures);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmCorrectiveMeasuresMockMvc.perform(put("/api/pm-corrective-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorrectiveMeasuresDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorrectiveMeasures in the database
        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmCorrectiveMeasures() throws Exception {
        // Initialize the database
        pmCorrectiveMeasuresRepository.saveAndFlush(pmCorrectiveMeasures);
        int databaseSizeBeforeDelete = pmCorrectiveMeasuresRepository.findAll().size();

        // Get the pmCorrectiveMeasures
        restPmCorrectiveMeasuresMockMvc.perform(delete("/api/pm-corrective-measures/{id}", pmCorrectiveMeasures.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmCorrectiveMeasures> pmCorrectiveMeasuresList = pmCorrectiveMeasuresRepository.findAll();
        assertThat(pmCorrectiveMeasuresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorrectiveMeasures.class);
        PmCorrectiveMeasures pmCorrectiveMeasures1 = new PmCorrectiveMeasures();
        pmCorrectiveMeasures1.setId(1L);
        PmCorrectiveMeasures pmCorrectiveMeasures2 = new PmCorrectiveMeasures();
        pmCorrectiveMeasures2.setId(pmCorrectiveMeasures1.getId());
        assertThat(pmCorrectiveMeasures1).isEqualTo(pmCorrectiveMeasures2);
        pmCorrectiveMeasures2.setId(2L);
        assertThat(pmCorrectiveMeasures1).isNotEqualTo(pmCorrectiveMeasures2);
        pmCorrectiveMeasures1.setId(null);
        assertThat(pmCorrectiveMeasures1).isNotEqualTo(pmCorrectiveMeasures2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorrectiveMeasuresDTO.class);
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO1 = new PmCorrectiveMeasuresDTO();
        pmCorrectiveMeasuresDTO1.setId(1L);
        PmCorrectiveMeasuresDTO pmCorrectiveMeasuresDTO2 = new PmCorrectiveMeasuresDTO();
        assertThat(pmCorrectiveMeasuresDTO1).isNotEqualTo(pmCorrectiveMeasuresDTO2);
        pmCorrectiveMeasuresDTO2.setId(pmCorrectiveMeasuresDTO1.getId());
        assertThat(pmCorrectiveMeasuresDTO1).isEqualTo(pmCorrectiveMeasuresDTO2);
        pmCorrectiveMeasuresDTO2.setId(2L);
        assertThat(pmCorrectiveMeasuresDTO1).isNotEqualTo(pmCorrectiveMeasuresDTO2);
        pmCorrectiveMeasuresDTO1.setId(null);
        assertThat(pmCorrectiveMeasuresDTO1).isNotEqualTo(pmCorrectiveMeasuresDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmCorrectiveMeasuresMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmCorrectiveMeasuresMapper.fromId(null)).isNull();
    }
}

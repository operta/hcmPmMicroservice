package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestCompletions;
import com.infostudio.ba.repository.PmQuestCompletionsRepository;
import com.infostudio.ba.service.dto.PmQuestCompletionsDTO;
import com.infostudio.ba.service.mapper.PmQuestCompletionsMapper;
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
 * Test class for the PmQuestCompletionsResource REST controller.
 *
 * @see PmQuestCompletionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestCompletionsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_EMPLOYEE_COMPLETED_BY = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_COMPLETED_BY = 2L;

    private static final Long DEFAULT_ID_EMPLOYEE_ORDERED_BY = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_ORDERED_BY = 2L;

    private static final LocalDate DEFAULT_STATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PmQuestCompletionsRepository pmQuestCompletionsRepository;

    @Autowired
    private PmQuestCompletionsMapper pmQuestCompletionsMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestCompletionsMockMvc;

    private PmQuestCompletions pmQuestCompletions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestCompletionsResource pmQuestCompletionsResource = new PmQuestCompletionsResource(pmQuestCompletionsRepository, pmQuestCompletionsMapper);
        this.restPmQuestCompletionsMockMvc = MockMvcBuilders.standaloneSetup(pmQuestCompletionsResource)
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
    public static PmQuestCompletions createEntity(EntityManager em) {
        PmQuestCompletions pmQuestCompletions = new PmQuestCompletions()
            .description(DEFAULT_DESCRIPTION)
            .idEmployeeCompletedBy(DEFAULT_ID_EMPLOYEE_COMPLETED_BY)
            .idEmployeeOrderedBy(DEFAULT_ID_EMPLOYEE_ORDERED_BY)
            .stateDate(DEFAULT_STATE_DATE);
        return pmQuestCompletions;
    }

    @Before
    public void initTest() {
        pmQuestCompletions = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestCompletions() throws Exception {
        int databaseSizeBeforeCreate = pmQuestCompletionsRepository.findAll().size();

        // Create the PmQuestCompletions
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);
        restPmQuestCompletionsMockMvc.perform(post("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestCompletions in the database
        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestCompletions testPmQuestCompletions = pmQuestCompletionsList.get(pmQuestCompletionsList.size() - 1);
        assertThat(testPmQuestCompletions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPmQuestCompletions.getIdEmployeeCompletedBy()).isEqualTo(DEFAULT_ID_EMPLOYEE_COMPLETED_BY);
        assertThat(testPmQuestCompletions.getIdEmployeeOrderedBy()).isEqualTo(DEFAULT_ID_EMPLOYEE_ORDERED_BY);
        assertThat(testPmQuestCompletions.getStateDate()).isEqualTo(DEFAULT_STATE_DATE);
    }

    @Test
    @Transactional
    public void createPmQuestCompletionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestCompletionsRepository.findAll().size();

        // Create the PmQuestCompletions with an existing ID
        pmQuestCompletions.setId(1L);
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestCompletionsMockMvc.perform(post("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestCompletions in the database
        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIdEmployeeCompletedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestCompletionsRepository.findAll().size();
        // set the field null
        pmQuestCompletions.setIdEmployeeCompletedBy(null);

        // Create the PmQuestCompletions, which fails.
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);

        restPmQuestCompletionsMockMvc.perform(post("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdEmployeeOrderedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestCompletionsRepository.findAll().size();
        // set the field null
        pmQuestCompletions.setIdEmployeeOrderedBy(null);

        // Create the PmQuestCompletions, which fails.
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);

        restPmQuestCompletionsMockMvc.perform(post("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestCompletionsRepository.findAll().size();
        // set the field null
        pmQuestCompletions.setStateDate(null);

        // Create the PmQuestCompletions, which fails.
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);

        restPmQuestCompletionsMockMvc.perform(post("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestCompletions() throws Exception {
        // Initialize the database
        pmQuestCompletionsRepository.saveAndFlush(pmQuestCompletions);

        // Get all the pmQuestCompletionsList
        restPmQuestCompletionsMockMvc.perform(get("/api/pm-quest-completions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestCompletions.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].idEmployeeCompletedBy").value(hasItem(DEFAULT_ID_EMPLOYEE_COMPLETED_BY.intValue())))
            .andExpect(jsonPath("$.[*].idEmployeeOrderedBy").value(hasItem(DEFAULT_ID_EMPLOYEE_ORDERED_BY.intValue())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(DEFAULT_STATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPmQuestCompletions() throws Exception {
        // Initialize the database
        pmQuestCompletionsRepository.saveAndFlush(pmQuestCompletions);

        // Get the pmQuestCompletions
        restPmQuestCompletionsMockMvc.perform(get("/api/pm-quest-completions/{id}", pmQuestCompletions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestCompletions.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.idEmployeeCompletedBy").value(DEFAULT_ID_EMPLOYEE_COMPLETED_BY.intValue()))
            .andExpect(jsonPath("$.idEmployeeOrderedBy").value(DEFAULT_ID_EMPLOYEE_ORDERED_BY.intValue()))
            .andExpect(jsonPath("$.stateDate").value(DEFAULT_STATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestCompletions() throws Exception {
        // Get the pmQuestCompletions
        restPmQuestCompletionsMockMvc.perform(get("/api/pm-quest-completions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestCompletions() throws Exception {
        // Initialize the database
        pmQuestCompletionsRepository.saveAndFlush(pmQuestCompletions);
        int databaseSizeBeforeUpdate = pmQuestCompletionsRepository.findAll().size();

        // Update the pmQuestCompletions
        PmQuestCompletions updatedPmQuestCompletions = pmQuestCompletionsRepository.findOne(pmQuestCompletions.getId());
        // Disconnect from session so that the updates on updatedPmQuestCompletions are not directly saved in db
        em.detach(updatedPmQuestCompletions);
        updatedPmQuestCompletions
            .description(UPDATED_DESCRIPTION)
            .idEmployeeCompletedBy(UPDATED_ID_EMPLOYEE_COMPLETED_BY)
            .idEmployeeOrderedBy(UPDATED_ID_EMPLOYEE_ORDERED_BY)
            .stateDate(UPDATED_STATE_DATE);
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(updatedPmQuestCompletions);

        restPmQuestCompletionsMockMvc.perform(put("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestCompletions in the database
        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeUpdate);
        PmQuestCompletions testPmQuestCompletions = pmQuestCompletionsList.get(pmQuestCompletionsList.size() - 1);
        assertThat(testPmQuestCompletions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPmQuestCompletions.getIdEmployeeCompletedBy()).isEqualTo(UPDATED_ID_EMPLOYEE_COMPLETED_BY);
        assertThat(testPmQuestCompletions.getIdEmployeeOrderedBy()).isEqualTo(UPDATED_ID_EMPLOYEE_ORDERED_BY);
        assertThat(testPmQuestCompletions.getStateDate()).isEqualTo(UPDATED_STATE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestCompletions() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestCompletionsRepository.findAll().size();

        // Create the PmQuestCompletions
        PmQuestCompletionsDTO pmQuestCompletionsDTO = pmQuestCompletionsMapper.toDto(pmQuestCompletions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestCompletionsMockMvc.perform(put("/api/pm-quest-completions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestCompletionsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestCompletions in the database
        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestCompletions() throws Exception {
        // Initialize the database
        pmQuestCompletionsRepository.saveAndFlush(pmQuestCompletions);
        int databaseSizeBeforeDelete = pmQuestCompletionsRepository.findAll().size();

        // Get the pmQuestCompletions
        restPmQuestCompletionsMockMvc.perform(delete("/api/pm-quest-completions/{id}", pmQuestCompletions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestCompletions> pmQuestCompletionsList = pmQuestCompletionsRepository.findAll();
        assertThat(pmQuestCompletionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestCompletions.class);
        PmQuestCompletions pmQuestCompletions1 = new PmQuestCompletions();
        pmQuestCompletions1.setId(1L);
        PmQuestCompletions pmQuestCompletions2 = new PmQuestCompletions();
        pmQuestCompletions2.setId(pmQuestCompletions1.getId());
        assertThat(pmQuestCompletions1).isEqualTo(pmQuestCompletions2);
        pmQuestCompletions2.setId(2L);
        assertThat(pmQuestCompletions1).isNotEqualTo(pmQuestCompletions2);
        pmQuestCompletions1.setId(null);
        assertThat(pmQuestCompletions1).isNotEqualTo(pmQuestCompletions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestCompletionsDTO.class);
        PmQuestCompletionsDTO pmQuestCompletionsDTO1 = new PmQuestCompletionsDTO();
        pmQuestCompletionsDTO1.setId(1L);
        PmQuestCompletionsDTO pmQuestCompletionsDTO2 = new PmQuestCompletionsDTO();
        assertThat(pmQuestCompletionsDTO1).isNotEqualTo(pmQuestCompletionsDTO2);
        pmQuestCompletionsDTO2.setId(pmQuestCompletionsDTO1.getId());
        assertThat(pmQuestCompletionsDTO1).isEqualTo(pmQuestCompletionsDTO2);
        pmQuestCompletionsDTO2.setId(2L);
        assertThat(pmQuestCompletionsDTO1).isNotEqualTo(pmQuestCompletionsDTO2);
        pmQuestCompletionsDTO1.setId(null);
        assertThat(pmQuestCompletionsDTO1).isNotEqualTo(pmQuestCompletionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestCompletionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestCompletionsMapper.fromId(null)).isNull();
    }
}

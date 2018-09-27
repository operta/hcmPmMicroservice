package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmGoals;
import com.infostudio.ba.repository.PmGoalsRepository;
import com.infostudio.ba.service.dto.PmGoalsDTO;
import com.infostudio.ba.service.mapper.PmGoalsMapper;
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
import java.util.List;

import static com.infostudio.ba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PmGoalsResource REST controller.
 *
 * @see PmGoalsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmGoalsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_EMPLOYEE_OWNER = 1L;
    private static final Long UPDATED_ID_EMPLOYEE_OWNER = 2L;

    private static final String DEFAULT_IS_ACTIVE = "AAAAAAAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBBBBBBB";

    @Autowired
    private PmGoalsRepository pmGoalsRepository;

    @Autowired
    private PmGoalsMapper pmGoalsMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmGoalsMockMvc;

    private PmGoals pmGoals;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmGoalsResource pmGoalsResource = new PmGoalsResource(pmGoalsRepository, pmGoalsMapper);
        this.restPmGoalsMockMvc = MockMvcBuilders.standaloneSetup(pmGoalsResource)
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
    public static PmGoals createEntity(EntityManager em) {
        PmGoals pmGoals = new PmGoals()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .idEmployeeOwner(DEFAULT_ID_EMPLOYEE_OWNER)
            .isActive(DEFAULT_IS_ACTIVE);
        return pmGoals;
    }

    @Before
    public void initTest() {
        pmGoals = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmGoals() throws Exception {
        int databaseSizeBeforeCreate = pmGoalsRepository.findAll().size();

        // Create the PmGoals
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);
        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoals in the database
        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeCreate + 1);
        PmGoals testPmGoals = pmGoalsList.get(pmGoalsList.size() - 1);
        assertThat(testPmGoals.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmGoals.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmGoals.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPmGoals.getIdEmployeeOwner()).isEqualTo(DEFAULT_ID_EMPLOYEE_OWNER);
        assertThat(testPmGoals.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createPmGoalsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmGoalsRepository.findAll().size();

        // Create the PmGoals with an existing ID
        pmGoals.setId(1L);
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmGoals in the database
        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsRepository.findAll().size();
        // set the field null
        pmGoals.setCode(null);

        // Create the PmGoals, which fails.
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsRepository.findAll().size();
        // set the field null
        pmGoals.setName(null);

        // Create the PmGoals, which fails.
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdEmployeeOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsRepository.findAll().size();
        // set the field null
        pmGoals.setIdEmployeeOwner(null);

        // Create the PmGoals, which fails.
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalsRepository.findAll().size();
        // set the field null
        pmGoals.setIsActive(null);

        // Create the PmGoals, which fails.
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        restPmGoalsMockMvc.perform(post("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmGoals() throws Exception {
        // Initialize the database
        pmGoalsRepository.saveAndFlush(pmGoals);

        // Get all the pmGoalsList
        restPmGoalsMockMvc.perform(get("/api/pm-goals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmGoals.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].idEmployeeOwner").value(hasItem(DEFAULT_ID_EMPLOYEE_OWNER.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.toString())));
    }

    @Test
    @Transactional
    public void getPmGoals() throws Exception {
        // Initialize the database
        pmGoalsRepository.saveAndFlush(pmGoals);

        // Get the pmGoals
        restPmGoalsMockMvc.perform(get("/api/pm-goals/{id}", pmGoals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmGoals.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.idEmployeeOwner").value(DEFAULT_ID_EMPLOYEE_OWNER.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmGoals() throws Exception {
        // Get the pmGoals
        restPmGoalsMockMvc.perform(get("/api/pm-goals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmGoals() throws Exception {
        // Initialize the database
        pmGoalsRepository.saveAndFlush(pmGoals);
        int databaseSizeBeforeUpdate = pmGoalsRepository.findAll().size();

        // Update the pmGoals
        PmGoals updatedPmGoals = pmGoalsRepository.findOne(pmGoals.getId());
        // Disconnect from session so that the updates on updatedPmGoals are not directly saved in db
        em.detach(updatedPmGoals);
        updatedPmGoals
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idEmployeeOwner(UPDATED_ID_EMPLOYEE_OWNER)
            .isActive(UPDATED_IS_ACTIVE);
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(updatedPmGoals);

        restPmGoalsMockMvc.perform(put("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isOk());

        // Validate the PmGoals in the database
        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeUpdate);
        PmGoals testPmGoals = pmGoalsList.get(pmGoalsList.size() - 1);
        assertThat(testPmGoals.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmGoals.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmGoals.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPmGoals.getIdEmployeeOwner()).isEqualTo(UPDATED_ID_EMPLOYEE_OWNER);
        assertThat(testPmGoals.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingPmGoals() throws Exception {
        int databaseSizeBeforeUpdate = pmGoalsRepository.findAll().size();

        // Create the PmGoals
        PmGoalsDTO pmGoalsDTO = pmGoalsMapper.toDto(pmGoals);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmGoalsMockMvc.perform(put("/api/pm-goals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoals in the database
        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmGoals() throws Exception {
        // Initialize the database
        pmGoalsRepository.saveAndFlush(pmGoals);
        int databaseSizeBeforeDelete = pmGoalsRepository.findAll().size();

        // Get the pmGoals
        restPmGoalsMockMvc.perform(delete("/api/pm-goals/{id}", pmGoals.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmGoals> pmGoalsList = pmGoalsRepository.findAll();
        assertThat(pmGoalsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoals.class);
        PmGoals pmGoals1 = new PmGoals();
        pmGoals1.setId(1L);
        PmGoals pmGoals2 = new PmGoals();
        pmGoals2.setId(pmGoals1.getId());
        assertThat(pmGoals1).isEqualTo(pmGoals2);
        pmGoals2.setId(2L);
        assertThat(pmGoals1).isNotEqualTo(pmGoals2);
        pmGoals1.setId(null);
        assertThat(pmGoals1).isNotEqualTo(pmGoals2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalsDTO.class);
        PmGoalsDTO pmGoalsDTO1 = new PmGoalsDTO();
        pmGoalsDTO1.setId(1L);
        PmGoalsDTO pmGoalsDTO2 = new PmGoalsDTO();
        assertThat(pmGoalsDTO1).isNotEqualTo(pmGoalsDTO2);
        pmGoalsDTO2.setId(pmGoalsDTO1.getId());
        assertThat(pmGoalsDTO1).isEqualTo(pmGoalsDTO2);
        pmGoalsDTO2.setId(2L);
        assertThat(pmGoalsDTO1).isNotEqualTo(pmGoalsDTO2);
        pmGoalsDTO1.setId(null);
        assertThat(pmGoalsDTO1).isNotEqualTo(pmGoalsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmGoalsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmGoalsMapper.fromId(null)).isNull();
    }
}

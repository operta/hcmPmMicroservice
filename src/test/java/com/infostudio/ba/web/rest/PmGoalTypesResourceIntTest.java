package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmGoalTypes;
import com.infostudio.ba.repository.PmGoalTypesRepository;
import com.infostudio.ba.service.dto.PmGoalTypesDTO;
import com.infostudio.ba.service.mapper.PmGoalTypesMapper;
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
 * Test class for the PmGoalTypesResource REST controller.
 *
 * @see PmGoalTypesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmGoalTypesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PmGoalTypesRepository pmGoalTypesRepository;

    @Autowired
    private PmGoalTypesMapper pmGoalTypesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmGoalTypesMockMvc;

    private PmGoalTypes pmGoalTypes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmGoalTypesResource pmGoalTypesResource = new PmGoalTypesResource(pmGoalTypesRepository, pmGoalTypesMapper);
        this.restPmGoalTypesMockMvc = MockMvcBuilders.standaloneSetup(pmGoalTypesResource)
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
    public static PmGoalTypes createEntity(EntityManager em) {
        PmGoalTypes pmGoalTypes = new PmGoalTypes()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return pmGoalTypes;
    }

    @Before
    public void initTest() {
        pmGoalTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmGoalTypes() throws Exception {
        int databaseSizeBeforeCreate = pmGoalTypesRepository.findAll().size();

        // Create the PmGoalTypes
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);
        restPmGoalTypesMockMvc.perform(post("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalTypes in the database
        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeCreate + 1);
        PmGoalTypes testPmGoalTypes = pmGoalTypesList.get(pmGoalTypesList.size() - 1);
        assertThat(testPmGoalTypes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmGoalTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPmGoalTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmGoalTypesRepository.findAll().size();

        // Create the PmGoalTypes with an existing ID
        pmGoalTypes.setId(1L);
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmGoalTypesMockMvc.perform(post("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmGoalTypes in the database
        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalTypesRepository.findAll().size();
        // set the field null
        pmGoalTypes.setCode(null);

        // Create the PmGoalTypes, which fails.
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);

        restPmGoalTypesMockMvc.perform(post("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalTypesRepository.findAll().size();
        // set the field null
        pmGoalTypes.setName(null);

        // Create the PmGoalTypes, which fails.
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);

        restPmGoalTypesMockMvc.perform(post("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmGoalTypes() throws Exception {
        // Initialize the database
        pmGoalTypesRepository.saveAndFlush(pmGoalTypes);

        // Get all the pmGoalTypesList
        restPmGoalTypesMockMvc.perform(get("/api/pm-goal-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmGoalTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPmGoalTypes() throws Exception {
        // Initialize the database
        pmGoalTypesRepository.saveAndFlush(pmGoalTypes);

        // Get the pmGoalTypes
        restPmGoalTypesMockMvc.perform(get("/api/pm-goal-types/{id}", pmGoalTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmGoalTypes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmGoalTypes() throws Exception {
        // Get the pmGoalTypes
        restPmGoalTypesMockMvc.perform(get("/api/pm-goal-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmGoalTypes() throws Exception {
        // Initialize the database
        pmGoalTypesRepository.saveAndFlush(pmGoalTypes);
        int databaseSizeBeforeUpdate = pmGoalTypesRepository.findAll().size();

        // Update the pmGoalTypes
        PmGoalTypes updatedPmGoalTypes = pmGoalTypesRepository.findOne(pmGoalTypes.getId());
        // Disconnect from session so that the updates on updatedPmGoalTypes are not directly saved in db
        em.detach(updatedPmGoalTypes);
        updatedPmGoalTypes
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(updatedPmGoalTypes);

        restPmGoalTypesMockMvc.perform(put("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isOk());

        // Validate the PmGoalTypes in the database
        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeUpdate);
        PmGoalTypes testPmGoalTypes = pmGoalTypesList.get(pmGoalTypesList.size() - 1);
        assertThat(testPmGoalTypes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmGoalTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPmGoalTypes() throws Exception {
        int databaseSizeBeforeUpdate = pmGoalTypesRepository.findAll().size();

        // Create the PmGoalTypes
        PmGoalTypesDTO pmGoalTypesDTO = pmGoalTypesMapper.toDto(pmGoalTypes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmGoalTypesMockMvc.perform(put("/api/pm-goal-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalTypes in the database
        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmGoalTypes() throws Exception {
        // Initialize the database
        pmGoalTypesRepository.saveAndFlush(pmGoalTypes);
        int databaseSizeBeforeDelete = pmGoalTypesRepository.findAll().size();

        // Get the pmGoalTypes
        restPmGoalTypesMockMvc.perform(delete("/api/pm-goal-types/{id}", pmGoalTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmGoalTypes> pmGoalTypesList = pmGoalTypesRepository.findAll();
        assertThat(pmGoalTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalTypes.class);
        PmGoalTypes pmGoalTypes1 = new PmGoalTypes();
        pmGoalTypes1.setId(1L);
        PmGoalTypes pmGoalTypes2 = new PmGoalTypes();
        pmGoalTypes2.setId(pmGoalTypes1.getId());
        assertThat(pmGoalTypes1).isEqualTo(pmGoalTypes2);
        pmGoalTypes2.setId(2L);
        assertThat(pmGoalTypes1).isNotEqualTo(pmGoalTypes2);
        pmGoalTypes1.setId(null);
        assertThat(pmGoalTypes1).isNotEqualTo(pmGoalTypes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalTypesDTO.class);
        PmGoalTypesDTO pmGoalTypesDTO1 = new PmGoalTypesDTO();
        pmGoalTypesDTO1.setId(1L);
        PmGoalTypesDTO pmGoalTypesDTO2 = new PmGoalTypesDTO();
        assertThat(pmGoalTypesDTO1).isNotEqualTo(pmGoalTypesDTO2);
        pmGoalTypesDTO2.setId(pmGoalTypesDTO1.getId());
        assertThat(pmGoalTypesDTO1).isEqualTo(pmGoalTypesDTO2);
        pmGoalTypesDTO2.setId(2L);
        assertThat(pmGoalTypesDTO1).isNotEqualTo(pmGoalTypesDTO2);
        pmGoalTypesDTO1.setId(null);
        assertThat(pmGoalTypesDTO1).isNotEqualTo(pmGoalTypesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmGoalTypesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmGoalTypesMapper.fromId(null)).isNull();
    }
}

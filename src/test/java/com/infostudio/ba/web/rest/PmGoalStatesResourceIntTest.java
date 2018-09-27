package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmGoalStates;
import com.infostudio.ba.repository.PmGoalStatesRepository;
import com.infostudio.ba.service.dto.PmGoalStatesDTO;
import com.infostudio.ba.service.mapper.PmGoalStatesMapper;
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
 * Test class for the PmGoalStatesResource REST controller.
 *
 * @see PmGoalStatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmGoalStatesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PmGoalStatesRepository pmGoalStatesRepository;

    @Autowired
    private PmGoalStatesMapper pmGoalStatesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmGoalStatesMockMvc;

    private PmGoalStates pmGoalStates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmGoalStatesResource pmGoalStatesResource = new PmGoalStatesResource(pmGoalStatesRepository, pmGoalStatesMapper);
        this.restPmGoalStatesMockMvc = MockMvcBuilders.standaloneSetup(pmGoalStatesResource)
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
    public static PmGoalStates createEntity(EntityManager em) {
        PmGoalStates pmGoalStates = new PmGoalStates()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return pmGoalStates;
    }

    @Before
    public void initTest() {
        pmGoalStates = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmGoalStates() throws Exception {
        int databaseSizeBeforeCreate = pmGoalStatesRepository.findAll().size();

        // Create the PmGoalStates
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);
        restPmGoalStatesMockMvc.perform(post("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalStates in the database
        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeCreate + 1);
        PmGoalStates testPmGoalStates = pmGoalStatesList.get(pmGoalStatesList.size() - 1);
        assertThat(testPmGoalStates.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmGoalStates.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPmGoalStatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmGoalStatesRepository.findAll().size();

        // Create the PmGoalStates with an existing ID
        pmGoalStates.setId(1L);
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmGoalStatesMockMvc.perform(post("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmGoalStates in the database
        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalStatesRepository.findAll().size();
        // set the field null
        pmGoalStates.setCode(null);

        // Create the PmGoalStates, which fails.
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);

        restPmGoalStatesMockMvc.perform(post("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmGoalStatesRepository.findAll().size();
        // set the field null
        pmGoalStates.setName(null);

        // Create the PmGoalStates, which fails.
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);

        restPmGoalStatesMockMvc.perform(post("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmGoalStates() throws Exception {
        // Initialize the database
        pmGoalStatesRepository.saveAndFlush(pmGoalStates);

        // Get all the pmGoalStatesList
        restPmGoalStatesMockMvc.perform(get("/api/pm-goal-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmGoalStates.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPmGoalStates() throws Exception {
        // Initialize the database
        pmGoalStatesRepository.saveAndFlush(pmGoalStates);

        // Get the pmGoalStates
        restPmGoalStatesMockMvc.perform(get("/api/pm-goal-states/{id}", pmGoalStates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmGoalStates.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmGoalStates() throws Exception {
        // Get the pmGoalStates
        restPmGoalStatesMockMvc.perform(get("/api/pm-goal-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmGoalStates() throws Exception {
        // Initialize the database
        pmGoalStatesRepository.saveAndFlush(pmGoalStates);
        int databaseSizeBeforeUpdate = pmGoalStatesRepository.findAll().size();

        // Update the pmGoalStates
        PmGoalStates updatedPmGoalStates = pmGoalStatesRepository.findOne(pmGoalStates.getId());
        // Disconnect from session so that the updates on updatedPmGoalStates are not directly saved in db
        em.detach(updatedPmGoalStates);
        updatedPmGoalStates
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(updatedPmGoalStates);

        restPmGoalStatesMockMvc.perform(put("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isOk());

        // Validate the PmGoalStates in the database
        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeUpdate);
        PmGoalStates testPmGoalStates = pmGoalStatesList.get(pmGoalStatesList.size() - 1);
        assertThat(testPmGoalStates.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmGoalStates.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPmGoalStates() throws Exception {
        int databaseSizeBeforeUpdate = pmGoalStatesRepository.findAll().size();

        // Create the PmGoalStates
        PmGoalStatesDTO pmGoalStatesDTO = pmGoalStatesMapper.toDto(pmGoalStates);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmGoalStatesMockMvc.perform(put("/api/pm-goal-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalStates in the database
        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmGoalStates() throws Exception {
        // Initialize the database
        pmGoalStatesRepository.saveAndFlush(pmGoalStates);
        int databaseSizeBeforeDelete = pmGoalStatesRepository.findAll().size();

        // Get the pmGoalStates
        restPmGoalStatesMockMvc.perform(delete("/api/pm-goal-states/{id}", pmGoalStates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmGoalStates> pmGoalStatesList = pmGoalStatesRepository.findAll();
        assertThat(pmGoalStatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalStates.class);
        PmGoalStates pmGoalStates1 = new PmGoalStates();
        pmGoalStates1.setId(1L);
        PmGoalStates pmGoalStates2 = new PmGoalStates();
        pmGoalStates2.setId(pmGoalStates1.getId());
        assertThat(pmGoalStates1).isEqualTo(pmGoalStates2);
        pmGoalStates2.setId(2L);
        assertThat(pmGoalStates1).isNotEqualTo(pmGoalStates2);
        pmGoalStates1.setId(null);
        assertThat(pmGoalStates1).isNotEqualTo(pmGoalStates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalStatesDTO.class);
        PmGoalStatesDTO pmGoalStatesDTO1 = new PmGoalStatesDTO();
        pmGoalStatesDTO1.setId(1L);
        PmGoalStatesDTO pmGoalStatesDTO2 = new PmGoalStatesDTO();
        assertThat(pmGoalStatesDTO1).isNotEqualTo(pmGoalStatesDTO2);
        pmGoalStatesDTO2.setId(pmGoalStatesDTO1.getId());
        assertThat(pmGoalStatesDTO1).isEqualTo(pmGoalStatesDTO2);
        pmGoalStatesDTO2.setId(2L);
        assertThat(pmGoalStatesDTO1).isNotEqualTo(pmGoalStatesDTO2);
        pmGoalStatesDTO1.setId(null);
        assertThat(pmGoalStatesDTO1).isNotEqualTo(pmGoalStatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmGoalStatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmGoalStatesMapper.fromId(null)).isNull();
    }
}

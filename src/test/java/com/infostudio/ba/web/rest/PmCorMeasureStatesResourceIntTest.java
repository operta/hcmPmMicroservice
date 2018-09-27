package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmCorMeasureStates;
import com.infostudio.ba.repository.PmCorMeasureStatesRepository;
import com.infostudio.ba.service.dto.PmCorMeasureStatesDTO;
import com.infostudio.ba.service.mapper.PmCorMeasureStatesMapper;
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
 * Test class for the PmCorMeasureStatesResource REST controller.
 *
 * @see PmCorMeasureStatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmCorMeasureStatesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmCorMeasureStatesRepository pmCorMeasureStatesRepository;

    @Autowired
    private PmCorMeasureStatesMapper pmCorMeasureStatesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmCorMeasureStatesMockMvc;

    private PmCorMeasureStates pmCorMeasureStates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmCorMeasureStatesResource pmCorMeasureStatesResource = new PmCorMeasureStatesResource(pmCorMeasureStatesRepository, pmCorMeasureStatesMapper);
        this.restPmCorMeasureStatesMockMvc = MockMvcBuilders.standaloneSetup(pmCorMeasureStatesResource)
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
    public static PmCorMeasureStates createEntity(EntityManager em) {
        PmCorMeasureStates pmCorMeasureStates = new PmCorMeasureStates()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmCorMeasureStates;
    }

    @Before
    public void initTest() {
        pmCorMeasureStates = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmCorMeasureStates() throws Exception {
        int databaseSizeBeforeCreate = pmCorMeasureStatesRepository.findAll().size();

        // Create the PmCorMeasureStates
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);
        restPmCorMeasureStatesMockMvc.perform(post("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorMeasureStates in the database
        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeCreate + 1);
        PmCorMeasureStates testPmCorMeasureStates = pmCorMeasureStatesList.get(pmCorMeasureStatesList.size() - 1);
        assertThat(testPmCorMeasureStates.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmCorMeasureStates.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmCorMeasureStates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmCorMeasureStatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmCorMeasureStatesRepository.findAll().size();

        // Create the PmCorMeasureStates with an existing ID
        pmCorMeasureStates.setId(1L);
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmCorMeasureStatesMockMvc.perform(post("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmCorMeasureStates in the database
        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorMeasureStatesRepository.findAll().size();
        // set the field null
        pmCorMeasureStates.setCode(null);

        // Create the PmCorMeasureStates, which fails.
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);

        restPmCorMeasureStatesMockMvc.perform(post("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorMeasureStatesRepository.findAll().size();
        // set the field null
        pmCorMeasureStates.setName(null);

        // Create the PmCorMeasureStates, which fails.
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);

        restPmCorMeasureStatesMockMvc.perform(post("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmCorMeasureStates() throws Exception {
        // Initialize the database
        pmCorMeasureStatesRepository.saveAndFlush(pmCorMeasureStates);

        // Get all the pmCorMeasureStatesList
        restPmCorMeasureStatesMockMvc.perform(get("/api/pm-cor-measure-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmCorMeasureStates.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmCorMeasureStates() throws Exception {
        // Initialize the database
        pmCorMeasureStatesRepository.saveAndFlush(pmCorMeasureStates);

        // Get the pmCorMeasureStates
        restPmCorMeasureStatesMockMvc.perform(get("/api/pm-cor-measure-states/{id}", pmCorMeasureStates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmCorMeasureStates.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmCorMeasureStates() throws Exception {
        // Get the pmCorMeasureStates
        restPmCorMeasureStatesMockMvc.perform(get("/api/pm-cor-measure-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmCorMeasureStates() throws Exception {
        // Initialize the database
        pmCorMeasureStatesRepository.saveAndFlush(pmCorMeasureStates);
        int databaseSizeBeforeUpdate = pmCorMeasureStatesRepository.findAll().size();

        // Update the pmCorMeasureStates
        PmCorMeasureStates updatedPmCorMeasureStates = pmCorMeasureStatesRepository.findOne(pmCorMeasureStates.getId());
        // Disconnect from session so that the updates on updatedPmCorMeasureStates are not directly saved in db
        em.detach(updatedPmCorMeasureStates);
        updatedPmCorMeasureStates
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(updatedPmCorMeasureStates);

        restPmCorMeasureStatesMockMvc.perform(put("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isOk());

        // Validate the PmCorMeasureStates in the database
        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeUpdate);
        PmCorMeasureStates testPmCorMeasureStates = pmCorMeasureStatesList.get(pmCorMeasureStatesList.size() - 1);
        assertThat(testPmCorMeasureStates.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmCorMeasureStates.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmCorMeasureStates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmCorMeasureStates() throws Exception {
        int databaseSizeBeforeUpdate = pmCorMeasureStatesRepository.findAll().size();

        // Create the PmCorMeasureStates
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO = pmCorMeasureStatesMapper.toDto(pmCorMeasureStates);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmCorMeasureStatesMockMvc.perform(put("/api/pm-cor-measure-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorMeasureStates in the database
        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmCorMeasureStates() throws Exception {
        // Initialize the database
        pmCorMeasureStatesRepository.saveAndFlush(pmCorMeasureStates);
        int databaseSizeBeforeDelete = pmCorMeasureStatesRepository.findAll().size();

        // Get the pmCorMeasureStates
        restPmCorMeasureStatesMockMvc.perform(delete("/api/pm-cor-measure-states/{id}", pmCorMeasureStates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmCorMeasureStates> pmCorMeasureStatesList = pmCorMeasureStatesRepository.findAll();
        assertThat(pmCorMeasureStatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorMeasureStates.class);
        PmCorMeasureStates pmCorMeasureStates1 = new PmCorMeasureStates();
        pmCorMeasureStates1.setId(1L);
        PmCorMeasureStates pmCorMeasureStates2 = new PmCorMeasureStates();
        pmCorMeasureStates2.setId(pmCorMeasureStates1.getId());
        assertThat(pmCorMeasureStates1).isEqualTo(pmCorMeasureStates2);
        pmCorMeasureStates2.setId(2L);
        assertThat(pmCorMeasureStates1).isNotEqualTo(pmCorMeasureStates2);
        pmCorMeasureStates1.setId(null);
        assertThat(pmCorMeasureStates1).isNotEqualTo(pmCorMeasureStates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorMeasureStatesDTO.class);
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO1 = new PmCorMeasureStatesDTO();
        pmCorMeasureStatesDTO1.setId(1L);
        PmCorMeasureStatesDTO pmCorMeasureStatesDTO2 = new PmCorMeasureStatesDTO();
        assertThat(pmCorMeasureStatesDTO1).isNotEqualTo(pmCorMeasureStatesDTO2);
        pmCorMeasureStatesDTO2.setId(pmCorMeasureStatesDTO1.getId());
        assertThat(pmCorMeasureStatesDTO1).isEqualTo(pmCorMeasureStatesDTO2);
        pmCorMeasureStatesDTO2.setId(2L);
        assertThat(pmCorMeasureStatesDTO1).isNotEqualTo(pmCorMeasureStatesDTO2);
        pmCorMeasureStatesDTO1.setId(null);
        assertThat(pmCorMeasureStatesDTO1).isNotEqualTo(pmCorMeasureStatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmCorMeasureStatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmCorMeasureStatesMapper.fromId(null)).isNull();
    }
}

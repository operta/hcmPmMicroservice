package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmEvaluationStates;
import com.infostudio.ba.repository.PmEvaluationStatesRepository;
import com.infostudio.ba.service.dto.PmEvaluationStatesDTO;
import com.infostudio.ba.service.mapper.PmEvaluationStatesMapper;
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
 * Test class for the PmEvaluationStatesResource REST controller.
 *
 * @see PmEvaluationStatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmEvaluationStatesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmEvaluationStatesRepository pmEvaluationStatesRepository;

    @Autowired
    private PmEvaluationStatesMapper pmEvaluationStatesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmEvaluationStatesMockMvc;

    private PmEvaluationStates pmEvaluationStates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmEvaluationStatesResource pmEvaluationStatesResource = new PmEvaluationStatesResource(pmEvaluationStatesRepository, pmEvaluationStatesMapper);
        this.restPmEvaluationStatesMockMvc = MockMvcBuilders.standaloneSetup(pmEvaluationStatesResource)
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
    public static PmEvaluationStates createEntity(EntityManager em) {
        PmEvaluationStates pmEvaluationStates = new PmEvaluationStates()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmEvaluationStates;
    }

    @Before
    public void initTest() {
        pmEvaluationStates = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmEvaluationStates() throws Exception {
        int databaseSizeBeforeCreate = pmEvaluationStatesRepository.findAll().size();

        // Create the PmEvaluationStates
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);
        restPmEvaluationStatesMockMvc.perform(post("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmEvaluationStates in the database
        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeCreate + 1);
        PmEvaluationStates testPmEvaluationStates = pmEvaluationStatesList.get(pmEvaluationStatesList.size() - 1);
        assertThat(testPmEvaluationStates.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmEvaluationStates.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmEvaluationStates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmEvaluationStatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmEvaluationStatesRepository.findAll().size();

        // Create the PmEvaluationStates with an existing ID
        pmEvaluationStates.setId(1L);
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmEvaluationStatesMockMvc.perform(post("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmEvaluationStates in the database
        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEvaluationStatesRepository.findAll().size();
        // set the field null
        pmEvaluationStates.setCode(null);

        // Create the PmEvaluationStates, which fails.
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);

        restPmEvaluationStatesMockMvc.perform(post("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmEvaluationStatesRepository.findAll().size();
        // set the field null
        pmEvaluationStates.setName(null);

        // Create the PmEvaluationStates, which fails.
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);

        restPmEvaluationStatesMockMvc.perform(post("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmEvaluationStates() throws Exception {
        // Initialize the database
        pmEvaluationStatesRepository.saveAndFlush(pmEvaluationStates);

        // Get all the pmEvaluationStatesList
        restPmEvaluationStatesMockMvc.perform(get("/api/pm-evaluation-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmEvaluationStates.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmEvaluationStates() throws Exception {
        // Initialize the database
        pmEvaluationStatesRepository.saveAndFlush(pmEvaluationStates);

        // Get the pmEvaluationStates
        restPmEvaluationStatesMockMvc.perform(get("/api/pm-evaluation-states/{id}", pmEvaluationStates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmEvaluationStates.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmEvaluationStates() throws Exception {
        // Get the pmEvaluationStates
        restPmEvaluationStatesMockMvc.perform(get("/api/pm-evaluation-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmEvaluationStates() throws Exception {
        // Initialize the database
        pmEvaluationStatesRepository.saveAndFlush(pmEvaluationStates);
        int databaseSizeBeforeUpdate = pmEvaluationStatesRepository.findAll().size();

        // Update the pmEvaluationStates
        PmEvaluationStates updatedPmEvaluationStates = pmEvaluationStatesRepository.findOne(pmEvaluationStates.getId());
        // Disconnect from session so that the updates on updatedPmEvaluationStates are not directly saved in db
        em.detach(updatedPmEvaluationStates);
        updatedPmEvaluationStates
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(updatedPmEvaluationStates);

        restPmEvaluationStatesMockMvc.perform(put("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isOk());

        // Validate the PmEvaluationStates in the database
        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeUpdate);
        PmEvaluationStates testPmEvaluationStates = pmEvaluationStatesList.get(pmEvaluationStatesList.size() - 1);
        assertThat(testPmEvaluationStates.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmEvaluationStates.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmEvaluationStates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmEvaluationStates() throws Exception {
        int databaseSizeBeforeUpdate = pmEvaluationStatesRepository.findAll().size();

        // Create the PmEvaluationStates
        PmEvaluationStatesDTO pmEvaluationStatesDTO = pmEvaluationStatesMapper.toDto(pmEvaluationStates);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmEvaluationStatesMockMvc.perform(put("/api/pm-evaluation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmEvaluationStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmEvaluationStates in the database
        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmEvaluationStates() throws Exception {
        // Initialize the database
        pmEvaluationStatesRepository.saveAndFlush(pmEvaluationStates);
        int databaseSizeBeforeDelete = pmEvaluationStatesRepository.findAll().size();

        // Get the pmEvaluationStates
        restPmEvaluationStatesMockMvc.perform(delete("/api/pm-evaluation-states/{id}", pmEvaluationStates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmEvaluationStates> pmEvaluationStatesList = pmEvaluationStatesRepository.findAll();
        assertThat(pmEvaluationStatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmEvaluationStates.class);
        PmEvaluationStates pmEvaluationStates1 = new PmEvaluationStates();
        pmEvaluationStates1.setId(1L);
        PmEvaluationStates pmEvaluationStates2 = new PmEvaluationStates();
        pmEvaluationStates2.setId(pmEvaluationStates1.getId());
        assertThat(pmEvaluationStates1).isEqualTo(pmEvaluationStates2);
        pmEvaluationStates2.setId(2L);
        assertThat(pmEvaluationStates1).isNotEqualTo(pmEvaluationStates2);
        pmEvaluationStates1.setId(null);
        assertThat(pmEvaluationStates1).isNotEqualTo(pmEvaluationStates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmEvaluationStatesDTO.class);
        PmEvaluationStatesDTO pmEvaluationStatesDTO1 = new PmEvaluationStatesDTO();
        pmEvaluationStatesDTO1.setId(1L);
        PmEvaluationStatesDTO pmEvaluationStatesDTO2 = new PmEvaluationStatesDTO();
        assertThat(pmEvaluationStatesDTO1).isNotEqualTo(pmEvaluationStatesDTO2);
        pmEvaluationStatesDTO2.setId(pmEvaluationStatesDTO1.getId());
        assertThat(pmEvaluationStatesDTO1).isEqualTo(pmEvaluationStatesDTO2);
        pmEvaluationStatesDTO2.setId(2L);
        assertThat(pmEvaluationStatesDTO1).isNotEqualTo(pmEvaluationStatesDTO2);
        pmEvaluationStatesDTO1.setId(null);
        assertThat(pmEvaluationStatesDTO1).isNotEqualTo(pmEvaluationStatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmEvaluationStatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmEvaluationStatesMapper.fromId(null)).isNull();
    }
}

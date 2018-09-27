package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestComplStates;
import com.infostudio.ba.repository.PmQuestComplStatesRepository;
import com.infostudio.ba.service.dto.PmQuestComplStatesDTO;
import com.infostudio.ba.service.mapper.PmQuestComplStatesMapper;
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
 * Test class for the PmQuestComplStatesResource REST controller.
 *
 * @see PmQuestComplStatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestComplStatesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmQuestComplStatesRepository pmQuestComplStatesRepository;

    @Autowired
    private PmQuestComplStatesMapper pmQuestComplStatesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestComplStatesMockMvc;

    private PmQuestComplStates pmQuestComplStates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestComplStatesResource pmQuestComplStatesResource = new PmQuestComplStatesResource(pmQuestComplStatesRepository, pmQuestComplStatesMapper);
        this.restPmQuestComplStatesMockMvc = MockMvcBuilders.standaloneSetup(pmQuestComplStatesResource)
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
    public static PmQuestComplStates createEntity(EntityManager em) {
        PmQuestComplStates pmQuestComplStates = new PmQuestComplStates()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmQuestComplStates;
    }

    @Before
    public void initTest() {
        pmQuestComplStates = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestComplStates() throws Exception {
        int databaseSizeBeforeCreate = pmQuestComplStatesRepository.findAll().size();

        // Create the PmQuestComplStates
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);
        restPmQuestComplStatesMockMvc.perform(post("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestComplStates in the database
        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestComplStates testPmQuestComplStates = pmQuestComplStatesList.get(pmQuestComplStatesList.size() - 1);
        assertThat(testPmQuestComplStates.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmQuestComplStates.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmQuestComplStates.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmQuestComplStatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestComplStatesRepository.findAll().size();

        // Create the PmQuestComplStates with an existing ID
        pmQuestComplStates.setId(1L);
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestComplStatesMockMvc.perform(post("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestComplStates in the database
        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplStatesRepository.findAll().size();
        // set the field null
        pmQuestComplStates.setCode(null);

        // Create the PmQuestComplStates, which fails.
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);

        restPmQuestComplStatesMockMvc.perform(post("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplStatesRepository.findAll().size();
        // set the field null
        pmQuestComplStates.setName(null);

        // Create the PmQuestComplStates, which fails.
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);

        restPmQuestComplStatesMockMvc.perform(post("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestComplStates() throws Exception {
        // Initialize the database
        pmQuestComplStatesRepository.saveAndFlush(pmQuestComplStates);

        // Get all the pmQuestComplStatesList
        restPmQuestComplStatesMockMvc.perform(get("/api/pm-quest-compl-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestComplStates.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmQuestComplStates() throws Exception {
        // Initialize the database
        pmQuestComplStatesRepository.saveAndFlush(pmQuestComplStates);

        // Get the pmQuestComplStates
        restPmQuestComplStatesMockMvc.perform(get("/api/pm-quest-compl-states/{id}", pmQuestComplStates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestComplStates.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestComplStates() throws Exception {
        // Get the pmQuestComplStates
        restPmQuestComplStatesMockMvc.perform(get("/api/pm-quest-compl-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestComplStates() throws Exception {
        // Initialize the database
        pmQuestComplStatesRepository.saveAndFlush(pmQuestComplStates);
        int databaseSizeBeforeUpdate = pmQuestComplStatesRepository.findAll().size();

        // Update the pmQuestComplStates
        PmQuestComplStates updatedPmQuestComplStates = pmQuestComplStatesRepository.findOne(pmQuestComplStates.getId());
        // Disconnect from session so that the updates on updatedPmQuestComplStates are not directly saved in db
        em.detach(updatedPmQuestComplStates);
        updatedPmQuestComplStates
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(updatedPmQuestComplStates);

        restPmQuestComplStatesMockMvc.perform(put("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestComplStates in the database
        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeUpdate);
        PmQuestComplStates testPmQuestComplStates = pmQuestComplStatesList.get(pmQuestComplStatesList.size() - 1);
        assertThat(testPmQuestComplStates.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmQuestComplStates.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmQuestComplStates.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestComplStates() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestComplStatesRepository.findAll().size();

        // Create the PmQuestComplStates
        PmQuestComplStatesDTO pmQuestComplStatesDTO = pmQuestComplStatesMapper.toDto(pmQuestComplStates);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestComplStatesMockMvc.perform(put("/api/pm-quest-compl-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplStatesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestComplStates in the database
        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestComplStates() throws Exception {
        // Initialize the database
        pmQuestComplStatesRepository.saveAndFlush(pmQuestComplStates);
        int databaseSizeBeforeDelete = pmQuestComplStatesRepository.findAll().size();

        // Get the pmQuestComplStates
        restPmQuestComplStatesMockMvc.perform(delete("/api/pm-quest-compl-states/{id}", pmQuestComplStates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestComplStates> pmQuestComplStatesList = pmQuestComplStatesRepository.findAll();
        assertThat(pmQuestComplStatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestComplStates.class);
        PmQuestComplStates pmQuestComplStates1 = new PmQuestComplStates();
        pmQuestComplStates1.setId(1L);
        PmQuestComplStates pmQuestComplStates2 = new PmQuestComplStates();
        pmQuestComplStates2.setId(pmQuestComplStates1.getId());
        assertThat(pmQuestComplStates1).isEqualTo(pmQuestComplStates2);
        pmQuestComplStates2.setId(2L);
        assertThat(pmQuestComplStates1).isNotEqualTo(pmQuestComplStates2);
        pmQuestComplStates1.setId(null);
        assertThat(pmQuestComplStates1).isNotEqualTo(pmQuestComplStates2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestComplStatesDTO.class);
        PmQuestComplStatesDTO pmQuestComplStatesDTO1 = new PmQuestComplStatesDTO();
        pmQuestComplStatesDTO1.setId(1L);
        PmQuestComplStatesDTO pmQuestComplStatesDTO2 = new PmQuestComplStatesDTO();
        assertThat(pmQuestComplStatesDTO1).isNotEqualTo(pmQuestComplStatesDTO2);
        pmQuestComplStatesDTO2.setId(pmQuestComplStatesDTO1.getId());
        assertThat(pmQuestComplStatesDTO1).isEqualTo(pmQuestComplStatesDTO2);
        pmQuestComplStatesDTO2.setId(2L);
        assertThat(pmQuestComplStatesDTO1).isNotEqualTo(pmQuestComplStatesDTO2);
        pmQuestComplStatesDTO1.setId(null);
        assertThat(pmQuestComplStatesDTO1).isNotEqualTo(pmQuestComplStatesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestComplStatesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestComplStatesMapper.fromId(null)).isNull();
    }
}

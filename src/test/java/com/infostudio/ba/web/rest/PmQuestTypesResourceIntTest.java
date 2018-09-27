package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestTypes;
import com.infostudio.ba.repository.PmQuestTypesRepository;
import com.infostudio.ba.service.dto.PmQuestTypesDTO;
import com.infostudio.ba.service.mapper.PmQuestTypesMapper;
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
 * Test class for the PmQuestTypesResource REST controller.
 *
 * @see PmQuestTypesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestTypesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmQuestTypesRepository pmQuestTypesRepository;

    @Autowired
    private PmQuestTypesMapper pmQuestTypesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestTypesMockMvc;

    private PmQuestTypes pmQuestTypes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestTypesResource pmQuestTypesResource = new PmQuestTypesResource(pmQuestTypesRepository, pmQuestTypesMapper);
        this.restPmQuestTypesMockMvc = MockMvcBuilders.standaloneSetup(pmQuestTypesResource)
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
    public static PmQuestTypes createEntity(EntityManager em) {
        PmQuestTypes pmQuestTypes = new PmQuestTypes()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmQuestTypes;
    }

    @Before
    public void initTest() {
        pmQuestTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestTypes() throws Exception {
        int databaseSizeBeforeCreate = pmQuestTypesRepository.findAll().size();

        // Create the PmQuestTypes
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);
        restPmQuestTypesMockMvc.perform(post("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestTypes in the database
        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestTypes testPmQuestTypes = pmQuestTypesList.get(pmQuestTypesList.size() - 1);
        assertThat(testPmQuestTypes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmQuestTypes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmQuestTypes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmQuestTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestTypesRepository.findAll().size();

        // Create the PmQuestTypes with an existing ID
        pmQuestTypes.setId(1L);
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestTypesMockMvc.perform(post("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestTypes in the database
        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestTypesRepository.findAll().size();
        // set the field null
        pmQuestTypes.setCode(null);

        // Create the PmQuestTypes, which fails.
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);

        restPmQuestTypesMockMvc.perform(post("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestTypesRepository.findAll().size();
        // set the field null
        pmQuestTypes.setName(null);

        // Create the PmQuestTypes, which fails.
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);

        restPmQuestTypesMockMvc.perform(post("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestTypes() throws Exception {
        // Initialize the database
        pmQuestTypesRepository.saveAndFlush(pmQuestTypes);

        // Get all the pmQuestTypesList
        restPmQuestTypesMockMvc.perform(get("/api/pm-quest-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmQuestTypes() throws Exception {
        // Initialize the database
        pmQuestTypesRepository.saveAndFlush(pmQuestTypes);

        // Get the pmQuestTypes
        restPmQuestTypesMockMvc.perform(get("/api/pm-quest-types/{id}", pmQuestTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestTypes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestTypes() throws Exception {
        // Get the pmQuestTypes
        restPmQuestTypesMockMvc.perform(get("/api/pm-quest-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestTypes() throws Exception {
        // Initialize the database
        pmQuestTypesRepository.saveAndFlush(pmQuestTypes);
        int databaseSizeBeforeUpdate = pmQuestTypesRepository.findAll().size();

        // Update the pmQuestTypes
        PmQuestTypes updatedPmQuestTypes = pmQuestTypesRepository.findOne(pmQuestTypes.getId());
        // Disconnect from session so that the updates on updatedPmQuestTypes are not directly saved in db
        em.detach(updatedPmQuestTypes);
        updatedPmQuestTypes
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(updatedPmQuestTypes);

        restPmQuestTypesMockMvc.perform(put("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestTypes in the database
        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeUpdate);
        PmQuestTypes testPmQuestTypes = pmQuestTypesList.get(pmQuestTypesList.size() - 1);
        assertThat(testPmQuestTypes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmQuestTypes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmQuestTypes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestTypes() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestTypesRepository.findAll().size();

        // Create the PmQuestTypes
        PmQuestTypesDTO pmQuestTypesDTO = pmQuestTypesMapper.toDto(pmQuestTypes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestTypesMockMvc.perform(put("/api/pm-quest-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestTypes in the database
        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestTypes() throws Exception {
        // Initialize the database
        pmQuestTypesRepository.saveAndFlush(pmQuestTypes);
        int databaseSizeBeforeDelete = pmQuestTypesRepository.findAll().size();

        // Get the pmQuestTypes
        restPmQuestTypesMockMvc.perform(delete("/api/pm-quest-types/{id}", pmQuestTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestTypes> pmQuestTypesList = pmQuestTypesRepository.findAll();
        assertThat(pmQuestTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestTypes.class);
        PmQuestTypes pmQuestTypes1 = new PmQuestTypes();
        pmQuestTypes1.setId(1L);
        PmQuestTypes pmQuestTypes2 = new PmQuestTypes();
        pmQuestTypes2.setId(pmQuestTypes1.getId());
        assertThat(pmQuestTypes1).isEqualTo(pmQuestTypes2);
        pmQuestTypes2.setId(2L);
        assertThat(pmQuestTypes1).isNotEqualTo(pmQuestTypes2);
        pmQuestTypes1.setId(null);
        assertThat(pmQuestTypes1).isNotEqualTo(pmQuestTypes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestTypesDTO.class);
        PmQuestTypesDTO pmQuestTypesDTO1 = new PmQuestTypesDTO();
        pmQuestTypesDTO1.setId(1L);
        PmQuestTypesDTO pmQuestTypesDTO2 = new PmQuestTypesDTO();
        assertThat(pmQuestTypesDTO1).isNotEqualTo(pmQuestTypesDTO2);
        pmQuestTypesDTO2.setId(pmQuestTypesDTO1.getId());
        assertThat(pmQuestTypesDTO1).isEqualTo(pmQuestTypesDTO2);
        pmQuestTypesDTO2.setId(2L);
        assertThat(pmQuestTypesDTO1).isNotEqualTo(pmQuestTypesDTO2);
        pmQuestTypesDTO1.setId(null);
        assertThat(pmQuestTypesDTO1).isNotEqualTo(pmQuestTypesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestTypesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestTypesMapper.fromId(null)).isNull();
    }
}

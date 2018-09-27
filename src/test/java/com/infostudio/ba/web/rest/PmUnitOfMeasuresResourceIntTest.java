package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmUnitOfMeasures;
import com.infostudio.ba.repository.PmUnitOfMeasuresRepository;
import com.infostudio.ba.service.dto.PmUnitOfMeasuresDTO;
import com.infostudio.ba.service.mapper.PmUnitOfMeasuresMapper;
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
 * Test class for the PmUnitOfMeasuresResource REST controller.
 *
 * @see PmUnitOfMeasuresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmUnitOfMeasuresResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmUnitOfMeasuresRepository pmUnitOfMeasuresRepository;

    @Autowired
    private PmUnitOfMeasuresMapper pmUnitOfMeasuresMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmUnitOfMeasuresMockMvc;

    private PmUnitOfMeasures pmUnitOfMeasures;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmUnitOfMeasuresResource pmUnitOfMeasuresResource = new PmUnitOfMeasuresResource(pmUnitOfMeasuresRepository, pmUnitOfMeasuresMapper);
        this.restPmUnitOfMeasuresMockMvc = MockMvcBuilders.standaloneSetup(pmUnitOfMeasuresResource)
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
    public static PmUnitOfMeasures createEntity(EntityManager em) {
        PmUnitOfMeasures pmUnitOfMeasures = new PmUnitOfMeasures()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmUnitOfMeasures;
    }

    @Before
    public void initTest() {
        pmUnitOfMeasures = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmUnitOfMeasures() throws Exception {
        int databaseSizeBeforeCreate = pmUnitOfMeasuresRepository.findAll().size();

        // Create the PmUnitOfMeasures
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);
        restPmUnitOfMeasuresMockMvc.perform(post("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isCreated());

        // Validate the PmUnitOfMeasures in the database
        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeCreate + 1);
        PmUnitOfMeasures testPmUnitOfMeasures = pmUnitOfMeasuresList.get(pmUnitOfMeasuresList.size() - 1);
        assertThat(testPmUnitOfMeasures.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmUnitOfMeasures.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmUnitOfMeasures.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmUnitOfMeasuresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmUnitOfMeasuresRepository.findAll().size();

        // Create the PmUnitOfMeasures with an existing ID
        pmUnitOfMeasures.setId(1L);
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmUnitOfMeasuresMockMvc.perform(post("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmUnitOfMeasures in the database
        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmUnitOfMeasuresRepository.findAll().size();
        // set the field null
        pmUnitOfMeasures.setCode(null);

        // Create the PmUnitOfMeasures, which fails.
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);

        restPmUnitOfMeasuresMockMvc.perform(post("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmUnitOfMeasuresRepository.findAll().size();
        // set the field null
        pmUnitOfMeasures.setName(null);

        // Create the PmUnitOfMeasures, which fails.
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);

        restPmUnitOfMeasuresMockMvc.perform(post("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isBadRequest());

        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmUnitOfMeasures() throws Exception {
        // Initialize the database
        pmUnitOfMeasuresRepository.saveAndFlush(pmUnitOfMeasures);

        // Get all the pmUnitOfMeasuresList
        restPmUnitOfMeasuresMockMvc.perform(get("/api/pm-unit-of-measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmUnitOfMeasures.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmUnitOfMeasures() throws Exception {
        // Initialize the database
        pmUnitOfMeasuresRepository.saveAndFlush(pmUnitOfMeasures);

        // Get the pmUnitOfMeasures
        restPmUnitOfMeasuresMockMvc.perform(get("/api/pm-unit-of-measures/{id}", pmUnitOfMeasures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmUnitOfMeasures.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmUnitOfMeasures() throws Exception {
        // Get the pmUnitOfMeasures
        restPmUnitOfMeasuresMockMvc.perform(get("/api/pm-unit-of-measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmUnitOfMeasures() throws Exception {
        // Initialize the database
        pmUnitOfMeasuresRepository.saveAndFlush(pmUnitOfMeasures);
        int databaseSizeBeforeUpdate = pmUnitOfMeasuresRepository.findAll().size();

        // Update the pmUnitOfMeasures
        PmUnitOfMeasures updatedPmUnitOfMeasures = pmUnitOfMeasuresRepository.findOne(pmUnitOfMeasures.getId());
        // Disconnect from session so that the updates on updatedPmUnitOfMeasures are not directly saved in db
        em.detach(updatedPmUnitOfMeasures);
        updatedPmUnitOfMeasures
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(updatedPmUnitOfMeasures);

        restPmUnitOfMeasuresMockMvc.perform(put("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isOk());

        // Validate the PmUnitOfMeasures in the database
        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeUpdate);
        PmUnitOfMeasures testPmUnitOfMeasures = pmUnitOfMeasuresList.get(pmUnitOfMeasuresList.size() - 1);
        assertThat(testPmUnitOfMeasures.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmUnitOfMeasures.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmUnitOfMeasures.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmUnitOfMeasures() throws Exception {
        int databaseSizeBeforeUpdate = pmUnitOfMeasuresRepository.findAll().size();

        // Create the PmUnitOfMeasures
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO = pmUnitOfMeasuresMapper.toDto(pmUnitOfMeasures);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmUnitOfMeasuresMockMvc.perform(put("/api/pm-unit-of-measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmUnitOfMeasuresDTO)))
            .andExpect(status().isCreated());

        // Validate the PmUnitOfMeasures in the database
        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmUnitOfMeasures() throws Exception {
        // Initialize the database
        pmUnitOfMeasuresRepository.saveAndFlush(pmUnitOfMeasures);
        int databaseSizeBeforeDelete = pmUnitOfMeasuresRepository.findAll().size();

        // Get the pmUnitOfMeasures
        restPmUnitOfMeasuresMockMvc.perform(delete("/api/pm-unit-of-measures/{id}", pmUnitOfMeasures.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmUnitOfMeasures> pmUnitOfMeasuresList = pmUnitOfMeasuresRepository.findAll();
        assertThat(pmUnitOfMeasuresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmUnitOfMeasures.class);
        PmUnitOfMeasures pmUnitOfMeasures1 = new PmUnitOfMeasures();
        pmUnitOfMeasures1.setId(1L);
        PmUnitOfMeasures pmUnitOfMeasures2 = new PmUnitOfMeasures();
        pmUnitOfMeasures2.setId(pmUnitOfMeasures1.getId());
        assertThat(pmUnitOfMeasures1).isEqualTo(pmUnitOfMeasures2);
        pmUnitOfMeasures2.setId(2L);
        assertThat(pmUnitOfMeasures1).isNotEqualTo(pmUnitOfMeasures2);
        pmUnitOfMeasures1.setId(null);
        assertThat(pmUnitOfMeasures1).isNotEqualTo(pmUnitOfMeasures2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmUnitOfMeasuresDTO.class);
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO1 = new PmUnitOfMeasuresDTO();
        pmUnitOfMeasuresDTO1.setId(1L);
        PmUnitOfMeasuresDTO pmUnitOfMeasuresDTO2 = new PmUnitOfMeasuresDTO();
        assertThat(pmUnitOfMeasuresDTO1).isNotEqualTo(pmUnitOfMeasuresDTO2);
        pmUnitOfMeasuresDTO2.setId(pmUnitOfMeasuresDTO1.getId());
        assertThat(pmUnitOfMeasuresDTO1).isEqualTo(pmUnitOfMeasuresDTO2);
        pmUnitOfMeasuresDTO2.setId(2L);
        assertThat(pmUnitOfMeasuresDTO1).isNotEqualTo(pmUnitOfMeasuresDTO2);
        pmUnitOfMeasuresDTO1.setId(null);
        assertThat(pmUnitOfMeasuresDTO1).isNotEqualTo(pmUnitOfMeasuresDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmUnitOfMeasuresMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmUnitOfMeasuresMapper.fromId(null)).isNull();
    }
}

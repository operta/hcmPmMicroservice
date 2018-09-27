package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmCorMeasureTypes;
import com.infostudio.ba.repository.PmCorMeasureTypesRepository;
import com.infostudio.ba.service.dto.PmCorMeasureTypesDTO;
import com.infostudio.ba.service.mapper.PmCorMeasureTypesMapper;
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
 * Test class for the PmCorMeasureTypesResource REST controller.
 *
 * @see PmCorMeasureTypesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmCorMeasureTypesResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmCorMeasureTypesRepository pmCorMeasureTypesRepository;

    @Autowired
    private PmCorMeasureTypesMapper pmCorMeasureTypesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmCorMeasureTypesMockMvc;

    private PmCorMeasureTypes pmCorMeasureTypes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmCorMeasureTypesResource pmCorMeasureTypesResource = new PmCorMeasureTypesResource(pmCorMeasureTypesRepository, pmCorMeasureTypesMapper);
        this.restPmCorMeasureTypesMockMvc = MockMvcBuilders.standaloneSetup(pmCorMeasureTypesResource)
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
    public static PmCorMeasureTypes createEntity(EntityManager em) {
        PmCorMeasureTypes pmCorMeasureTypes = new PmCorMeasureTypes()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return pmCorMeasureTypes;
    }

    @Before
    public void initTest() {
        pmCorMeasureTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmCorMeasureTypes() throws Exception {
        int databaseSizeBeforeCreate = pmCorMeasureTypesRepository.findAll().size();

        // Create the PmCorMeasureTypes
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);
        restPmCorMeasureTypesMockMvc.perform(post("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorMeasureTypes in the database
        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeCreate + 1);
        PmCorMeasureTypes testPmCorMeasureTypes = pmCorMeasureTypesList.get(pmCorMeasureTypesList.size() - 1);
        assertThat(testPmCorMeasureTypes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPmCorMeasureTypes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPmCorMeasureTypes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmCorMeasureTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmCorMeasureTypesRepository.findAll().size();

        // Create the PmCorMeasureTypes with an existing ID
        pmCorMeasureTypes.setId(1L);
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmCorMeasureTypesMockMvc.perform(post("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmCorMeasureTypes in the database
        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorMeasureTypesRepository.findAll().size();
        // set the field null
        pmCorMeasureTypes.setCode(null);

        // Create the PmCorMeasureTypes, which fails.
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);

        restPmCorMeasureTypesMockMvc.perform(post("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmCorMeasureTypesRepository.findAll().size();
        // set the field null
        pmCorMeasureTypes.setName(null);

        // Create the PmCorMeasureTypes, which fails.
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);

        restPmCorMeasureTypesMockMvc.perform(post("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isBadRequest());

        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmCorMeasureTypes() throws Exception {
        // Initialize the database
        pmCorMeasureTypesRepository.saveAndFlush(pmCorMeasureTypes);

        // Get all the pmCorMeasureTypesList
        restPmCorMeasureTypesMockMvc.perform(get("/api/pm-cor-measure-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmCorMeasureTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmCorMeasureTypes() throws Exception {
        // Initialize the database
        pmCorMeasureTypesRepository.saveAndFlush(pmCorMeasureTypes);

        // Get the pmCorMeasureTypes
        restPmCorMeasureTypesMockMvc.perform(get("/api/pm-cor-measure-types/{id}", pmCorMeasureTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmCorMeasureTypes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmCorMeasureTypes() throws Exception {
        // Get the pmCorMeasureTypes
        restPmCorMeasureTypesMockMvc.perform(get("/api/pm-cor-measure-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmCorMeasureTypes() throws Exception {
        // Initialize the database
        pmCorMeasureTypesRepository.saveAndFlush(pmCorMeasureTypes);
        int databaseSizeBeforeUpdate = pmCorMeasureTypesRepository.findAll().size();

        // Update the pmCorMeasureTypes
        PmCorMeasureTypes updatedPmCorMeasureTypes = pmCorMeasureTypesRepository.findOne(pmCorMeasureTypes.getId());
        // Disconnect from session so that the updates on updatedPmCorMeasureTypes are not directly saved in db
        em.detach(updatedPmCorMeasureTypes);
        updatedPmCorMeasureTypes
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(updatedPmCorMeasureTypes);

        restPmCorMeasureTypesMockMvc.perform(put("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isOk());

        // Validate the PmCorMeasureTypes in the database
        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeUpdate);
        PmCorMeasureTypes testPmCorMeasureTypes = pmCorMeasureTypesList.get(pmCorMeasureTypesList.size() - 1);
        assertThat(testPmCorMeasureTypes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPmCorMeasureTypes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPmCorMeasureTypes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmCorMeasureTypes() throws Exception {
        int databaseSizeBeforeUpdate = pmCorMeasureTypesRepository.findAll().size();

        // Create the PmCorMeasureTypes
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO = pmCorMeasureTypesMapper.toDto(pmCorMeasureTypes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmCorMeasureTypesMockMvc.perform(put("/api/pm-cor-measure-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmCorMeasureTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmCorMeasureTypes in the database
        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmCorMeasureTypes() throws Exception {
        // Initialize the database
        pmCorMeasureTypesRepository.saveAndFlush(pmCorMeasureTypes);
        int databaseSizeBeforeDelete = pmCorMeasureTypesRepository.findAll().size();

        // Get the pmCorMeasureTypes
        restPmCorMeasureTypesMockMvc.perform(delete("/api/pm-cor-measure-types/{id}", pmCorMeasureTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmCorMeasureTypes> pmCorMeasureTypesList = pmCorMeasureTypesRepository.findAll();
        assertThat(pmCorMeasureTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorMeasureTypes.class);
        PmCorMeasureTypes pmCorMeasureTypes1 = new PmCorMeasureTypes();
        pmCorMeasureTypes1.setId(1L);
        PmCorMeasureTypes pmCorMeasureTypes2 = new PmCorMeasureTypes();
        pmCorMeasureTypes2.setId(pmCorMeasureTypes1.getId());
        assertThat(pmCorMeasureTypes1).isEqualTo(pmCorMeasureTypes2);
        pmCorMeasureTypes2.setId(2L);
        assertThat(pmCorMeasureTypes1).isNotEqualTo(pmCorMeasureTypes2);
        pmCorMeasureTypes1.setId(null);
        assertThat(pmCorMeasureTypes1).isNotEqualTo(pmCorMeasureTypes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmCorMeasureTypesDTO.class);
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO1 = new PmCorMeasureTypesDTO();
        pmCorMeasureTypesDTO1.setId(1L);
        PmCorMeasureTypesDTO pmCorMeasureTypesDTO2 = new PmCorMeasureTypesDTO();
        assertThat(pmCorMeasureTypesDTO1).isNotEqualTo(pmCorMeasureTypesDTO2);
        pmCorMeasureTypesDTO2.setId(pmCorMeasureTypesDTO1.getId());
        assertThat(pmCorMeasureTypesDTO1).isEqualTo(pmCorMeasureTypesDTO2);
        pmCorMeasureTypesDTO2.setId(2L);
        assertThat(pmCorMeasureTypesDTO1).isNotEqualTo(pmCorMeasureTypesDTO2);
        pmCorMeasureTypesDTO1.setId(null);
        assertThat(pmCorMeasureTypesDTO1).isNotEqualTo(pmCorMeasureTypesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmCorMeasureTypesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmCorMeasureTypesMapper.fromId(null)).isNull();
    }
}

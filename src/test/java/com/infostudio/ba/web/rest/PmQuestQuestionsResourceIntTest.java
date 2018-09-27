package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestQuestions;
import com.infostudio.ba.repository.PmQuestQuestionsRepository;
import com.infostudio.ba.service.dto.PmQuestQuestionsDTO;
import com.infostudio.ba.service.mapper.PmQuestQuestionsMapper;
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
 * Test class for the PmQuestQuestionsResource REST controller.
 *
 * @see PmQuestQuestionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestQuestionsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_WEIGHT = 1L;
    private static final Long UPDATED_WEIGHT = 2L;

    private static final Long DEFAULT_ID_DETAIL = 1L;
    private static final Long UPDATED_ID_DETAIL = 2L;

    @Autowired
    private PmQuestQuestionsRepository pmQuestQuestionsRepository;

    @Autowired
    private PmQuestQuestionsMapper pmQuestQuestionsMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestQuestionsMockMvc;

    private PmQuestQuestions pmQuestQuestions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestQuestionsResource pmQuestQuestionsResource = new PmQuestQuestionsResource(pmQuestQuestionsRepository, pmQuestQuestionsMapper);
        this.restPmQuestQuestionsMockMvc = MockMvcBuilders.standaloneSetup(pmQuestQuestionsResource)
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
    public static PmQuestQuestions createEntity(EntityManager em) {
        PmQuestQuestions pmQuestQuestions = new PmQuestQuestions()
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .idDetail(DEFAULT_ID_DETAIL);
        return pmQuestQuestions;
    }

    @Before
    public void initTest() {
        pmQuestQuestions = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestQuestions() throws Exception {
        int databaseSizeBeforeCreate = pmQuestQuestionsRepository.findAll().size();

        // Create the PmQuestQuestions
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);
        restPmQuestQuestionsMockMvc.perform(post("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestQuestions in the database
        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestQuestions testPmQuestQuestions = pmQuestQuestionsList.get(pmQuestQuestionsList.size() - 1);
        assertThat(testPmQuestQuestions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPmQuestQuestions.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPmQuestQuestions.getIdDetail()).isEqualTo(DEFAULT_ID_DETAIL);
    }

    @Test
    @Transactional
    public void createPmQuestQuestionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestQuestionsRepository.findAll().size();

        // Create the PmQuestQuestions with an existing ID
        pmQuestQuestions.setId(1L);
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestQuestionsMockMvc.perform(post("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestQuestions in the database
        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestQuestionsRepository.findAll().size();
        // set the field null
        pmQuestQuestions.setWeight(null);

        // Create the PmQuestQuestions, which fails.
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);

        restPmQuestQuestionsMockMvc.perform(post("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdDetailIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestQuestionsRepository.findAll().size();
        // set the field null
        pmQuestQuestions.setIdDetail(null);

        // Create the PmQuestQuestions, which fails.
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);

        restPmQuestQuestionsMockMvc.perform(post("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestQuestions() throws Exception {
        // Initialize the database
        pmQuestQuestionsRepository.saveAndFlush(pmQuestQuestions);

        // Get all the pmQuestQuestionsList
        restPmQuestQuestionsMockMvc.perform(get("/api/pm-quest-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestQuestions.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].idDetail").value(hasItem(DEFAULT_ID_DETAIL.intValue())));
    }

    @Test
    @Transactional
    public void getPmQuestQuestions() throws Exception {
        // Initialize the database
        pmQuestQuestionsRepository.saveAndFlush(pmQuestQuestions);

        // Get the pmQuestQuestions
        restPmQuestQuestionsMockMvc.perform(get("/api/pm-quest-questions/{id}", pmQuestQuestions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestQuestions.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.intValue()))
            .andExpect(jsonPath("$.idDetail").value(DEFAULT_ID_DETAIL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestQuestions() throws Exception {
        // Get the pmQuestQuestions
        restPmQuestQuestionsMockMvc.perform(get("/api/pm-quest-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestQuestions() throws Exception {
        // Initialize the database
        pmQuestQuestionsRepository.saveAndFlush(pmQuestQuestions);
        int databaseSizeBeforeUpdate = pmQuestQuestionsRepository.findAll().size();

        // Update the pmQuestQuestions
        PmQuestQuestions updatedPmQuestQuestions = pmQuestQuestionsRepository.findOne(pmQuestQuestions.getId());
        // Disconnect from session so that the updates on updatedPmQuestQuestions are not directly saved in db
        em.detach(updatedPmQuestQuestions);
        updatedPmQuestQuestions
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .idDetail(UPDATED_ID_DETAIL);
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(updatedPmQuestQuestions);

        restPmQuestQuestionsMockMvc.perform(put("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestQuestions in the database
        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeUpdate);
        PmQuestQuestions testPmQuestQuestions = pmQuestQuestionsList.get(pmQuestQuestionsList.size() - 1);
        assertThat(testPmQuestQuestions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPmQuestQuestions.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPmQuestQuestions.getIdDetail()).isEqualTo(UPDATED_ID_DETAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestQuestions() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestQuestionsRepository.findAll().size();

        // Create the PmQuestQuestions
        PmQuestQuestionsDTO pmQuestQuestionsDTO = pmQuestQuestionsMapper.toDto(pmQuestQuestions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestQuestionsMockMvc.perform(put("/api/pm-quest-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestQuestionsDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestQuestions in the database
        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestQuestions() throws Exception {
        // Initialize the database
        pmQuestQuestionsRepository.saveAndFlush(pmQuestQuestions);
        int databaseSizeBeforeDelete = pmQuestQuestionsRepository.findAll().size();

        // Get the pmQuestQuestions
        restPmQuestQuestionsMockMvc.perform(delete("/api/pm-quest-questions/{id}", pmQuestQuestions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestQuestions> pmQuestQuestionsList = pmQuestQuestionsRepository.findAll();
        assertThat(pmQuestQuestionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestQuestions.class);
        PmQuestQuestions pmQuestQuestions1 = new PmQuestQuestions();
        pmQuestQuestions1.setId(1L);
        PmQuestQuestions pmQuestQuestions2 = new PmQuestQuestions();
        pmQuestQuestions2.setId(pmQuestQuestions1.getId());
        assertThat(pmQuestQuestions1).isEqualTo(pmQuestQuestions2);
        pmQuestQuestions2.setId(2L);
        assertThat(pmQuestQuestions1).isNotEqualTo(pmQuestQuestions2);
        pmQuestQuestions1.setId(null);
        assertThat(pmQuestQuestions1).isNotEqualTo(pmQuestQuestions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestQuestionsDTO.class);
        PmQuestQuestionsDTO pmQuestQuestionsDTO1 = new PmQuestQuestionsDTO();
        pmQuestQuestionsDTO1.setId(1L);
        PmQuestQuestionsDTO pmQuestQuestionsDTO2 = new PmQuestQuestionsDTO();
        assertThat(pmQuestQuestionsDTO1).isNotEqualTo(pmQuestQuestionsDTO2);
        pmQuestQuestionsDTO2.setId(pmQuestQuestionsDTO1.getId());
        assertThat(pmQuestQuestionsDTO1).isEqualTo(pmQuestQuestionsDTO2);
        pmQuestQuestionsDTO2.setId(2L);
        assertThat(pmQuestQuestionsDTO1).isNotEqualTo(pmQuestQuestionsDTO2);
        pmQuestQuestionsDTO1.setId(null);
        assertThat(pmQuestQuestionsDTO1).isNotEqualTo(pmQuestQuestionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestQuestionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestQuestionsMapper.fromId(null)).isNull();
    }
}

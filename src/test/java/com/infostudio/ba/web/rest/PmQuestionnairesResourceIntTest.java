package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestionnaires;
import com.infostudio.ba.repository.PmQuestionnairesRepository;
import com.infostudio.ba.service.dto.PmQuestionnairesDTO;
import com.infostudio.ba.service.mapper.PmQuestionnairesMapper;
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
 * Test class for the PmQuestionnairesResource REST controller.
 *
 * @see PmQuestionnairesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestionnairesResourceIntTest {

    private static final Long DEFAULT_ID_HEADER = 1L;
    private static final Long UPDATED_ID_HEADER = 2L;

    @Autowired
    private PmQuestionnairesRepository pmQuestionnairesRepository;

    @Autowired
    private PmQuestionnairesMapper pmQuestionnairesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestionnairesMockMvc;

    private PmQuestionnaires pmQuestionnaires;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestionnairesResource pmQuestionnairesResource = new PmQuestionnairesResource(pmQuestionnairesRepository, pmQuestionnairesMapper);
        this.restPmQuestionnairesMockMvc = MockMvcBuilders.standaloneSetup(pmQuestionnairesResource)
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
    public static PmQuestionnaires createEntity(EntityManager em) {
        PmQuestionnaires pmQuestionnaires = new PmQuestionnaires()
            .idHeader(DEFAULT_ID_HEADER);
        return pmQuestionnaires;
    }

    @Before
    public void initTest() {
        pmQuestionnaires = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestionnaires() throws Exception {
        int databaseSizeBeforeCreate = pmQuestionnairesRepository.findAll().size();

        // Create the PmQuestionnaires
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);
        restPmQuestionnairesMockMvc.perform(post("/api/pm-questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestionnairesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestionnaires in the database
        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestionnaires testPmQuestionnaires = pmQuestionnairesList.get(pmQuestionnairesList.size() - 1);
        assertThat(testPmQuestionnaires.getIdHeader()).isEqualTo(DEFAULT_ID_HEADER);
    }

    @Test
    @Transactional
    public void createPmQuestionnairesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestionnairesRepository.findAll().size();

        // Create the PmQuestionnaires with an existing ID
        pmQuestionnaires.setId(1L);
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestionnairesMockMvc.perform(post("/api/pm-questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestionnairesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestionnaires in the database
        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIdHeaderIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestionnairesRepository.findAll().size();
        // set the field null
        pmQuestionnaires.setIdHeader(null);

        // Create the PmQuestionnaires, which fails.
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);

        restPmQuestionnairesMockMvc.perform(post("/api/pm-questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestionnairesDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestionnaires() throws Exception {
        // Initialize the database
        pmQuestionnairesRepository.saveAndFlush(pmQuestionnaires);

        // Get all the pmQuestionnairesList
        restPmQuestionnairesMockMvc.perform(get("/api/pm-questionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestionnaires.getId().intValue())))
            .andExpect(jsonPath("$.[*].idHeader").value(hasItem(DEFAULT_ID_HEADER.intValue())));
    }

    @Test
    @Transactional
    public void getPmQuestionnaires() throws Exception {
        // Initialize the database
        pmQuestionnairesRepository.saveAndFlush(pmQuestionnaires);

        // Get the pmQuestionnaires
        restPmQuestionnairesMockMvc.perform(get("/api/pm-questionnaires/{id}", pmQuestionnaires.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestionnaires.getId().intValue()))
            .andExpect(jsonPath("$.idHeader").value(DEFAULT_ID_HEADER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestionnaires() throws Exception {
        // Get the pmQuestionnaires
        restPmQuestionnairesMockMvc.perform(get("/api/pm-questionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestionnaires() throws Exception {
        // Initialize the database
        pmQuestionnairesRepository.saveAndFlush(pmQuestionnaires);
        int databaseSizeBeforeUpdate = pmQuestionnairesRepository.findAll().size();

        // Update the pmQuestionnaires
        PmQuestionnaires updatedPmQuestionnaires = pmQuestionnairesRepository.findOne(pmQuestionnaires.getId());
        // Disconnect from session so that the updates on updatedPmQuestionnaires are not directly saved in db
        em.detach(updatedPmQuestionnaires);
        updatedPmQuestionnaires
            .idHeader(UPDATED_ID_HEADER);
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(updatedPmQuestionnaires);

        restPmQuestionnairesMockMvc.perform(put("/api/pm-questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestionnairesDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestionnaires in the database
        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeUpdate);
        PmQuestionnaires testPmQuestionnaires = pmQuestionnairesList.get(pmQuestionnairesList.size() - 1);
        assertThat(testPmQuestionnaires.getIdHeader()).isEqualTo(UPDATED_ID_HEADER);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestionnaires() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestionnairesRepository.findAll().size();

        // Create the PmQuestionnaires
        PmQuestionnairesDTO pmQuestionnairesDTO = pmQuestionnairesMapper.toDto(pmQuestionnaires);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestionnairesMockMvc.perform(put("/api/pm-questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestionnairesDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestionnaires in the database
        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestionnaires() throws Exception {
        // Initialize the database
        pmQuestionnairesRepository.saveAndFlush(pmQuestionnaires);
        int databaseSizeBeforeDelete = pmQuestionnairesRepository.findAll().size();

        // Get the pmQuestionnaires
        restPmQuestionnairesMockMvc.perform(delete("/api/pm-questionnaires/{id}", pmQuestionnaires.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestionnaires> pmQuestionnairesList = pmQuestionnairesRepository.findAll();
        assertThat(pmQuestionnairesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestionnaires.class);
        PmQuestionnaires pmQuestionnaires1 = new PmQuestionnaires();
        pmQuestionnaires1.setId(1L);
        PmQuestionnaires pmQuestionnaires2 = new PmQuestionnaires();
        pmQuestionnaires2.setId(pmQuestionnaires1.getId());
        assertThat(pmQuestionnaires1).isEqualTo(pmQuestionnaires2);
        pmQuestionnaires2.setId(2L);
        assertThat(pmQuestionnaires1).isNotEqualTo(pmQuestionnaires2);
        pmQuestionnaires1.setId(null);
        assertThat(pmQuestionnaires1).isNotEqualTo(pmQuestionnaires2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestionnairesDTO.class);
        PmQuestionnairesDTO pmQuestionnairesDTO1 = new PmQuestionnairesDTO();
        pmQuestionnairesDTO1.setId(1L);
        PmQuestionnairesDTO pmQuestionnairesDTO2 = new PmQuestionnairesDTO();
        assertThat(pmQuestionnairesDTO1).isNotEqualTo(pmQuestionnairesDTO2);
        pmQuestionnairesDTO2.setId(pmQuestionnairesDTO1.getId());
        assertThat(pmQuestionnairesDTO1).isEqualTo(pmQuestionnairesDTO2);
        pmQuestionnairesDTO2.setId(2L);
        assertThat(pmQuestionnairesDTO1).isNotEqualTo(pmQuestionnairesDTO2);
        pmQuestionnairesDTO1.setId(null);
        assertThat(pmQuestionnairesDTO1).isNotEqualTo(pmQuestionnairesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestionnairesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestionnairesMapper.fromId(null)).isNull();
    }
}

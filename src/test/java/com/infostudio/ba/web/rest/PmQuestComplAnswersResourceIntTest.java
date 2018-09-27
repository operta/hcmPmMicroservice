package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmQuestComplAnswers;
import com.infostudio.ba.repository.PmQuestComplAnswersRepository;
import com.infostudio.ba.service.dto.PmQuestComplAnswersDTO;
import com.infostudio.ba.service.mapper.PmQuestComplAnswersMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.infostudio.ba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PmQuestComplAnswersResource REST controller.
 *
 * @see PmQuestComplAnswersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmQuestComplAnswersResourceIntTest {

    private static final String DEFAULT_DTL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DTL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DTL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DTL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DTL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DTL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ANSWERED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ANSWERED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DTL_ID = "AAAAAAAAAA";
    private static final String UPDATED_DTL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DTL_ID_HEADER = "AAAAAAAAAA";
    private static final String UPDATED_DTL_ID_HEADER = "BBBBBBBBBB";

    private static final String DEFAULT_DTL_MANDATORY = "AAAAAAAAAA";
    private static final String UPDATED_DTL_MANDATORY = "BBBBBBBBBB";

    private static final String DEFAULT_DTL_ID_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DTL_ID_DATA_TYPE = "BBBBBBBBBB";

    @Autowired
    private PmQuestComplAnswersRepository pmQuestComplAnswersRepository;

    @Autowired
    private PmQuestComplAnswersMapper pmQuestComplAnswersMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmQuestComplAnswersMockMvc;

    private PmQuestComplAnswers pmQuestComplAnswers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmQuestComplAnswersResource pmQuestComplAnswersResource = new PmQuestComplAnswersResource(pmQuestComplAnswersRepository, pmQuestComplAnswersMapper);
        this.restPmQuestComplAnswersMockMvc = MockMvcBuilders.standaloneSetup(pmQuestComplAnswersResource)
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
    public static PmQuestComplAnswers createEntity(EntityManager em) {
        PmQuestComplAnswers pmQuestComplAnswers = new PmQuestComplAnswers()
            .dtlCode(DEFAULT_DTL_CODE)
            .dtlName(DEFAULT_DTL_NAME)
            .dtlDescription(DEFAULT_DTL_DESCRIPTION)
            .description(DEFAULT_DESCRIPTION)
            .answer(DEFAULT_ANSWER)
            .dateAnswered(DEFAULT_DATE_ANSWERED)
            .dtlId(DEFAULT_DTL_ID)
            .dtlIdHeader(DEFAULT_DTL_ID_HEADER)
            .dtlMandatory(DEFAULT_DTL_MANDATORY)
            .dtlIdDataType(DEFAULT_DTL_ID_DATA_TYPE);
        return pmQuestComplAnswers;
    }

    @Before
    public void initTest() {
        pmQuestComplAnswers = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmQuestComplAnswers() throws Exception {
        int databaseSizeBeforeCreate = pmQuestComplAnswersRepository.findAll().size();

        // Create the PmQuestComplAnswers
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);
        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestComplAnswers in the database
        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeCreate + 1);
        PmQuestComplAnswers testPmQuestComplAnswers = pmQuestComplAnswersList.get(pmQuestComplAnswersList.size() - 1);
        assertThat(testPmQuestComplAnswers.getDtlCode()).isEqualTo(DEFAULT_DTL_CODE);
        assertThat(testPmQuestComplAnswers.getDtlName()).isEqualTo(DEFAULT_DTL_NAME);
        assertThat(testPmQuestComplAnswers.getDtlDescription()).isEqualTo(DEFAULT_DTL_DESCRIPTION);
        assertThat(testPmQuestComplAnswers.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPmQuestComplAnswers.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testPmQuestComplAnswers.getDateAnswered()).isEqualTo(DEFAULT_DATE_ANSWERED);
        assertThat(testPmQuestComplAnswers.getDtlId()).isEqualTo(DEFAULT_DTL_ID);
        assertThat(testPmQuestComplAnswers.getDtlIdHeader()).isEqualTo(DEFAULT_DTL_ID_HEADER);
        assertThat(testPmQuestComplAnswers.getDtlMandatory()).isEqualTo(DEFAULT_DTL_MANDATORY);
        assertThat(testPmQuestComplAnswers.getDtlIdDataType()).isEqualTo(DEFAULT_DTL_ID_DATA_TYPE);
    }

    @Test
    @Transactional
    public void createPmQuestComplAnswersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmQuestComplAnswersRepository.findAll().size();

        // Create the PmQuestComplAnswers with an existing ID
        pmQuestComplAnswers.setId(1L);
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmQuestComplAnswers in the database
        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplAnswersRepository.findAll().size();
        // set the field null
        pmQuestComplAnswers.setAnswer(null);

        // Create the PmQuestComplAnswers, which fails.
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAnsweredIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplAnswersRepository.findAll().size();
        // set the field null
        pmQuestComplAnswers.setDateAnswered(null);

        // Create the PmQuestComplAnswers, which fails.
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDtlIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplAnswersRepository.findAll().size();
        // set the field null
        pmQuestComplAnswers.setDtlId(null);

        // Create the PmQuestComplAnswers, which fails.
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDtlIdHeaderIsRequired() throws Exception {
        int databaseSizeBeforeTest = pmQuestComplAnswersRepository.findAll().size();
        // set the field null
        pmQuestComplAnswers.setDtlIdHeader(null);

        // Create the PmQuestComplAnswers, which fails.
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        restPmQuestComplAnswersMockMvc.perform(post("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isBadRequest());

        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPmQuestComplAnswers() throws Exception {
        // Initialize the database
        pmQuestComplAnswersRepository.saveAndFlush(pmQuestComplAnswers);

        // Get all the pmQuestComplAnswersList
        restPmQuestComplAnswersMockMvc.perform(get("/api/pm-quest-compl-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmQuestComplAnswers.getId().intValue())))
            .andExpect(jsonPath("$.[*].dtlCode").value(hasItem(DEFAULT_DTL_CODE.toString())))
            .andExpect(jsonPath("$.[*].dtlName").value(hasItem(DEFAULT_DTL_NAME.toString())))
            .andExpect(jsonPath("$.[*].dtlDescription").value(hasItem(DEFAULT_DTL_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].dateAnswered").value(hasItem(DEFAULT_DATE_ANSWERED.toString())))
            .andExpect(jsonPath("$.[*].dtlId").value(hasItem(DEFAULT_DTL_ID.toString())))
            .andExpect(jsonPath("$.[*].dtlIdHeader").value(hasItem(DEFAULT_DTL_ID_HEADER.toString())))
            .andExpect(jsonPath("$.[*].dtlMandatory").value(hasItem(DEFAULT_DTL_MANDATORY.toString())))
            .andExpect(jsonPath("$.[*].dtlIdDataType").value(hasItem(DEFAULT_DTL_ID_DATA_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPmQuestComplAnswers() throws Exception {
        // Initialize the database
        pmQuestComplAnswersRepository.saveAndFlush(pmQuestComplAnswers);

        // Get the pmQuestComplAnswers
        restPmQuestComplAnswersMockMvc.perform(get("/api/pm-quest-compl-answers/{id}", pmQuestComplAnswers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmQuestComplAnswers.getId().intValue()))
            .andExpect(jsonPath("$.dtlCode").value(DEFAULT_DTL_CODE.toString()))
            .andExpect(jsonPath("$.dtlName").value(DEFAULT_DTL_NAME.toString()))
            .andExpect(jsonPath("$.dtlDescription").value(DEFAULT_DTL_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.dateAnswered").value(DEFAULT_DATE_ANSWERED.toString()))
            .andExpect(jsonPath("$.dtlId").value(DEFAULT_DTL_ID.toString()))
            .andExpect(jsonPath("$.dtlIdHeader").value(DEFAULT_DTL_ID_HEADER.toString()))
            .andExpect(jsonPath("$.dtlMandatory").value(DEFAULT_DTL_MANDATORY.toString()))
            .andExpect(jsonPath("$.dtlIdDataType").value(DEFAULT_DTL_ID_DATA_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmQuestComplAnswers() throws Exception {
        // Get the pmQuestComplAnswers
        restPmQuestComplAnswersMockMvc.perform(get("/api/pm-quest-compl-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmQuestComplAnswers() throws Exception {
        // Initialize the database
        pmQuestComplAnswersRepository.saveAndFlush(pmQuestComplAnswers);
        int databaseSizeBeforeUpdate = pmQuestComplAnswersRepository.findAll().size();

        // Update the pmQuestComplAnswers
        PmQuestComplAnswers updatedPmQuestComplAnswers = pmQuestComplAnswersRepository.findOne(pmQuestComplAnswers.getId());
        // Disconnect from session so that the updates on updatedPmQuestComplAnswers are not directly saved in db
        em.detach(updatedPmQuestComplAnswers);
        updatedPmQuestComplAnswers
            .dtlCode(UPDATED_DTL_CODE)
            .dtlName(UPDATED_DTL_NAME)
            .dtlDescription(UPDATED_DTL_DESCRIPTION)
            .description(UPDATED_DESCRIPTION)
            .answer(UPDATED_ANSWER)
            .dateAnswered(UPDATED_DATE_ANSWERED)
            .dtlId(UPDATED_DTL_ID)
            .dtlIdHeader(UPDATED_DTL_ID_HEADER)
            .dtlMandatory(UPDATED_DTL_MANDATORY)
            .dtlIdDataType(UPDATED_DTL_ID_DATA_TYPE);
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(updatedPmQuestComplAnswers);

        restPmQuestComplAnswersMockMvc.perform(put("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isOk());

        // Validate the PmQuestComplAnswers in the database
        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeUpdate);
        PmQuestComplAnswers testPmQuestComplAnswers = pmQuestComplAnswersList.get(pmQuestComplAnswersList.size() - 1);
        assertThat(testPmQuestComplAnswers.getDtlCode()).isEqualTo(UPDATED_DTL_CODE);
        assertThat(testPmQuestComplAnswers.getDtlName()).isEqualTo(UPDATED_DTL_NAME);
        assertThat(testPmQuestComplAnswers.getDtlDescription()).isEqualTo(UPDATED_DTL_DESCRIPTION);
        assertThat(testPmQuestComplAnswers.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPmQuestComplAnswers.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testPmQuestComplAnswers.getDateAnswered()).isEqualTo(UPDATED_DATE_ANSWERED);
        assertThat(testPmQuestComplAnswers.getDtlId()).isEqualTo(UPDATED_DTL_ID);
        assertThat(testPmQuestComplAnswers.getDtlIdHeader()).isEqualTo(UPDATED_DTL_ID_HEADER);
        assertThat(testPmQuestComplAnswers.getDtlMandatory()).isEqualTo(UPDATED_DTL_MANDATORY);
        assertThat(testPmQuestComplAnswers.getDtlIdDataType()).isEqualTo(UPDATED_DTL_ID_DATA_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPmQuestComplAnswers() throws Exception {
        int databaseSizeBeforeUpdate = pmQuestComplAnswersRepository.findAll().size();

        // Create the PmQuestComplAnswers
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO = pmQuestComplAnswersMapper.toDto(pmQuestComplAnswers);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmQuestComplAnswersMockMvc.perform(put("/api/pm-quest-compl-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmQuestComplAnswersDTO)))
            .andExpect(status().isCreated());

        // Validate the PmQuestComplAnswers in the database
        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmQuestComplAnswers() throws Exception {
        // Initialize the database
        pmQuestComplAnswersRepository.saveAndFlush(pmQuestComplAnswers);
        int databaseSizeBeforeDelete = pmQuestComplAnswersRepository.findAll().size();

        // Get the pmQuestComplAnswers
        restPmQuestComplAnswersMockMvc.perform(delete("/api/pm-quest-compl-answers/{id}", pmQuestComplAnswers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmQuestComplAnswers> pmQuestComplAnswersList = pmQuestComplAnswersRepository.findAll();
        assertThat(pmQuestComplAnswersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestComplAnswers.class);
        PmQuestComplAnswers pmQuestComplAnswers1 = new PmQuestComplAnswers();
        pmQuestComplAnswers1.setId(1L);
        PmQuestComplAnswers pmQuestComplAnswers2 = new PmQuestComplAnswers();
        pmQuestComplAnswers2.setId(pmQuestComplAnswers1.getId());
        assertThat(pmQuestComplAnswers1).isEqualTo(pmQuestComplAnswers2);
        pmQuestComplAnswers2.setId(2L);
        assertThat(pmQuestComplAnswers1).isNotEqualTo(pmQuestComplAnswers2);
        pmQuestComplAnswers1.setId(null);
        assertThat(pmQuestComplAnswers1).isNotEqualTo(pmQuestComplAnswers2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmQuestComplAnswersDTO.class);
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO1 = new PmQuestComplAnswersDTO();
        pmQuestComplAnswersDTO1.setId(1L);
        PmQuestComplAnswersDTO pmQuestComplAnswersDTO2 = new PmQuestComplAnswersDTO();
        assertThat(pmQuestComplAnswersDTO1).isNotEqualTo(pmQuestComplAnswersDTO2);
        pmQuestComplAnswersDTO2.setId(pmQuestComplAnswersDTO1.getId());
        assertThat(pmQuestComplAnswersDTO1).isEqualTo(pmQuestComplAnswersDTO2);
        pmQuestComplAnswersDTO2.setId(2L);
        assertThat(pmQuestComplAnswersDTO1).isNotEqualTo(pmQuestComplAnswersDTO2);
        pmQuestComplAnswersDTO1.setId(null);
        assertThat(pmQuestComplAnswersDTO1).isNotEqualTo(pmQuestComplAnswersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmQuestComplAnswersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmQuestComplAnswersMapper.fromId(null)).isNull();
    }
}

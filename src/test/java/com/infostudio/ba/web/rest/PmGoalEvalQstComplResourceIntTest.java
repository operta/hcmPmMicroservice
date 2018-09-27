package com.infostudio.ba.web.rest;

import com.infostudio.ba.HcmPmMicroserviceApp;

import com.infostudio.ba.domain.PmGoalEvalQstCompl;
import com.infostudio.ba.repository.PmGoalEvalQstComplRepository;
import com.infostudio.ba.service.dto.PmGoalEvalQstComplDTO;
import com.infostudio.ba.service.mapper.PmGoalEvalQstComplMapper;
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
 * Test class for the PmGoalEvalQstComplResource REST controller.
 *
 * @see PmGoalEvalQstComplResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmPmMicroserviceApp.class)
public class PmGoalEvalQstComplResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository;

    @Autowired
    private PmGoalEvalQstComplMapper pmGoalEvalQstComplMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPmGoalEvalQstComplMockMvc;

    private PmGoalEvalQstCompl pmGoalEvalQstCompl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PmGoalEvalQstComplResource pmGoalEvalQstComplResource = new PmGoalEvalQstComplResource(pmGoalEvalQstComplRepository, pmGoalEvalQstComplMapper);
        this.restPmGoalEvalQstComplMockMvc = MockMvcBuilders.standaloneSetup(pmGoalEvalQstComplResource)
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
    public static PmGoalEvalQstCompl createEntity(EntityManager em) {
        PmGoalEvalQstCompl pmGoalEvalQstCompl = new PmGoalEvalQstCompl()
            .description(DEFAULT_DESCRIPTION);
        return pmGoalEvalQstCompl;
    }

    @Before
    public void initTest() {
        pmGoalEvalQstCompl = createEntity(em);
    }

    @Test
    @Transactional
    public void createPmGoalEvalQstCompl() throws Exception {
        int databaseSizeBeforeCreate = pmGoalEvalQstComplRepository.findAll().size();

        // Create the PmGoalEvalQstCompl
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);
        restPmGoalEvalQstComplMockMvc.perform(post("/api/pm-goal-eval-qst-compls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalEvalQstComplDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalEvalQstCompl in the database
        List<PmGoalEvalQstCompl> pmGoalEvalQstComplList = pmGoalEvalQstComplRepository.findAll();
        assertThat(pmGoalEvalQstComplList).hasSize(databaseSizeBeforeCreate + 1);
        PmGoalEvalQstCompl testPmGoalEvalQstCompl = pmGoalEvalQstComplList.get(pmGoalEvalQstComplList.size() - 1);
        assertThat(testPmGoalEvalQstCompl.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPmGoalEvalQstComplWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pmGoalEvalQstComplRepository.findAll().size();

        // Create the PmGoalEvalQstCompl with an existing ID
        pmGoalEvalQstCompl.setId(1L);
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPmGoalEvalQstComplMockMvc.perform(post("/api/pm-goal-eval-qst-compls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalEvalQstComplDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PmGoalEvalQstCompl in the database
        List<PmGoalEvalQstCompl> pmGoalEvalQstComplList = pmGoalEvalQstComplRepository.findAll();
        assertThat(pmGoalEvalQstComplList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPmGoalEvalQstCompls() throws Exception {
        // Initialize the database
        pmGoalEvalQstComplRepository.saveAndFlush(pmGoalEvalQstCompl);

        // Get all the pmGoalEvalQstComplList
        restPmGoalEvalQstComplMockMvc.perform(get("/api/pm-goal-eval-qst-compls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pmGoalEvalQstCompl.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPmGoalEvalQstCompl() throws Exception {
        // Initialize the database
        pmGoalEvalQstComplRepository.saveAndFlush(pmGoalEvalQstCompl);

        // Get the pmGoalEvalQstCompl
        restPmGoalEvalQstComplMockMvc.perform(get("/api/pm-goal-eval-qst-compls/{id}", pmGoalEvalQstCompl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pmGoalEvalQstCompl.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPmGoalEvalQstCompl() throws Exception {
        // Get the pmGoalEvalQstCompl
        restPmGoalEvalQstComplMockMvc.perform(get("/api/pm-goal-eval-qst-compls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePmGoalEvalQstCompl() throws Exception {
        // Initialize the database
        pmGoalEvalQstComplRepository.saveAndFlush(pmGoalEvalQstCompl);
        int databaseSizeBeforeUpdate = pmGoalEvalQstComplRepository.findAll().size();

        // Update the pmGoalEvalQstCompl
        PmGoalEvalQstCompl updatedPmGoalEvalQstCompl = pmGoalEvalQstComplRepository.findOne(pmGoalEvalQstCompl.getId());
        // Disconnect from session so that the updates on updatedPmGoalEvalQstCompl are not directly saved in db
        em.detach(updatedPmGoalEvalQstCompl);
        updatedPmGoalEvalQstCompl
            .description(UPDATED_DESCRIPTION);
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = pmGoalEvalQstComplMapper.toDto(updatedPmGoalEvalQstCompl);

        restPmGoalEvalQstComplMockMvc.perform(put("/api/pm-goal-eval-qst-compls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalEvalQstComplDTO)))
            .andExpect(status().isOk());

        // Validate the PmGoalEvalQstCompl in the database
        List<PmGoalEvalQstCompl> pmGoalEvalQstComplList = pmGoalEvalQstComplRepository.findAll();
        assertThat(pmGoalEvalQstComplList).hasSize(databaseSizeBeforeUpdate);
        PmGoalEvalQstCompl testPmGoalEvalQstCompl = pmGoalEvalQstComplList.get(pmGoalEvalQstComplList.size() - 1);
        assertThat(testPmGoalEvalQstCompl.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPmGoalEvalQstCompl() throws Exception {
        int databaseSizeBeforeUpdate = pmGoalEvalQstComplRepository.findAll().size();

        // Create the PmGoalEvalQstCompl
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO = pmGoalEvalQstComplMapper.toDto(pmGoalEvalQstCompl);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPmGoalEvalQstComplMockMvc.perform(put("/api/pm-goal-eval-qst-compls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pmGoalEvalQstComplDTO)))
            .andExpect(status().isCreated());

        // Validate the PmGoalEvalQstCompl in the database
        List<PmGoalEvalQstCompl> pmGoalEvalQstComplList = pmGoalEvalQstComplRepository.findAll();
        assertThat(pmGoalEvalQstComplList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePmGoalEvalQstCompl() throws Exception {
        // Initialize the database
        pmGoalEvalQstComplRepository.saveAndFlush(pmGoalEvalQstCompl);
        int databaseSizeBeforeDelete = pmGoalEvalQstComplRepository.findAll().size();

        // Get the pmGoalEvalQstCompl
        restPmGoalEvalQstComplMockMvc.perform(delete("/api/pm-goal-eval-qst-compls/{id}", pmGoalEvalQstCompl.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PmGoalEvalQstCompl> pmGoalEvalQstComplList = pmGoalEvalQstComplRepository.findAll();
        assertThat(pmGoalEvalQstComplList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalEvalQstCompl.class);
        PmGoalEvalQstCompl pmGoalEvalQstCompl1 = new PmGoalEvalQstCompl();
        pmGoalEvalQstCompl1.setId(1L);
        PmGoalEvalQstCompl pmGoalEvalQstCompl2 = new PmGoalEvalQstCompl();
        pmGoalEvalQstCompl2.setId(pmGoalEvalQstCompl1.getId());
        assertThat(pmGoalEvalQstCompl1).isEqualTo(pmGoalEvalQstCompl2);
        pmGoalEvalQstCompl2.setId(2L);
        assertThat(pmGoalEvalQstCompl1).isNotEqualTo(pmGoalEvalQstCompl2);
        pmGoalEvalQstCompl1.setId(null);
        assertThat(pmGoalEvalQstCompl1).isNotEqualTo(pmGoalEvalQstCompl2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PmGoalEvalQstComplDTO.class);
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO1 = new PmGoalEvalQstComplDTO();
        pmGoalEvalQstComplDTO1.setId(1L);
        PmGoalEvalQstComplDTO pmGoalEvalQstComplDTO2 = new PmGoalEvalQstComplDTO();
        assertThat(pmGoalEvalQstComplDTO1).isNotEqualTo(pmGoalEvalQstComplDTO2);
        pmGoalEvalQstComplDTO2.setId(pmGoalEvalQstComplDTO1.getId());
        assertThat(pmGoalEvalQstComplDTO1).isEqualTo(pmGoalEvalQstComplDTO2);
        pmGoalEvalQstComplDTO2.setId(2L);
        assertThat(pmGoalEvalQstComplDTO1).isNotEqualTo(pmGoalEvalQstComplDTO2);
        pmGoalEvalQstComplDTO1.setId(null);
        assertThat(pmGoalEvalQstComplDTO1).isNotEqualTo(pmGoalEvalQstComplDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pmGoalEvalQstComplMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pmGoalEvalQstComplMapper.fromId(null)).isNull();
    }
}

//package com.infostudio.ba.web.rest;
//
//import com.infostudio.ba.HcmPmMicroserviceApp;
//import com.infostudio.ba.domain.PmGoalEvalQstCompl;
//import com.infostudio.ba.domain.PmGoalsEvaluations;
//import com.infostudio.ba.domain.PmQuestCompletions;
//import com.infostudio.ba.repository.PmGoalEvalQstComplRepository;
//import com.infostudio.ba.repository.PmGoalsEvaluationsRepository;
//import com.infostudio.ba.repository.PmQuestCompletionsRepository;
//import com.infostudio.ba.service.mapper.PmGoalEvalQstComplMapper;
//import com.infostudio.ba.service.mapper.PmQuestCompletionsMapper;
//import com.infostudio.ba.web.rest.errors.ExceptionTranslator;
//import com.netflix.discovery.converters.Auto;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//@SpringBootTest(classes = HcmPmMicroserviceApp.class)
//@RunWith(SpringRunner.class)
//public class PmQuestCompletionsResourceIntTest {
//
//    private MockMvc mvc;
//
//    @Autowired
//    private PmQuestCompletionsRepository pmQuestCompletionsRepository;
//
//    @Autowired
//    private PmQuestCompletionsMapper pmQuestCompletionsMapper;
//
//    @Autowired
//    private PmGoalEvalQstComplRepository pmGoalEvalQstComplRepository;
//
//    @Autowired
//    private PmGoalsEvaluationsRepository pmGoalsEvaluationsRepository;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
//
//    @Before
//    public void setup(){
//        MockitoAnnotations.initMocks(this);
//
//        final PmQuestCompletionsResource pmQuestCompletionsResource = new PmQuestCompletionsResource(pmQuestCompletionsRepository,
//            pmQuestCompletionsMapper,
//            pmGoalEvalQstComplRepository,
//            pmGoalsEvaluationsRepository);
//
//        mvc = MockMvcBuilders.standaloneSetup(pmQuestCompletionsResource)
//            .setControllerAdvice(exceptionTranslator)
//            .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
//            .build();
//
//    }
//
//    // TODO -> write tests
//}

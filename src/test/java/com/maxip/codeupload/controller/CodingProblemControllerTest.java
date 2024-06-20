package com.maxip.codeupload.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maxip.codeupload.persistence.entity.*;
import com.maxip.codeupload.persistence.repository.springdatajpa.*;
import com.maxip.codeupload.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CodingProblemControllerTest
{
    private String  TOKEN;
    private String  USERNAME;
    private User    USER;
    private Long    TEST_ID = 1L;
    private Long    TESt_DELETE_ID = 2L;
    private String  TEST_CREATE = "[CREATED!!!] ";
    private String  TEST_UPDATE = "[UPDATED!!!] ";
    private String  TEST_EXISTING_CATEGORY = "TREES";
    private String  TEST_EXISTING_DIFFICULTY = "MEDIUM";

    private ObjectMapper om;
    private ObjectWriter ow;

    public MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CodingProblemRepository codingProblemRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private Environment env;
    @Autowired
    private SubjectCategoryRepository subjectCategoryRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
    @Autowired
    private AlgorithmRepository algorithmRepository;

    @BeforeEach
    public void setUp()
    {
        USERNAME = env.getProperty("test.admin");
        USER = userRepository.findByUsername(USERNAME);
        TOKEN = jwtService.generateToken(userDetailsService.loadUserByUsername(USERNAME));
        om = new ObjectMapper();
        ow = om.writer();
    }

    @Test
    public void shouldGetAllCodingProblems() throws Exception
    {
        Set<CodingProblem> actualSet = codingProblemRepository.findByUser(USER);
        String jsonExpected = ow.writeValueAsString(actualSet);
        System.out.println("Expected: " + jsonExpected);

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();

        String jsonActual = res.getResponse().getContentAsString();
        System.out.println("Actual: " + jsonActual);

        assertEquals(jsonActual, jsonExpected);
    }

    @Test
    public void shouldCreateCodingProblem() throws Exception
    {
        Difficulty difficulty = new Difficulty();
        difficulty.setName(TEST_CREATE);

        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setName(TEST_CREATE);

        AlgorithmSubStep algorithmSubStep = new AlgorithmSubStep();
        algorithmSubStep.setStepNumber(1L);
        algorithmSubStep.setDescription(TEST_CREATE);

        Algorithm algorithm = new Algorithm();
        algorithm.setName(TEST_CREATE);
        algorithm.setDescription(TEST_CREATE);
        algorithm.setAlgorithmSubSteps(Collections.singletonList(algorithmSubStep));

        CodingProblem problem = new CodingProblem();
        problem.setName(TEST_CREATE);
        problem.setSubjectCategory(Collections.singletonList(subjectCategory));
        problem.setDifficulty(difficulty);
        problem.setAlgorithms(Collections.singletonList(algorithm));


        String problemJson = ow.writeValueAsString(problem);
        System.out.println("Expected: " + problemJson);

        MvcResult res = mockMvc.perform(post("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem")
                        .header("Authorization", "Bearer "+TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(problemJson))
                .andExpect(status().isOk())
                .andReturn();

        String jsonInsert = res.getResponse().getContentAsString();
        long resId = om.readTree(jsonInsert).get("id").asLong();

        res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem/"+resId)
                        .header("Authorization", "Bearer "+TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String resJson = res.getResponse().getContentAsString();
        System.out.println("Actual: " + resJson);

        assertEquals(ignoreIdHelper(problemJson),ignoreIdHelper(resJson));
    }

    @Test
    public void shouldReadCodingProblem() throws Exception
    {
        CodingProblem problem = codingProblemRepository.findById(TEST_ID).orElse(null);

        String expectedJson = ow.writeValueAsString(problem);
        System.out.println("Expected: " + expectedJson);

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem/"+TEST_ID)
                        .header("Authorization", "Bearer "+TOKEN))
                .andExpect(status().isOk())
                .andReturn();

        String resJson = res.getResponse().getContentAsString();
        System.out.println("Actual: " + resJson);

        assertEquals(ignoreIdHelper(expectedJson),ignoreIdHelper(resJson));
    }

    @Test
    public void shouldUpdateCodingProblem() throws Exception
    {
        SubjectCategory category = subjectCategoryRepository.findByName(TEST_EXISTING_CATEGORY);

        SubjectCategory category2 = new SubjectCategory();
        category2.setName(TEST_UPDATE);

        AlgorithmSubStep algorithmSubStep = new AlgorithmSubStep();
        algorithmSubStep.setStepNumber(1L);
        algorithmSubStep.setDescription(TEST_UPDATE);

        Algorithm algorithm = new Algorithm();
        algorithm.setName(TEST_UPDATE);
        algorithm.setDescription(TEST_UPDATE);
        algorithm.setAlgorithmSubSteps(Collections.singletonList(algorithmSubStep));

        CodingProblem problem = codingProblemRepository.findById(TEST_ID).orElse(null);
        for(Algorithm algo : problem.getAlgorithms())
        {
            algo.setName(TEST_UPDATE);
            algo.setDescription(TEST_UPDATE);
            for (AlgorithmSubStep subStep : algo.getAlgorithmSubSteps())
            {
                subStep.setDescription(TEST_UPDATE);
            }
        }

        problem.setName(TEST_UPDATE);
        problem.setDifficulty(difficultyRepository.findByName(TEST_EXISTING_DIFFICULTY)); // Already existing difficulty
        problem.getSubjectCategory().add(category);
        problem.getSubjectCategory().add(category2);
        problem.getAlgorithms().add(algorithm);

        String expectedJson = ow.writeValueAsString(problem);
        System.out.println("Expected: " + expectedJson);

        MvcResult res = mockMvc.perform(put("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem")
                        .header("Authorization", "Bearer "+TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(expectedJson))
                .andExpect(status().isOk())
                .andReturn();
        String updateJson = res.getResponse().getContentAsString();

        res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem/"+TEST_ID)
                        .header("Authorization", "Bearer "+TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String readJson = res.getResponse().getContentAsString();
        System.out.println("Actual: " + readJson);

        assertEquals(ignoreIdHelper(expectedJson),ignoreIdHelper(updateJson));
        assertEquals(ignoreIdHelper(expectedJson),ignoreIdHelper(readJson));
    }

    @Test
    public void shouldDeleteCodingProblem() throws Exception
    {
        mockMvc.perform(delete("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem/"+TESt_DELETE_ID)
                        .header("Authorization", "Bearer "+TOKEN))
                .andExpect(status().isOk());

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/coding-problem/"+TESt_DELETE_ID)
                        .header("Authorization", "Bearer "+TOKEN))
                .andExpect(status().isOk())
                .andReturn();

        String json = res.getResponse().getContentAsString();

        assertEquals("", json);
    }

    public String ignoreIdHelper(String json)
    {
        String replacedNulls = json.replaceAll("\"id\":null,", "\"id\":1,");
        return replacedNulls.replaceAll("\"id\":(\\d+),", "\"id\":1,");
    }
}

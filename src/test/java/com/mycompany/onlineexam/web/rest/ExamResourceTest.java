package com.mycompany.onlineexam.web.rest;

import com.mycompany.onlineexam.domain.Role;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.service.dto.*;
import com.mycompany.onlineexam.service.impl.TestUtil;
import com.mycompany.onlineexam.web.model.TokenModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class ExamResourceTest {

    private String adminToken;
    private String masterToken;

    @Value(value = "${application.security.get-token-request-uri}")
    private String getTokenRequestURI;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        adminToken = getToken("admin", "admin");
        masterToken = getToken("master", "123");
    }

    private String getToken(String username, String password) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(Constants.USERNAME, username);
        parameters.add(Constants.PASSWORD, password);
        TokenModel tokenModel = WebClient.create()
                .post()
                .uri(getTokenRequestURI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(parameters))
                .retrieve()
                .bodyToMono(TokenModel.class)
                .block();
        return tokenModel.getAccessToken();
    }

    @Test
    void createExam() throws Exception {
        ExamDTO exam = generateNewExam();
//        CourseDTO course = generateNewCourse(exam);
        mockMvc.perform(post("/api/master/create-exam").with(csrf())
                        .queryParam("courseCode", "TEST")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(exam))
                        .header("Authorization", "Bearer " + masterToken))
                .andExpect(status().isNotFound());
    }

    private CourseDTO generateNewCourse(ExamDTO exam) {
        CourseDTO course = new CourseDTO();
        course.setCourseTitle("test course - " + LocalDateTime.now());
        course.setCapacity(20);
        course.setExamDTOS(Collections.singletonList(exam));
        course.setMasterDTOS(generateNewMaster());
        return course;
    }

    private MasterDTO generateNewMaster() {
        MasterDTO newMaster = new MasterDTO();
        newMaster.setUsername("test_master");
        newMaster.setPassword("123");
        newMaster.setPhoneNumber("123");
        newMaster.setRoles(Collections.singletonList(new Role("ROLE_MASTER")));
        return newMaster;
    }

    private ExamDTO generateNewExam() {
        ExamDTO exam = new ExamDTO();
        exam.setExamScore(100F);
        exam.setExamTitle("test exam - " + LocalDateTime.now());
        exam.setStartDateTime(LocalDateTime.now());
        exam.setEndDateTime(LocalDateTime.of(2022, 11, 16, 23, 0));
        exam.setQuestionDTOS(getQuestions());
        return exam;
    }

    private Set<QuestionDTO> getQuestions() {
        return new HashSet<QuestionDTO>() {
            {
                add(createQuestion("q1 - test"));
                add(createQuestion("q2 - test"));
                add(createQuestion("q3 - test"));
            }
        };
    }

    private QuestionDTO createQuestion(String questionTitle) {
        QuestionDTO newQuestion = new QuestionDTO();
        newQuestion.setQuestionTitle("q1 - test ");
        newQuestion.setAnswerDTOS(getAnswersForRequiredQuestion(newQuestion.getQuestionTitle()));
        AnswerDTO q1CorrectAnswer = new AnswerDTO();
        q1CorrectAnswer.setContent(newQuestion.getQuestionTitle() + " correct answer");
        newQuestion.setCorrectAnswerDTO(q1CorrectAnswer);
        return newQuestion;
    }

    private Set<AnswerDTO> getAnswersForRequiredQuestion(String questionTitle) {
        AnswerDTO answer1 = new AnswerDTO();
        answer1.setContent("answer 1 of " + questionTitle);
        AnswerDTO answer2 = new AnswerDTO();
        answer2.setContent("answer 2 of " + questionTitle);
        AnswerDTO answer3 = new AnswerDTO();
        answer3.setContent("answer 3 of " + questionTitle);
        return new HashSet<AnswerDTO>() {
            {
                add(answer1);
                add(answer2);
                add(answer3);
            }
        };
    }

    @Test
    void getAllExams() throws Exception {
        mockMvc.perform(get("/api/admin/get-all-exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteExam() {
    }

    @Test
    void deleteQuestion() {
    }

    @Test
    void getExamInfoByExamCode() {
    }

    @Test
    void getExamRemainingTime() {
    }

    @Test
    void getExamQuestions() {
    }

    @Test
    void updateExamTime() {
    }
}
package com.mycompany.onlineexam.service.impl;

import com.mycompany.onlineexam.domain.Answer;
import com.mycompany.onlineexam.domain.Exam;
import com.mycompany.onlineexam.domain.Question;
import com.mycompany.onlineexam.domain.Student;
import com.mycompany.onlineexam.repository.ExamRepository;
import com.mycompany.onlineexam.repository.StudentRepository;
import com.mycompany.onlineexam.web.model.QuestionAndAnswerForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ExamRepository examRepository;

    @Test
    void createStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setPassword("student");
        student.setPhoneNumber("09118249965");
        student.setStudentCode("sample_student_code");
        student.setRoles(Collections.emptyList());
        studentRepository.save(student);
    }

    @Test
    void getStudentByUsernameAndPassword() {
        studentRepository.save(new Student("student2 name", "student 2 last name", "student2", "student", new ArrayList<>(), "test2", "09118249968"));
        Assertions.assertNotNull(studentRepository.getStudentByUsernameAndPassword("student2", "student"));
        Assertions.assertNull(studentRepository.findStudentByUsername("test"));
    }

    @Test
    void getAllStudents() {
        Assertions.assertNotNull(studentRepository.findAll());
    }

    @Test
    void deleteStudentByStudentCode() {
        studentRepository.deleteStudentByStudentCode("sample_student_code");
        Assertions.assertNull(studentRepository.getStudentByStudentCode("sample_student_code"));
    }

    @Test
    void getStudentByStudentCode() {
        Assertions.assertNotNull(studentRepository.getStudentByStudentCode("sample_student_code"));
    }

    @Test
    void updateStudent() {
    }

    @Test
    void checkQuestionAnswers() {
        setUp();
        Exam exam = examRepository.getExamByExamCode("Exam-sample-code");
        List<QuestionAndAnswerForm> studentAnswers = Arrays.asList(
                new QuestionAndAnswerForm("q1-code", "answer4"),
                new QuestionAndAnswerForm("q2-code", "answer4")
        );

        Float examScore = 0f;
        for (Question question : exam.getQuestions()) {
            for (QuestionAndAnswerForm answer : studentAnswers) {
                if (question.getQuestionCode().equals(answer.getQuestionCode())
                        && answer.getAnswer().equals(question.getCorrectAnswer().getContent())) {
                    examScore += question.getScore();
                }
            }
        }

        Assertions.assertEquals(72, examScore);

    }

    private void setUp() {
        Exam exam = createExam();
        Question question = createQuestion("q1", 42);

        Answer answer1 = new Answer();
        answer1.setContent("answer1");
        Answer answer2 = new Answer();
        answer2.setContent("answer2");
        Answer answer3 = new Answer();
        answer3.setContent("answer3");
        Answer answer4 = new Answer();
        answer4.setContent("answer4");

        Set<Answer> answers = new HashSet<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        question.setQuestionCode("q1-code");
        question.setCorrectAnswer(answer4);
        question.setAnswers(answers);

        Question question2 = createQuestion("q2" , 30f);

        Answer q2Answer1 = new Answer();
        q2Answer1.setContent("answer1");
        Answer q2Answer2 = new Answer();
        q2Answer2.setContent("answer2");
        Answer q2Answer3 = new Answer();
        q2Answer3.setContent("answer3");
        Answer q2Answer4 = new Answer();
        q2Answer4.setContent("answer4");

        Set<Answer> q2Answers = new HashSet<>();
        q2Answers.add(q2Answer1);
        q2Answers.add(q2Answer2);
        q2Answers.add(q2Answer3);
        q2Answers.add(q2Answer4);

        question2.setQuestionCode("q2-code");
        question2.setCorrectAnswer(q2Answer4);
        question2.setAnswers(q2Answers);


        exam.getQuestions().add(question);
        exam.getQuestions().add(question2);
        examRepository.save(exam);
    }

    private Question createQuestion(String title, float score) {
        Question question = new Question();
        question.setQuestionTitle(title);
        question.setScore(score);
        return question;
    }

    private Exam createExam() {
        Exam exam = new Exam();
        exam.setExamTitle("test Exam");
        exam.setStartDateTime(LocalDateTime.of(LocalDate.of(2022, 1, 23), LocalTime.of(10, 8)));
        exam.setEndDateTime(LocalDateTime.of(LocalDate.of(2022, 1, 23), LocalTime.of(10, 30)));
        exam.setExamCode("Exam-sample-code");
        exam.setExamScore(100f);
        return exam;
    }

    @Test
    void getStudentByUsername() {
        studentRepository.save(new Student("student1 name", "student 1 last name ", "student1", "student", new ArrayList<>(), "test1", "09118249969"));
        Assertions.assertNotNull(studentRepository.findStudentByUsername("student1"));
    }
}
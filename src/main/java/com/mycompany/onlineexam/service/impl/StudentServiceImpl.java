package com.mycompany.onlineexam.service.impl;

import com.mycompany.onlineexam.domain.Exam;
import com.mycompany.onlineexam.domain.Question;
import com.mycompany.onlineexam.domain.Student;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.repository.StudentRepository;
import com.mycompany.onlineexam.service.ExamService;
import com.mycompany.onlineexam.service.StudentService;
import com.mycompany.onlineexam.service.UserService;
import com.mycompany.onlineexam.service.dto.StudentDTO;
import com.mycompany.onlineexam.service.mapper.StudentMapper;
import com.mycompany.onlineexam.web.model.QuestionAndAnswerForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mycompany.onlineexam.config.enumuration.ApplicationUserRoles.ROLE_STUDENT;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger logger = LogManager.getLogger(StudentServiceImpl.class);
    private final UserService userService ;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ExamService examService;

    @Value(value = "${application.constants.student-code-number}")
    private Integer NUMBER_OF_STUDENT_CODE;

    public StudentServiceImpl(UserService userService, PasswordEncoder passwordEncoder, StudentRepository studentRepository, StudentMapper studentMapper, ExamService examService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.examService = examService;
    }

    @Override
    public Student getStudentByUsernameAndPassword(String username, String password) {
        logger.debug("Request to get student by username:{} and password:{}", username, password);
        return studentRepository.getStudentByUsernameAndPassword(username, password);
    }

    @Override
    public Student createStudent(StudentDTO studentDTO) {
        logger.debug("Request to create an student :{}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student.setStudentCode(Constants.STUDENT + Constants.DASH + student.getStudentCode());
        student.setUsername(student.getStudentCode().split("-")[1]);
        student.setPassword(passwordEncoder.encode(studentDTO.getPhoneNumber()));
        Student savedStudent = studentRepository.save(student);
        userService.addRoleToUser(savedStudent.getUsername(), ROLE_STUDENT.name());
        return savedStudent;
    }

    @Override
    public List<Student> getAllStudents() {
        logger.debug("Request to get all students in service layer");
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudentByStudentCode(String studentCode) {
        logger.debug("Request to delete student with student-code:{}", studentCode);
        studentRepository.deleteStudentByStudentCode(studentCode);
    }

    @Override
    public Student getStudentByStudentCode(String studentCode) {
        logger.debug("Request to get student by studentCode:{} in service layer", studentCode);
        return studentRepository.getStudentByStudentCode(studentCode);
    }

    @Override
    public Student updateStudent(Student student) {
        logger.debug("Request to update student:{}", student);
        return studentRepository.save(student);
    }

    @Override
    public float checkQuestionAnswers(List<QuestionAndAnswerForm> studentAnswers, Exam exam, String studentCode) {
        logger.debug("Request to answer questions for exam with exam-code:{} , answers :{} in service layer", exam.getExamCode(), studentAnswers);
        Float examScore = 0f;
        for (Question question : exam.getQuestions()) {
            for (QuestionAndAnswerForm answer : studentAnswers) {
                if (question.getQuestionCode().equals(answer.getQuestionCode())
                        && answer.getAnswer().equals(question.getCorrectAnswer().getContent())) {
                    examScore += question.getScore();
                }
            }
        }
        Student student = studentRepository.getStudentByStudentCode(studentCode);
        exam.getStudentScores().put(student.getId(), examScore);
        examService.update(exam);
        return examScore;
    }

    @Override
    public Student getStudentByUsername(String username) {
        return studentRepository.findStudentByUsername(username);
    }

}

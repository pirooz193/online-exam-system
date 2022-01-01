package com.mycompany.onlineexam.service.impl;

import com.mycompany.onlineexam.domain.Exam;
import com.mycompany.onlineexam.domain.Question;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.repository.ExamRepository;
import com.mycompany.onlineexam.service.ExamService;
import com.mycompany.onlineexam.service.QuestionService;
import com.mycompany.onlineexam.service.dto.ExamDTO;
import com.mycompany.onlineexam.service.mapper.ExamMapper;
import com.mycompany.onlineexam.web.errors.IsNotStartTimeException;
import com.mycompany.onlineexam.web.errors.TimeIsUpException;
import com.mycompany.onlineexam.web.mdel.ApiUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private Logger logger = LogManager.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final QuestionService questionService;

    public ExamServiceImpl(ExamRepository examRepository, ExamMapper examMapper, QuestionService questionService) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.questionService = questionService;
    }

    @Override
    public Exam save(ExamDTO examDTO) {
        logger.debug("Request to  create an Exam  in service layer :{}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam.setExamCode(ApiUtil.generateRandomCode(Constants.EXAM_CODE, 7));
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAllExams() {
        logger.info("Request to get all Exams in service layer");
        return examRepository.findAll();
    }

    @Override
    public void deleteExamByExamCode(String examCode) {
        logger.info("Request to delete an Exam with exam-code :{}", examCode);
        examRepository.deleteExamByExamCode(examCode);
    }

    @Override
    public void deleteQuestion(String questionCode, String examCode) {
        logger.info("Request to delete Exam's question with question-code :{} in service layer", questionCode);
        Exam exam = examRepository.getExamByExamCode(examCode);
        Question question = questionService.getQuestionByQuestionCode(questionCode);
        exam.getQuestions().remove(question);
        logger.debug("Successfully removed question");
    }

    @Override
    public Exam getExamByExamCode(String examCode) {
        logger.debug("Request to get Exam by exam-code :{}", examCode);
        return examRepository.getExamByExamCode(examCode);
    }

    @Override
    public Exam update(Exam exam) {
        logger.debug("Request to update Exam:{}", exam);
        return examRepository.save(exam);
    }

    /**
     * Check Exam time
     *
     * @param exam
     * @throws IsNotStartTimeException
     */
    @Override
    public void checkExamTime(Exam exam) throws IsNotStartTimeException, TimeIsUpException {
        logger.debug("Request to check Exam time : {}", exam);
        LocalDateTime dateTime = LocalDateTime.now();
        if (LocalDateTime.now().isBefore(exam.getStartDateTime()))
            throw new IsNotStartTimeException();
        else if (LocalDateTime.now().isAfter(exam.getEndDateTime())) {
            throw new TimeIsUpException();
        }
    }

    @Override
    public Long checkExamRemainingTime(String examCode) throws TimeIsUpException {
        logger.debug("Request to get Exam remaining time with exam-code:{} in service layer", examCode);
        Exam exam = getExamByExamCode(examCode);
        checkExamTime(exam);
        Long remainingTime = ChronoUnit.MINUTES.between(LocalDateTime.now(), exam.getEndDateTime());
        if (remainingTime <= 0) throw new TimeIsUpException();
        return remainingTime;
    }

    @Override
    public ExamDTO updateExamStartAndEndTime(String examCode, LocalDateTime startTime, LocalDateTime endTime) {
        logger.debug("Request to update time of exam : {}, start-time:{} and end-time :{}", examCode, startTime, endTime);
        Exam exam = examRepository.getExamByExamCode(examCode);
        exam.setStartDateTime(startTime);
        exam.setEndDateTime(endTime);
        Exam savedExam = examRepository.save(exam);
        return examMapper.toDTO(savedExam);
    }

}

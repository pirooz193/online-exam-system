package com.mycompany.onlineexam.service;

import com.mycompany.onlineexam.domain.Exam;
import com.mycompany.onlineexam.service.dto.ExamDTO;
import com.mycompany.onlineexam.web.errors.IsNotStartTimeException;
import com.mycompany.onlineexam.web.errors.TimeIsUpException;
import com.mycompany.onlineexam.web.model.ExamQuestionsForm;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamService {
    Exam save(ExamDTO examDTO , String courseCode);

    List<Exam> getAllExams();

    void deleteExamByExamCode(String examCode, String courseCode);

    void deleteQuestion(String questionCode , String examCode);

    Exam getExamByExamCode(String examCode);

    Exam update(Exam exam);

    void checkExamTime(Exam exam) throws IsNotStartTimeException, TimeIsUpException;

    Long checkExamRemainingTime(String examCode) throws TimeIsUpException;

    ExamDTO updateExamStartAndEndTime(String examCode, LocalDateTime startTime, LocalDateTime endTime);

    List<ExamQuestionsForm> getExamQuestionsForStudent(Exam exam);
}

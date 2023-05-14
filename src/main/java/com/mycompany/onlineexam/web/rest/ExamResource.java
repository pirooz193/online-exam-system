package com.mycompany.onlineexam.web.rest;

import com.mycompany.onlineexam.domain.Exam;
import com.mycompany.onlineexam.service.ExamService;
import com.mycompany.onlineexam.service.dto.ExamDTO;
import com.mycompany.onlineexam.web.model.ExamQuestionsForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamResource {

    private Logger logger = LogManager.getLogger(ExamResource.class);

    private final ExamService examService;

    public ExamResource(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/master/create-exam")
    public ResponseEntity<Exam> createExam(@RequestBody ExamDTO examDTO , @RequestParam String courseCode) {
        logger.info("Request to create an Exam :{} to  course with course-code :{}", examDTO , courseCode);
        Exam exam = examService.save(examDTO, courseCode);
        return ResponseEntity.created(URI.create("/create")).body(exam);
    }

    @GetMapping("/admin/get-all-exams")
    public ResponseEntity<List<Exam>> getAllExams() {
        logger.info("Request to get all Exams");
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @DeleteMapping("/master/delete-exam/{courseCode}")
    public ResponseEntity<?> deleteExam(@RequestParam String examCode, @PathVariable String courseCode) {
        logger.info("Request to delete an Exam with exam-code :{}", examCode);
        examService.deleteExamByExamCode(examCode, courseCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/master/delete-question")
    public ResponseEntity<?> deleteQuestion(@RequestParam String questionCode, @RequestParam String examCode) {
        logger.info("Request to delete Exam's question with question-code :{}", questionCode);
        examService.deleteQuestion(questionCode, examCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/master/exam-info")
    public ResponseEntity<Exam> getExamInfoByExamCode(@RequestParam String examCode) {
        logger.info("Request to check exam with exam-code :{}", examCode);
        Exam exam = examService.getExamByExamCode(examCode);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/exam/remaining-time")
    public ResponseEntity<Long> getExamRemainingTime(@RequestParam String examCode) {
        logger.info("Request to get Exam remaining time with exam-code:{}", examCode);
        Long remainingTime = examService.checkExamRemainingTime(examCode);
        return ResponseEntity.ok(remainingTime);
    }

    @GetMapping("/student/get-exam-by-student")
    public ResponseEntity<List<ExamQuestionsForm>> getExamQuestions(@RequestParam String examCode) {
        logger.info("Request to show exam questions by  exam-code :{}", examCode);
        Exam exam = examService.getExamByExamCode(examCode);
        examService.checkExamTime(exam);
        List<ExamQuestionsForm> examQuestion = examService.getExamQuestionsForStudent(exam);
        return ResponseEntity.ok(examQuestion);
    }

    @PutMapping("/master/update-time")
    public ResponseEntity<ExamDTO> updateExamTime(@RequestParam String examCode,
                                                  @RequestParam String startTime,
                                                  @RequestParam String endTime) {
        logger.debug("Request to update time of exam : {}, start-time:{} and end-time :{}", examCode, startTime, endTime);

        ExamDTO examDTO = examService.updateExamStartAndEndTime(examCode,LocalDateTime.parse(startTime), LocalDateTime.parse(endTime));
        return ResponseEntity.ok(examDTO);
    }
}

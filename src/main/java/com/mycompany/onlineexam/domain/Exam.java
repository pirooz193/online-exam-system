package com.mycompany.onlineexam.domain;

import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.web.model.ApiUtil;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "exam_table")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true)
    private String examTitle;
    @Column(name = "exam_code", nullable = false, unique = true)
    private String examCode;
    @Column(name = "exam_score" , nullable = false)
    private Float  examScore ;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startDateTime ;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endDateTime ;
    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Set<Question> questions = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "student_score_mapping",
            joinColumns = {@JoinColumn(name = "student_id")})
    @MapKeyColumn(name = "exam_id")
    @Column(name = "student_score")
    private Map<Long, Float> studentScores = new HashMap<>();


    public Exam() {
        this.examCode = ApiUtil.generateRandomCode(Constants.EXAM_CODE, Constants.EXAM_CODE_LENGTH);
    }

    public Exam(Long id, String examTitle, Set<Question> questions) {
        this.id = id;
        this.examTitle = examTitle;
        this.questions = questions;
        this.examCode = ApiUtil.generateRandomCode(Constants.EXAM_CODE, Constants.EXAM_CODE_LENGTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id.equals(exam.id) &&
                examTitle.equals(exam.examTitle) &&
                examCode.equals((exam.examCode)) &&
                questions.equals(exam.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examTitle, questions);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", examTitle='" + examTitle + '\'' +
                ", examCode='" + examCode + '\'' +
                ", questions=" + questions +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public Map<Long, Float> getStudentScores() {
        return studentScores;
    }

    public void setStudentScores(Map<Long, Float> studentScores) {
        this.studentScores = studentScores;
    }

    public Float getExamScore() {
        return examScore;
    }

    public void setExamScore(Float examScore) {
        this.examScore = examScore;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}

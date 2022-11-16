package com.mycompany.onlineexam.domain;


import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.web.model.ApiUtil;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "question_table")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true)
    private String questionTitle;
    @Column(name = "question_code", nullable = false, unique = true)
    private String questionCode;
    @Column(name = "question_score" ,nullable = false )
    private Float score ;
    @OneToOne(cascade = CascadeType.ALL)
    private Answer correctAnswer;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Answer> answers = new HashSet<>();

    public Question() {
        this.questionCode = ApiUtil.generateRandomCode(Constants.QUESTION, Constants.QUESTION_CODE_LENGTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id) &&
                questionTitle.equals(question.questionTitle) &&
                correctAnswer.equals(question.correctAnswer) &&
                questionCode.equals(question.questionCode) &&
                answers.equals(question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionTitle, questionCode, correctAnswer, answers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionCode='" + questionCode + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", answers=" + answers +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}

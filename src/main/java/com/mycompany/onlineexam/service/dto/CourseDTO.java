package com.mycompany.onlineexam.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import java.util.List;
import java.util.Set;

public class CourseDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("course_title")
    @NotNull
    private String courseTitle;
    @JsonProperty("course_code")
    private String courseCode;
    @JsonProperty("capacity")
    @NotNull
    private Integer capacity;
    @JsonProperty("exam")
    private List<ExamDTO> examDTOS;
    @JsonProperty("master")
    private MasterDTO masterDTO;
    @JsonProperty("students")
    private Set<StudentDTO> studentDTOS;


    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", capacity=" + capacity +
                ", examDTOS=" + examDTOS +
                ", masterDTO=" + masterDTO +
                ", studentDTOS=" + studentDTOS +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public List<ExamDTO> getExamDTOS() {
        return examDTOS;
    }

    public void setExamDTOS(List<ExamDTO> examDTOS) {
        this.examDTOS = examDTOS;
    }

    public MasterDTO getMasterDTO() {
        return masterDTO;
    }

    public void setMasterDTOS(MasterDTO masterDTO) {
        this.masterDTO = masterDTO;
    }

    public Set<StudentDTO> getStudentDTOS() {
        return studentDTOS;
    }

    public void setStudentDTOS(Set<StudentDTO> studentDTOS) {
        this.studentDTOS = studentDTOS;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

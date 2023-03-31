package com.mycompany.onlineexam.web.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CourseModel {
    @JsonProperty(value = "course_title")
    @NotNull
    private String courseTitle;
    @JsonProperty(value = "capacity")
    @NotNull
    private int capacity;
    @JsonProperty(value = "teacher_code")
    @NotNull
    private String masterCode;

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }
}

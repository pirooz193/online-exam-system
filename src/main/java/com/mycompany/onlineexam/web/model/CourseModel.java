package com.mycompany.onlineexam.web.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseModel {
    @JsonProperty(value = "course_title")
    private String courseTitle;
    @JsonProperty(value = "capacity")
    private int capacity;
    @JsonProperty(value = "teacher_code")
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

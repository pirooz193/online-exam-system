package com.mycompany.onlineexam.service;

import com.mycompany.onlineexam.domain.Course;
import com.mycompany.onlineexam.service.dto.CourseDTO;
import com.mycompany.onlineexam.web.errors.FullCapacityException;
import com.mycompany.onlineexam.web.errors.StudentExistenceException;
import com.mycompany.onlineexam.web.model.CourseModel;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseModel courseModel);

    Integer addStudentToCourse(String studentCode, String courseCode) throws FullCapacityException, StudentExistenceException;

    Course addMasterToCourse(String masterCode, String courseCode);

    Course getCourseByCourseCode(String courseCode);

    List<Course> getAllCourses();

    List<Course> getRequiredMasterCourses(String masterCode);

    CourseDTO updateCourseInfo(CourseDTO courseDTO);
}

package com.mycompany.onlineexam.web.rest;

import com.mycompany.onlineexam.domain.Course;
import com.mycompany.onlineexam.service.CourseService;
import com.mycompany.onlineexam.service.dto.CourseDTO;
import com.mycompany.onlineexam.web.errors.FullCapacityException;
import com.mycompany.onlineexam.web.errors.StudentExistenceException;
import com.mycompany.onlineexam.web.model.CourseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger logger = LogManager.getLogger(CourseResource.class);

    private final CourseService courseService;

    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/admin/create-course")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseModel courseModel) {
        logger.info("Request to create a new Course:{} ", courseModel);
        CourseDTO course = courseService.createCourse(courseModel);
        return ResponseEntity.created(URI.create("/created")).body(course);
    }

    @GetMapping("/admin/all-courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        logger.info("Request to get all courses");
            List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/master/get-course")
    public ResponseEntity<Course> getCourse(@RequestParam String courseCode) {
        logger.info("Request to get Course vy course-code :{}", courseCode);
            Course course = courseService.getCourseByCourseCode(courseCode);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/admin/update-course")
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDTO) {
        logger.info("Request to update course :{}", courseDTO);
        CourseDTO updatedCourseDTO = courseService.updateCourseInfo(courseDTO);
        return ResponseEntity.ok(updatedCourseDTO) ;
    }

    @GetMapping("/master/get-master-courses")
    public ResponseEntity<List<Course>> getMasterCourses(@RequestParam String masterCode) {
        logger.info("Request to  get courses by master with master-code :{}", masterCode);
        List<Course> masterCourses = courseService.getRequiredMasterCourses(masterCode);
        return ResponseEntity.ok(masterCourses);
    }

    @PutMapping("/master/add-student")
    public ResponseEntity<Integer> addStudentToCourse(@RequestParam String courseCode, @RequestParam String studentCode) throws FullCapacityException, StudentExistenceException {
        logger.info("Request to add ann student with studentCode:{} to  course with courseCode:{}", studentCode, courseCode);
            Integer courseCapacity = courseService.addStudentToCourse(studentCode, courseCode);
        return ResponseEntity.ok(courseCapacity);
    }

    @PutMapping("/admin/add-master")
    public ResponseEntity<Course> addMasterToCourse(@RequestParam String masterCode, @RequestParam String courseCode) {
        logger.info("Request to add ann master with masterCode:{} to  course with courseCode:{}", masterCode, courseCode);
            Course course = courseService.addMasterToCourse(masterCode, courseCode);
        return ResponseEntity.ok(course);
    }
}

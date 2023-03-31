package com.mycompany.onlineexam.service.impl;

import com.mycompany.onlineexam.domain.Course;
import com.mycompany.onlineexam.domain.Master;
import com.mycompany.onlineexam.domain.Student;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.repository.CourseRepository;
import com.mycompany.onlineexam.service.CourseService;
import com.mycompany.onlineexam.service.MasterService;
import com.mycompany.onlineexam.service.StudentService;
import com.mycompany.onlineexam.service.dto.CourseDTO;
import com.mycompany.onlineexam.service.mapper.CourseMapper;
import com.mycompany.onlineexam.web.errors.CourseListIsEmptyException;
import com.mycompany.onlineexam.web.errors.FullCapacityException;
import com.mycompany.onlineexam.web.errors.NotFoundException;
import com.mycompany.onlineexam.web.errors.StudentExistenceException;
import com.mycompany.onlineexam.web.model.ApiUtil;
import com.mycompany.onlineexam.web.model.CourseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger logger = LogManager.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final MasterService masterService;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepository courseRepository, StudentService studentService, MasterService masterService, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.masterService = masterService;
        this.courseMapper = courseMapper;
    }

    /**
     * Create new course
     * get the initial data of a course and fetch required data from DB like the required master.
     *
     * @param courseModel
     * @return
     */
    @Override
    public CourseDTO createCourse(CourseModel courseModel) {
        logger.debug("Request to create a new Course:{} ", courseModel);
        Course newCourse = new Course();
        newCourse.setCourseTitle(courseModel.getCourseTitle());
        newCourse.setCourseCapacity(courseModel.getCapacity());
        Master master = masterService.getMasterByMasterCode(Constants.MASTER_CODE + Constants.DASH + courseModel.getMasterCode());
        if (master == null) throw new NotFoundException("Teacher with code [" + courseModel.getMasterCode() + "]");
        newCourse.setMaster(master);
        newCourse.setCourseCode(ApiUtil.generateRandomCode(Constants.COURSE, 5));
        Course savedCourse = courseRepository.save(newCourse);
        return courseMapper.toDTO(savedCourse);
    }

    /**
     * Get Course and Student by course-code and student-code
     * Add student to course and calculate course capacity nex adding
     *
     * @param studentCode
     * @param courseCode
     * @return
     */
    @Override
    public Integer addStudentToCourse(String studentCode, String courseCode) throws FullCapacityException, StudentExistenceException {
        logger.debug("Request to add ann student with studentCode:{} to  course with courseCode:{}", studentCode, courseCode);
        Course course = courseRepository.findCourseByCourseCode(courseCode);
        if (course.getStudents().size() >= course.getCourseCapacity())
            throw new FullCapacityException(course.getCourseTitle());
        if (course.getStudents().stream().anyMatch(student -> student.getStudentCode().equals(studentCode)))
            throw new StudentExistenceException(studentCode);
        Student student = studentService.getStudentByStudentCode(studentCode);
        course.getStudents().add(student);
        courseRepository.save(course);
        return course.getCourseCapacity() - course.getStudents().size();
    }

    /**
     * Add a master to course
     * get master and course by  master-code and course-code
     *
     * @param masterCode
     * @param courseCode
     * @return
     */
    @Override
    public Course addMasterToCourse(String masterCode, String courseCode) {
        logger.debug("Request to add ann master with masterCode:{} to  course with courseCode:{}", masterCode, courseCode);
        Course course = courseRepository.findCourseByCourseCode(courseCode);
        Master master = masterService.getMasterByMasterCode(masterCode);
        course.setMaster(master);
        courseRepository.save(course);
        return course;
    }

    /**
     * Fetch  course by  course-code
     *
     * @param courseCode
     * @return
     */
    @Override
    public Course getCourseByCourseCode(String courseCode) {
        logger.debug("Request to get Course vy course-code :{} in service layer", courseCode);
        return courseRepository.findCourseByCourseCode(courseCode);
    }

    /**
     * Fetch all courses
     *
     * @return
     */
    @Override
    public List<Course> getAllCourses() {
        logger.info("Request to get all courses");
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getRequiredMasterCourses(String masterCode) {
        logger.debug("Request to get master :{} courses", masterCode);
        Master master = masterService.getMasterByMasterCode(masterCode);
        List<Course> allCourses = courseRepository.findAll();
        List<Course> masterCourses = allCourses.stream().filter(course -> course.getMaster() != null && course.getMaster().getMasterCode().equals(master.getMasterCode())
        ).collect(Collectors.toList());
        if (masterCourses.isEmpty()) throw new CourseListIsEmptyException();
        return masterCourses;
    }

    @Override
    public CourseDTO updateCourseInfo(CourseDTO courseDTO) {
        logger.info("Request to update course :{} in service layer", courseDTO);
        Course course = courseRepository.findCourseByCourseCode(courseDTO.getCourseCode());
        if (courseDTO.getCourseTitle() != null) course.setCourseTitle(courseDTO.getCourseTitle());
        if (courseDTO.getCapacity() != null) course.setCourseCapacity(courseDTO.getCapacity());
        courseRepository.save(course);
        return courseMapper.toDTO(course);
    }

}

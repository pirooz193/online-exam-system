package com.mycompany.onlineexam.domain;

import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.web.model.ApiUtil;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "course_table")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true)
    private String courseTitle;
    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;
    @Column(name = "course_capacity", nullable = false)
    private Integer courseCapacity;
    @OneToMany(/*cascade = CascadeType.PERSIST*/)
//    @JoinColumn(name = "exam_id")
    private List<Exam> examList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_students",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")}
    )
    private Set<Student> students = new HashSet<>();

    public Course() {
        this.courseCode = ApiUtil.generateRandomCode(Constants.COURSE, Constants.COURSE_CODE_LENGTH);
    }

    public Course(Long id, String courseTitle, String courseCode, List<Exam> examList, Master master, Set<Student> students) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseCode = courseCode;
        this.examList = examList;
        this.master = master;
        this.students = students;
        this.courseCode = ApiUtil.generateRandomCode(Constants.COURSE, Constants.COURSE_CODE_LENGTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id) &&
                courseTitle.equals(course.courseTitle) &&
                courseCode.equals(course.courseCode) &&
                examList.equals(course.examList) &&
                master.equals(course.master) &&
                students.equals(course.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseTitle, courseCode, examList, master, students);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", examList=" + examList +
                ", master =" + master +
                ", students=" + students +
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

    public List<Exam> getExamList() {
        return examList;
    }

    public void setExamList(List<Exam> examList) {
        this.examList = examList;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Integer getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(Integer courseCapacity) {
        this.courseCapacity = courseCapacity;
    }
}

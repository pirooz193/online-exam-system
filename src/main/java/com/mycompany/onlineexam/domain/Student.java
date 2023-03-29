package com.mycompany.onlineexam.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "student_table")
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    public Student(String name, String lastName, String username, String password, List<Role> roles, String studentCode, String phoneNumber) {
        super(name, lastName, username, password, roles);

        this.studentCode = studentCode;
        this.phoneNumber = phoneNumber;
    }

    public Student() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return id == student.id && studentCode.equals(student.studentCode) && phoneNumber.equals(student.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, studentCode, phoneNumber);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

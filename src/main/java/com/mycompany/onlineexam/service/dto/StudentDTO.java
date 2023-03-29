package com.mycompany.onlineexam.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.onlineexam.domain.Role;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StudentDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    @NotNull
    private String name;
    @JsonProperty("lastName")
    @NotNull
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("studentCode")
    @NotNull
    private String studentCode;
    @JsonProperty("phone_number")
    @NotNull
    private String phoneNumber;
    @JsonProperty("roles")
    private List<Role> roles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return id.equals(that.id) && name.equals(that.name) && lastName.equals(that.lastName) && username.equals(that.username) && password.equals(that.password) && studentCode.equals(that.studentCode) && phoneNumber.equals(that.phoneNumber) && roles.equals(that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, username, password, studentCode, phoneNumber, roles);
    }

    @Override
    public String toString() {
        return "StudentDTO{" + "id=" + id + ", name='" + name + '\'' + ", lastName='" + lastName + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + ", studentCode='" + studentCode + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", roles=" + roles + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}

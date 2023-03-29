package com.mycompany.onlineexam.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "admin")
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "admin_code", nullable = false, unique = true)
    private String adminCode;

    public Admin(String name, String lastName, String username, String password, List<Role> roles, String adminCode) {
        super(name, lastName, username, password, roles);
        this.adminCode = adminCode;
    }

    public Admin() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return id == admin.id && adminCode.equals(admin.adminCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, adminCode);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminCode='" + adminCode + '\'' +
                '}';
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }
}

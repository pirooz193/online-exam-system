package com.mycompany.onlineexam.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "master_table")
public class Master extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "master_code", nullable = false, unique = true)
    private String masterCode;
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    public Master(String name, String lastName, String username, String password, List<Role> roles, String masterCode, String phoneNumber) {
        super(name, lastName, username, password, roles);
        this.masterCode = masterCode;
        this.phoneNumber = phoneNumber;
    }

    public Master() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Master master = (Master) o;
        return id == master.id && masterCode.equals(master.masterCode) && phoneNumber.equals(master.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, masterCode, phoneNumber);
    }

    @Override
    public String toString() {
        return "Master{" +
                "id=" + id +
                ", masterCode='" + masterCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

package com.thymeleaf.springbootthymeleaf.entiy;

import java.io.Serializable;

public class Student implements Serializable {
    private String student_id;
    private String name;
    private String phone;
    private String email;
    private String locked;
    private String gmt_created;
    private String gmt_modified;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getGmt_created() {
        return gmt_created;
    }

    public void setGmt_created(String gmt_created) {
        this.gmt_created = gmt_created;
    }

    public String getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(String gmt_modified) {
        this.gmt_modified = gmt_modified;
    }
}

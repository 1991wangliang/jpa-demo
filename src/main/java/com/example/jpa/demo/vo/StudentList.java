package com.example.jpa.demo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class StudentList {

    private int id;

    private String name;

    private Date createTime;

    private int classId;

    private String className;

    public StudentList(int id, String name, Date createTime, int classId, String className) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.classId = classId;
        this.className = className;
    }
}

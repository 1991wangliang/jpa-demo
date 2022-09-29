package com.example.jpa.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_teacher_class")
@Setter
@Getter
public class TeacherClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int teacherId;

    private int classId;

}

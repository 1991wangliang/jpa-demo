package com.example.jpa.demo.repository;

import com.example.jpa.demo.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {


}

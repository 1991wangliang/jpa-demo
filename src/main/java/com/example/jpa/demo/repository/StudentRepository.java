package com.example.jpa.demo.repository;

import com.example.jpa.demo.entity.Student;
import com.example.jpa.demo.vo.StudentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {

    @Query(value = "select new com.example.jpa.demo.vo.StudentList(s.id,s.name,s.createTime,c.id,c.name) from Student s left join SchoolClass c on s.classId = c.id  where s.name like CONCAT('%',?1,'%') ")
    Page<StudentList> findByNameLike(String name, PageRequest pageRequest);


    List<Student> findByNameContaining(String name,PageRequest pageRequest);

}

package com.example.jpa.demo.controller;

import com.example.jpa.demo.dto.*;
import com.example.jpa.demo.entity.SchoolClass;
import com.example.jpa.demo.entity.Student;
import com.example.jpa.demo.entity.Teacher;
import com.example.jpa.demo.repository.ClassRepository;
import com.example.jpa.demo.repository.StudentRepository;
import com.example.jpa.demo.repository.TeacherRepository;
import com.example.jpa.demo.vo.StudentList;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;
import java.util.*;


@Configuration
public class ControllerConfiguration {

    @RestController
    @RequestMapping("/class")
    @AllArgsConstructor
    public static class ClassController{

        private final ClassRepository classRepository;

        @PostMapping("/save")
        public String save(@RequestBody ClassDTO.SaveRequest request){
            SchoolClass schoolClass = new SchoolClass();
            schoolClass.setName(request.getName());
            schoolClass.setCreateTime(new Date());
            classRepository.save(schoolClass);
            return "success";
        }

        @GetMapping("/page")
        public PageResponse<SchoolClass> page(PageRequest pageRequest){
            return  PageResponse.of(classRepository.findAll(pageRequest.getPageRequest(Sort.by("createTime").descending())));
        }

        @GetMapping("/find")
        public PageResponse<SchoolClass> find(ClassDTO.FindRequest request){
            Specification<SchoolClass> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();
                if (request.haveName()) {
                    Predicate predicate = criteriaBuilder.like(
                            root.<String>get("name"), "%" + request.getName() + "%");
                    predicateList.add(predicate);
                }
                return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
            };
            return  PageResponse.of(classRepository.findAll(specification,request.getPageRequest(Sort.by("createTime").descending())));
        }

    }

    @RestController
    @RequestMapping("/student")
    @AllArgsConstructor
    public static class StudentController{

        private final StudentRepository studentRepository;

        private final EntityManager entityManager;

        @PostMapping("/save")
        public String save(@RequestBody StudentDTO.SaveRequest request){
            Student student = new Student();
            student.setName(request.getName());
            student.setClassId(request.getClassId());
            student.setCreateTime(new Date());
            studentRepository.save(student);
            return "success";
        }

        @GetMapping("/page1")
        public PageResponse<StudentList> page1(StudentDTO.PageQueryRequest request){
            return  PageResponse.of(studentRepository.findByNameLike(request.getName(),request.getPageRequest(Sort.by("createTime").descending())));
        }

        @GetMapping("/page2")
        public PageResponse<Student> page2(StudentDTO.PageQueryRequest request){
            return  PageResponse.of(studentRepository.findByNameContaining(request.getName(),request.getPageRequest(Sort.by("createTime").descending())));
        }

        @GetMapping("/page3")
        public PageResponse<Student> page3(StudentDTO.PageQueryRequest request){
            StringBuilder builder = new StringBuilder();
            Map<String,Object> parameters = new HashMap<>();
            builder.append("select s from Student s where 1 = 1");
            if(request.haveName()){
                builder.append(" and s.name = :name");
                parameters.put("name",request.getName());
            }
            TypedQuery<Student> query = entityManager.createQuery(builder.toString(),Student.class);
            for(String name:parameters.keySet()){
                query.setParameter(name,parameters.get(name));
            }
            return  PageResponse.of(query.getResultList());
        }

    }

    @RestController
    @RequestMapping("/teacher")
    @AllArgsConstructor
    public static class TeacherController{

        private final TeacherRepository teacherRepository;

        @PostMapping("/save")
        public String save(@RequestBody TeacherDTO.SaveRequest request){
            Teacher teacher = new Teacher();
            teacher.setName(request.getName());
            teacherRepository.save(teacher);
            return "success";
        }

        @GetMapping("/page")
        public PageResponse<Teacher> page(){
            return  PageResponse.of(teacherRepository.findAll());
        }

        @GetMapping("/find")
        public List<Teacher> find(TeacherDTO.FindRequest request){
            return  teacherRepository.findAll(request.getExample());
        }

    }


}

package com.example.jpa.demo.dto;

import com.example.jpa.demo.entity.Teacher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class TeacherDTO {

    @Setter
    @Getter
    public static class SaveRequest{
        private String name;
    }

    @Getter
    @Setter
    public static class FindRequest {
        private String name;


        public Example<Teacher> getExample(){
            Teacher teacher = new Teacher();
            teacher.setName(name);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.REGEX).contains())
                    .withIgnorePaths("id");
            return Example.of(teacher,matcher);
        }
    }


}

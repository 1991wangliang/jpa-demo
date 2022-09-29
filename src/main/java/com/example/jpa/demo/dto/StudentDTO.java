package com.example.jpa.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

public class StudentDTO {

    @Setter
    @Getter
    public static class SaveRequest{
        private String name;
        private int classId;
    }


    @Setter
    @Getter
    public static class PageQueryRequest extends PageRequest{
        private String name;

        public boolean haveName(){
            return StringUtils.hasText(name);
        }

    }
}

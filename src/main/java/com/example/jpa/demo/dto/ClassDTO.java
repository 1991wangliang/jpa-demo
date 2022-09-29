package com.example.jpa.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

public class ClassDTO {

    @Setter
    @Getter
    public static class SaveRequest{
        private String name;
    }

    @Setter
    @Getter
    public static class FindRequest extends PageRequest{
        private String name;

        public boolean haveName(){
            return StringUtils.hasText(name);
        }
    }

}

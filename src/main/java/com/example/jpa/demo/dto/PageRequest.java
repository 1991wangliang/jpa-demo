package com.example.jpa.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class PageRequest {

    private int current;
    private int pageSize;


    public org.springframework.data.domain.PageRequest getPageRequest(){
        return org.springframework.data.domain.PageRequest.of(current,pageSize);
    }


    public org.springframework.data.domain.PageRequest getPageRequest(Sort sort){
        return org.springframework.data.domain.PageRequest.of(current,pageSize,sort);
    }

}

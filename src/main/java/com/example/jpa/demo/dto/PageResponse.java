package com.example.jpa.demo.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final long total;
    private final List<T> content;

    public PageResponse(long total, List<T> content) {
        this.total = total;
        this.content = content;
    }

    public PageResponse(List<T> content) {
        this.total = content.size();
        this.content = content;
    }

    public PageResponse(Page<T> page) {
        this.total = page.getTotalElements();
        this.content = page.getContent();
    }

    public static <T> PageResponse<T> of(Page<T> page){
        return new PageResponse<T>(page);
    }

    public static <T> PageResponse<T> of(long total, List<T> content){
        return new PageResponse<T>(total, content);
    }

    public static <T> PageResponse<T> of(List<T> content){
        return new PageResponse<T>(content);
    }

}

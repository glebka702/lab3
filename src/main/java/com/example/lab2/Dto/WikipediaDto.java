package com.example.lab2.Dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WikipediaDto {
    private String term;
    private String summary;

    public WikipediaDto(String term, String summary) {
        this.term = term;
        this.summary = summary;
    }

}

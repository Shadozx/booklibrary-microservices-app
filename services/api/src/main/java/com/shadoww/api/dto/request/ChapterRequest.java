package com.shadoww.api.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChapterRequest {

    private String title;

    private String patternText;

    private int chapterNumber;


    @JsonIgnore
    public boolean isEmpty() {
        return isTitleEmpty() && isTextEmpty() && isNumberOfPageEmpty();
    }

    @JsonIgnore
    public boolean isTitleEmpty() {
        return title == null || title.equals("");
    }

    @JsonIgnore
    public boolean isTextEmpty() {
        return patternText == null || patternText.equals("");
    }

    @JsonIgnore
    public boolean isNumberOfPageEmpty() {
        return chapterNumber <=0;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "ChapterForm{" +
                "title='" + title + '\'' +
                ", len text='" + patternText.trim().length() + '\'' +
                ", numberOfPage=" + chapterNumber +
                '}';
    }
}

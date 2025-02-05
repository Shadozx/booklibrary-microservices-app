package com.shadoww.parserservice.util.instances;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookInstance {

    private String title;

    private String description;


    private long amount;

    private ImageInstance bookImage;

    private List<ChapterInstance> chapters;

    private List<AuthorInstance> authors;

    public BookInstance(String parsedTitle, String parsedDescription) {
        this.title = parsedTitle;
        this.description = parsedDescription;

        chapters = new ArrayList<>();
        authors = new ArrayList<>();
    }

    public int getAmount() {
        return chapters == null ? 0 : chapters.size();
    }

    public void addAllAuthors(List<AuthorInstance> authors) {
        this.authors.addAll(authors);
    }

    @Override
    public String toString() {
        return "BookInstance{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
//                ", amount=" + getAmount() +
                '}';
    }
}


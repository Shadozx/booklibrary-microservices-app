package com.shadoww.commentservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Chapter")
@Setter
@Getter
public class ChapterComment extends Comment {

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;
}
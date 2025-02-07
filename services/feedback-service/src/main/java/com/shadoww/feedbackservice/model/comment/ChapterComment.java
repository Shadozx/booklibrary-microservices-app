package com.shadoww.feedbackservice.model.comment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
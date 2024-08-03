package com.shadoww.parserservice.util.instances;


//import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ChapterInstance {

    private String title = "";

    private String text = "";

    private int chapterNumber;

    private List<ImageInstance> images = new ArrayList<>();

    public ChapterInstance(String title, String text, int chapterNumber) {
        this.title = title;
        this.text = text;
        this.chapterNumber = chapterNumber;
    }

    public void addTitle(String title) {
        this.title += title;
    }


    public void addText(String text) {
        if(!this.hasText(text)) {
            this.text += Objects.equals(this.text, "") ? text : "\n" + text;
        }
    }

    public void addChapterImage(ImageInstance image) {
        if (image != null) this.images.add(image);
    }
    private boolean hasText(String text) {
        return this.text.endsWith(text);
    }
//    private String getParagraph(String text) {
//        return "<p>" + text + "</p>";
//    }
    public boolean isTitleEmpty() { return title == null || title.equals("");}

    public boolean isTextEmpty() {
        return text == null || text.equals("");
    }

    public boolean isEmpty() {
        return isTitleEmpty() && isTextEmpty();
    }

    public void addChapterInstance(ChapterInstance chapter) {
        this.addText(chapter.getText());
        this.images.addAll(chapter.getImages());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterInstance that = (ChapterInstance) o;
        return Objects.equals(title, that.title) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text);
    }

    @Override
    public String toString() {
        return "ChapterInstance{" +
                "title='" + title + '\'' +
                ", text='{{\n" + text + "\n}}" +
                "}";
    }
}
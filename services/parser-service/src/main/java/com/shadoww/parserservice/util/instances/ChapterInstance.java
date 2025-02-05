package com.shadoww.parserservice.util.instances;


//import com.shadoww.BookLibraryApp.model.image.ChapterImage;

import com.shadoww.api.util.texformatters.elements.TextElement;
import com.shadoww.api.util.texformatters.elements.TextElements;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChapterInstance {

    private String title = "";

//    private String text = "";
    private TextElements textElements = new TextElements();

    private int chapterNumber;

    private List<ImageInstance> images = new ArrayList<>();

//    public ChapterInstance(String title, String text, int chapterNumber) {
//        this.title = title;
//        this.text = text;
//        this.chapterNumber = chapterNumber;
//    }

    public void addTitle(String title) {
        if (!this.title.isEmpty()) {
            this.title += " " + title;
        } else {
            this.title += title;
        }
    }

    public void addTextElement(TextElement element) {
        if(!hasText(element)) {
            textElements.add(element);
        }
    }

    public void addAllTextElements(TextElements elements) {
        if (!elements.isEmpty()) {
            this.textElements.addAll(elements);
        }
    }

//    public void addText(String text) {
//        if (!this.hasText(text)) {
//            this.text += Objects.equals(this.text, "") ? text : "\n" + text;
//        }
//    }

    public void addChapterImage(ImageInstance image) {
        if (image != null) {

//            image.setFileName(images.size() + "_" + UUID.randomUUID() + ".jpeg");
            this.images.add(image);
        }
    }

//    private boolean hasText(String text) {
//        return this.text.endsWith(text);
//    }

    private boolean hasText(TextElement element) {
        return textElements.contains(element);
    }

    //    private String getParagraph(String text) {
//        return "<p>" + text + "</p>";
//    }
    public boolean isTitleEmpty() {
        return title == null || title.equals("");
    }

    public boolean isTextEmpty() {
        return textElements.isEmpty();
    }

    public boolean isEmpty() {
        return isTitleEmpty() && isTextEmpty();
    }

    public void addChapterInstance(ChapterInstance chapter) {
        this.textElements.addAll(chapter.getTextElements());
        this.images.addAll(chapter.getImages());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterInstance that = (ChapterInstance) o;
        return Objects.equals(title, that.title) && Objects.equals(textElements, that.textElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, textElements);
    }

    @Override
    public String toString() {
        return "ChapterInstance{" +
                "title='" + title + '\'' +
                ", text='{{\n" + textElements + "\n}}" +
                "}";
    }
}
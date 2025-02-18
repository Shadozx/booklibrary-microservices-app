package com.shadoww.parserservice.util.parser.parsers;

//import com.shadoww.BookLibraryApp.model.Book;
//import com.shadoww.BookLibraryApp.model.Chapter;
//import com.shadoww.BookLibraryApp.model.image.ChapterImage;

import com.shadoww.api.util.texformatters.TextFormatter;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.parserservice.util.instances.ImageInstance;
import com.shadoww.parserservice.util.parser.interfaces.*;
import com.shadoww.parserservice.util.parser.selectors.ChapterSelector;
import com.shadoww.parserservice.util.parser.selectors.ChapterSelectors;
import com.shadoww.api.util.texformatters.elements.TextElement;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.util.parser.statistics.ChapterParserStatistics;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ChapterParser {

    @Setter
    private BookInstance book;

    @Setter
    private ChapterSelectors chapterSelectors;

    private List<ChapterInstance> chapters;

    @Getter
    private final List<ImageInstance> chapterImages = new ArrayList<>();

    private String bookUrl;

    @Getter
    private ChapterParserStatistics statistics;


    public List<ChapterInstance> parse(String bookUrl, BookInstance book) throws IOException {

        this.book = book;
        this.bookUrl = bookUrl;

        statistics = new ChapterParserStatistics();

        List<String> links = getLinks(chapterSelectors);

        if (links != null && !links.isEmpty()) {

            Stack<ChapterInstance> chapterInstances = new Stack<>();
            for (String link : links) {

                Document page = ParserHelper.getDocument(link);

                if (chapterSelectors != null) {

                    // видаляємо не потрібні елементи
                    if (chapterSelectors.getDeleteElements() != null) {
                        page.select(String.join(", ", chapterSelectors.getDeleteElements())).remove();
                    }

                    Stack<ChapterInstance> parsedChapterInstances = getChapterInstances(chapterSelectors, page);

                    if (parsedChapterInstances != null) {
                        chapterInstances.addAll(parsedChapterInstances);

                        statistics.addPageParsed();
                    }
                }
            }

            return addNumber(formatChapters(chapterInstances));

//            this.chapters = addNumber(formatChapters.stream().filter(c->!c.isTextEmpty()).map(Cha::new).toList());

//            return this.chapters;
        }

        return List.of();
    }


    /**
     * книжка:
     * селектор глави
     * селектор опису
     * <p>
     * фотографія книжки:
     * селектор на фотографію
     * <p>
     * глави:
     * глава:
     * селектор на назву глави
     * селектор на текст
     * <p>
     * <p>
     * пишемо функцію яка дає силки на глави
     * <p>
     */

    //
    private List<String> getLinks(ChapterSelectors chapterSelectors) throws IOException {

        if (!chapterSelectors.isEmpty()) {
            for (ChapterSelector chapterSelector : chapterSelectors) {

                ChapterLinks chapterLinks = chapterSelector.getChapterLinks();

                if (chapterLinks == null) {
                    chapterLinks = chapterSelectors.getChapterLinks();

                }

                List<String> links = chapterLinks.getChapterLinks(bookUrl, ParserHelper.getDocument(bookUrl));

                if (links != null) return links;
            }


        }
        return null;
    }

    private Stack<ChapterInstance> getChapterInstances(ChapterSelectors chapterSelectors, Document page) {

        for (ChapterSelector chapterSelector : chapterSelectors) {

            Stack<ChapterInstance> chapterInstances = getPage(chapterSelector, page);

            if (chapterInstances != null) return chapterInstances;

        }

        return null;

    }

    private Stack<ChapterInstance> getPage(ChapterSelector chapterSelector, Document page) {
        String selector = chapterSelector.getSelector();

        if (selector != null) {
            Elements elements = page.select(selector);


            ChapterSelectorSwitcher switcher = chapterSelectors.getSwitcher();

            if (switcher != null) {
                if (switcher.switchSelector(elements)) return null;
            }

            ElementsFormatter elementsFormatter = chapterSelectors.getElementsFormatter();

            if (elementsFormatter != null) elements = elementsFormatter.format(elements);

            if (chapterSelector.getTextSelector() != null && !chapterSelector.getTextSelector().equals("")) {
                elements = elements.select(chapterSelector.getTextSelector());
            }

            FilterElements filterElements = chapterSelectors.getFilterElements();

            if (filterElements != null) {
                elements = elements.stream().filter(filterElements::filter).collect(Collectors.toCollection(Elements::new));
            }

            if (!elements.isEmpty()) {

                System.out.println("Page has elements: " + elements.size());

                Stack<ChapterInstance> chapterInstances = new Stack<>();


                for (Element el : elements) {

                    statistics.addElement(el);

                    if (chapterSelector.getTitles().contains(el.tagName() + (!el.className().equals("") ? ("." + el.className()) : ""))) {

                        TextConvector textConvector = chapterSelector.getTextConvector();
                        if (textConvector != null) {
                            textConvector.transform(chapterInstances, el);
                        } else {
                            ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();


                            addTitle(el, prev, chapterInstances);
                        }
                    } else {

                        if (chapterSelector.getParagraph() != null) {
                            ChapterInstance current = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                            TextElements textElements = chapterSelector.getParagraph().getParagraph(el, current, this.chapterImages, this.book);

                            if (textElements != null) current.addAllTextElements(textElements);

//                            if (el.tagName().equals("table")) {
//                                System.out.println(el);
//                            }

                            chapterInstances.push(current);
                        } else {
//                            System.out.println("Paragraph is empty...");

                            ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();

                            TextElements textElements = getParagraphText(el, prev);

                            prev.addAllTextElements(textElements);

                            chapterInstances.push(prev);
                        }
                    }
                }

                return chapterInstances;
            } else {
                System.out.println("Елементи тексту відсутні");
            }
        } else {
            List<String> titles = chapterSelector.getTitles();

            if (titles.size() == 1) {
                String selectorTitle = titles.get(0);
                Element elementTitle = page.select(selectorTitle).first();

                ChapterInstance current = new ChapterInstance();
                current.addTitle(elementTitle != null ? elementTitle.text() : "* * *");

                Elements elements = page.select(chapterSelector.getTextSelector());

                for (Element el : elements) {
                    Paragraph paragraph = chapterSelector.getParagraph();


                    TextElements textElements;
                    if (paragraph != null) {
                        textElements = paragraph.getParagraph(el, current, this.chapterImages, this.book);

                    } else {
                        textElements = getParagraphText(el, current);
                    }
                    if (textElements != null) current.addAllTextElements(textElements);
                }

                Stack<ChapterInstance> chapterInstances = new Stack<>();

                chapterInstances.push(current);
                return chapterInstances;
            }


        }

        return null;
    }

    private void addTitle(Element el, ChapterInstance prev, Stack<ChapterInstance> chapterInstances) {
        if (prev.isTextEmpty()) {
            prev.addTitle(el.text());

            chapterInstances.push(prev);
        }


        else if (prev.getTitle().equals("Примечания") || prev.getTitle().equals("Примітки") || el.text().matches("\\d+")) {

            prev.addTextElement(ParserHelper.formatText(el, ElementType.Paragraph));

            chapterInstances.push(prev);
        }

        // якщо існує глава, але має назву глави но немає тексту
        else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

            prev.addTitle(". " + el.text());
            chapterInstances.push(prev);

        } else {
            chapterInstances.push(prev);

            ChapterInstance current = new ChapterInstance();

            current.addTitle(el.text());

            chapterInstances.push(current);

        }

    }

    private TextElements getParagraphText(Element el, ChapterInstance current) {
        if (el.tagName().equals("img")) {
            ImageInstance image = ParserHelper.addChapterImage(el);

            current.addChapterImage(image);

            this.chapterImages.add(image);

            TextElement element = ParserHelper.formatText(el.attr("src", image.getFileName()), ElementType.Image);

            return new TextElements(List.of(element));
        } else {

//            if (el.tagName().equals("div") && el.className().equals("poem")) {
//                System.out.println(el);
//            }

            TextElement element = ParserHelper.formatText(el.text(el.text()), ElementType.Paragraph);


            return new TextElements(List.of(element));
        }
    }

    private List<ChapterInstance> formatChapters(List<ChapterInstance> chapters) {
        Stack<ChapterInstance> stack = new Stack<>();
        chapters = chapters.stream().filter(c -> !c.isEmpty()).toList();

        for (int i = 0; i < chapters.size(); i++) {
            ChapterInstance current = chapters.get(i);

            if (current != null) {
                if (i == 0 && current.isTitleEmpty()) {

                    current.addTitle("* * *");
                    stack.push(current);
                }
//                else if(current.isTextEmpty()) {
//                    ChapterInstance next = i < chapters.size() ? chapters.get(i+1) : null;
//                    if(next != null) {
//                        next.addPreTitle(current.getTitle());
//                    }
//
//                }

                else if (current.isTitleEmpty()) {
                    ChapterInstance prev = !stack.isEmpty() ? stack.pop() : new ChapterInstance();

                    prev.addChapterInstance(current);
                    stack.push(prev);
                } else if(current.isTextEmpty()) {
                    ChapterInstance prev = !stack.isEmpty() ? stack.pop() : new ChapterInstance();

                    prev.addTextElement(TextFormatter.parseToPattern(new Element("b").text(current.getTitle()), ElementType.Other));

                    stack.push(prev);
                }

                else stack.push(current);
            }
        }

        return stack;
    }


    private List<ChapterInstance> addNumber(List<ChapterInstance> chapters) {
        for (int i = 1; i <= chapters.size(); i++) {
            chapters.get(i - 1).setChapterNumber(i);
        }

        return chapters;
    }
}

/*
 *
 * Парсер:
 * селектор на кількість глав якщо такий є
 * <p>
 * список глав селекторів який буде йти по списку якщо один не дав хорошого результату
 * Глав селектор:
 * <p>
 */
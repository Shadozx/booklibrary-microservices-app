package com.shadoww.parserservice.util.readers;

import com.shadoww.parserservice.util.readers.elements.TBook;
import com.shadoww.parserservice.util.readers.elements.TImage;
import com.shadoww.parserservice.util.readers.elements.TSection;
import com.shadoww.parserservice.util.readers.elements.TText;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;

public class FB2Reader {

    public TBook parse(InputStream inputStream) throws IOException {

        return parseBook(Jsoup.parse(inputStream, "UTF-8", "", Parser.xmlParser()));
    }

    public TBook parse(String strB) {
        return parseBook(Jsoup.parse(strB, "", Parser.xmlParser()));
    }

    private TBook parseBook(Document doc) {

        Element titleInfo = doc.getElementsByTag("title-info").get(0);

        var book = getBook(titleInfo);

        if (book == null) return null;


        var sections = getSections(doc);
        book.setSections(sections);


        return book;
    }

    private TBook getBook(Element element) {

        Element bookTitle = element.selectFirst("book-title");


        Element annotation = element.selectFirst("annotation");


        return bookTitle != null && annotation != null ? new TBook(bookTitle.text(), annotation.text()) : null;
    }

    private Stack<TSection> getSections(Document doc) {
        Stack<TSection> sections = new Stack<>();

        var sectionsElements = doc.getElementsByTag("section");

        for (var sectionElement : sectionsElements) {
            for (var element : sectionElement.children().not("section")) {

                addSection(sections, doc, element);
            }
        }

        return sections;
    }

    private void addSection(Stack<TSection> sections, Document doc, Element element) {
        if (element.tagName().equals("title")) {

            var prev = !sections.isEmpty() ? sections.pop() : new TSection();


            // якщо існує глава але має назва глави но немає тексту
            if (!prev.isTitleEmpty() && prev.isTextEmpty()) {
                prev.addTitle(element.text().trim());
                sections.push(prev);
            } else {
                sections.push(prev);

                var current = new TSection();

                current.addTitle(element.text().trim());

                sections.push(current);
            }
        } else {

            if (element.tagName().equals("image")) {

                String href = element.attr("l:href");

                Element binaryElement = doc.getElementById(href.replace("#", ""));
                if (binaryElement != null) {

                    var prev = !sections.isEmpty() ? sections.pop() : new TSection();

                    String imageData = binaryElement.text();

                    byte[] data = decodeBase64(imageData);

//                            File file = new File("images/" + UUID.randomUUID() + ".png");
//
//                            if (!file.exists()) {
//                                try {
//                                    file.createNewFile();
//                                } catch (IOException e) {
//                                    System.out.println("Errro!");
//                                }
//                            }

//                            try (FileOutputStream writer = new FileOutputStream(file)) {
//                                writer.write(data);
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }

                    prev.addElement(new TImage(data));

                    sections.push(prev);
                }
            } else if (List.of("epigraph", "cite", "emphasis").contains(element.tagName())) {
                var prev = !sections.isEmpty() ? sections.pop() : new TSection();

                prev.addElement(new TText(element.tagName("i").text(element.text()).toString()));

                sections.push(prev);
            } else if (List.of("strong").contains(element.tagName())) {
                var prev = !sections.isEmpty() ? sections.pop() : new TSection();

                prev.addElement(new TText(element.tagName("b").text(element.text()).toString()));

                sections.push(prev);
            } else {
                var prev = !sections.isEmpty() ? sections.pop() : new TSection();

                prev.addElement(new TText(element.text().trim()));

                sections.push(prev);
            }
        }
    }

    private List<TSection> formatSections(List<TSection> sections) {
        Stack<TSection> stack = new Stack<>();


        for (int i = 0; i < sections.size(); i++) {
            var current = sections.get(i);

            if (current != null) {
                if (i == 0 && current.isTitleEmpty()) {

                    current.addTitle("* * *");
                    stack.push(current);
                } else if (current.isTitleEmpty()) {
                    var prev = !stack.isEmpty() ? stack.pop() : new TSection();


                    prev.addElements(current);
                    stack.push(prev);

                } else stack.push(current);
            }
        }

        return stack;
    }

    public static byte[] decodeBase64(String base64Data) {
        return java.util.Base64.getDecoder().decode(base64Data);
    }

}


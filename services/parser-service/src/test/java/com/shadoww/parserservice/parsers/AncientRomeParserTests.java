package com.shadoww.parserservice.parsers;

import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
import com.shadoww.parserservice.util.parser.parsers.ParserHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AncientRomeParserTests {

    @Test
    public void testAncientRomeParser() throws IOException {

        Parser parser = ParserFactory.createAncientRome();

        String url = "https://ancientrome.ru/antlitr/cicero/index-phil.htm";

//        System.out.println(parser.canParseBook(url) ? "парсер може парсити" : "не може");

        BookInstance instance = parser.parseBook(url);

        System.out.println(instance);

        List<ChapterInstance> chapterInstanceList = parser.parseChapters(url, instance);
        instance.setChapters(chapterInstanceList);
//
////        for (var c : chapterInstanceList.subList(0, 1)) {
////            System.out.println(c.getTitle());
////            System.out.println(c.getText());
////
////            System.out.println("============================\n");
////        }
//


//        BookInstance instance = new BookInstance();
//        instance.setTitle("test");
//        instance.setDescription("test");
//
//        instance.setChapters(List.of());

        String fileName = instance.getTitle().replaceAll(" ", "_") + ".txt";


        writeBookInstance(fileName, instance);
    }

    private void writeBookInstance(String fileName, BookInstance instance) {

        List<ChapterInstance> chapterInstanceList = instance.getChapters();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/books/" + fileName, true))) {

            for (var c : chapterInstanceList) {

                writer.newLine();
                writer.write(c.getTitle());
                writer.newLine();
                writer.newLine();
                writer.write(c.getTextElements().html());

                writer.write("============");
            }

            writer.newLine();
            System.out.println("Текст успішно додано до файлу: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAncientRomePage() throws IOException {

//        Document page = ParserHelper.getDocument("https://ancientrome.ru/antlitr/t.htm?a=1422278248");
//
//        page.select("script, noindex, .note, div.hr, a, div.otext span, sub, sup, br, wbr, div.ignored").remove();
//        Elements allElements = page.select("div.main > div.center");
//
//        Elements textElements = allElements.select("div.head > h1, div#selectable-content.text1 > div.otext > *");
//
//        System.out.println(textElements.size());

        Element element = Jsoup.parse("<h4><span>afafafaf</span></br><a name=\"0\"><span id=\"selection_index8\" class=\"selection_index\"></span>ВСТУПЛЕНИЕ</a></h4>")
                .select("h4")
                .select("a")
                .remove()
                .first();

//        System.out.println(ParserHelper.formatText(element, ElementType.Paragraph));
        System.out.println(element.text());
    }

    @Test
    public void testDownloadHtmlPage() throws IOException {
        Document doc = ParserHelper.getDocument("https://ancientrome.ru/antlitr/t.htm?a=1414890004");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/pages/" + "book_4.html", true))) {
            writer.write(doc.html());
        }
    }
}
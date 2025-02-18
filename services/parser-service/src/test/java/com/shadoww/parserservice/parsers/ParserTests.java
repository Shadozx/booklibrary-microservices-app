package com.shadoww.parserservice.parsers;

import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ParserTests {

    @Test
    public void parseBook() throws IOException {

        String url = "https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya";

        String host = new URL(url).getHost();

        Parser parser = ParserFactory.createParserForHost(host);

        BookInstance book = parser.parseBook(url);
        List<ChapterInstance> chapterInstanceList = parser.parseChapters(url, book);
        System.out.println(parser.getChapterParser().getStatistics());
    }
}

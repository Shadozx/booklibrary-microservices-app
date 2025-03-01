package com.shadoww.parserservice.parsers;

import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {

    @Test
    public void parseBook() throws IOException {

        String url = "https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya";

        String host = new URL(url).getHost();

        Parser parser = ParserFactory.createParserForHost(host);

        BookInstance book = parser.parseBook(url);
        parser.parseChapters(url, book);
        System.out.println(parser.getChapterParser().getStatistics());
    }

    private static Stream<Arguments> chaptersProvider() {
        return Stream.of(
//                Arguments.of("https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("https://coollib.xyz/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("https://coollib.in/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("https://coollib.cc/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("http://loveread.ec/view_global.php?id=67498", 34)//,
                Arguments.of("http://loveread.ec/view_global.php?id=6971", 13)
//                Arguments.of("https://librebook.me/the_prince/", 27)//,
//                Arguments.of("https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533/fulltext.htm", 53),
//                Arguments.of("http://flibusta.site/b/671697", 2)
//                Arguments.of("https://rulit.me/books/dolina-uzhasa-download-101113.html", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("chaptersProvider")
    public void testChapters(String url, int expectedAmountChapters) throws IOException {

        long start = System.currentTimeMillis();
        System.out.println(start);

        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());

        BookInstance book = createTestBook();
        List<ChapterInstance> chapters = parser.parseParallelChapters(url, book);

        System.out.println(parser.getChapterParser().getStatistics());
        System.out.println("Len chapters: " + chapters.size());

//        System.out.println("Chapters:");
//        chapters.forEach(c->{
//            System.out.println("Title:" + c.getTitle());
//            System.out.println("---------");
//
//        });

        long end = System.currentTimeMillis();
        System.out.println(end);
        System.out.printf("Time spent %d seconds\n", (end-start)/1000);

        assertEquals(expectedAmountChapters, chapters.size(), String.format("Кількість розділів книги не збігаються! Має бути '%s' а є - '%s'", expectedAmountChapters, chapters.size()));
    }


    private BookInstance createTestBook() {

        return new BookInstance(
                "test book",
                "test description"
        );
    }

}

package com.shadoww.parserservice.formatter;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.parserservice.service.RetryableLibraryService;
import com.shadoww.parserservice.util.formatters.BooksFormatter;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BooksFormatterTests {


    @Mock
    private RetryableLibraryService retryableLibraryService;

    @InjectMocks
    private BooksFormatter booksFormatter;

    @BeforeEach
    public void setUp() {

        when(retryableLibraryService.addAuthor(any(AuthorRequest.class))).thenReturn(createTestAuthorResponse());
        when(retryableLibraryService.addBook(any(BookRequest.class))).thenReturn(createTestBookResponse());
        when(retryableLibraryService.filterBooks(any(BookFilterRequest.class))).thenReturn(List.of(createTestBookResponse()));

    }

    private static Stream<Arguments> authorsBooksProvider() {
        return Stream.of(
                Arguments.of("http://loveread.ec/biography-author.php?author=Mark-Avreliy-Antonin", 2),
                Arguments.of("https://librebook.me/list/person/nikolo_makiavelli", 10),
                Arguments.of("http://flibusta.site/a/168916", 16),
                Arguments.of("https://coollib.net/a/112937-lutsiy-anney-seneka", 4)
        );
    }

    @ParameterizedTest
    @MethodSource("authorsBooksProvider")
    public void testAuthorsBooksCount(String url, int expectedBooksCount) throws IOException {

        when(retryableLibraryService.filterAuthors(any(AuthorRequest.class))).thenReturn(List.of());

        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());
        List<BookInstance> books = booksFormatter.parseAuthorBooks(parser, url);

        System.out.println(books.size());

        assertEquals(expectedBooksCount, books.size(), String.format("Кількість книг автора не збігаються! Має бути %s, а є - %s", expectedBooksCount, books.size()));
    }


    private static Stream<Arguments> chaptersProvider() {
        return Stream.of(
                Arguments.of("https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.xyz/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.in/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.cc/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("http://loveread.ec/view_global.php?id=67498", 34),
//                Arguments.of("https://librebook.me/the_prince/", 27),
//                Arguments.of("https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533/fulltext.htm", 53),
                Arguments.of("http://flibusta.site/b/671697", 2)
//                Arguments.of("https://rulit.me/books/dolina-uzhasa-download-101113.html", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("chaptersProvider")
    public void testChapters(String url, int expectedAmountChapters) throws IOException {
        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());

        BookInstance book = createTestBook();
        List<ChapterInstance> chapters = booksFormatter.parseBookChapters(parser, book, 10L, url);

        System.out.println(chapters.size());

        assertEquals(expectedAmountChapters, chapters.size(), String.format("Кількість розділів книги не збігаються! Має бути '%s' а є - '%s'", expectedAmountChapters, chapters.size()));
    }

    @Test
    public void testParseBooks() {
        List<String> urls = List.of(
                "http://flibusta.site/b/114549",
                "http://flibusta.site/b/121911",
                "http://flibusta.site/b/120337",
                "http://loveread.ec/view_global.php?id=6971",
                "http://loveread.ec/view_global.php?id=67498"
        );

        long start = System.currentTimeMillis();
        System.out.println(start);
        List<BookInstance> books = booksFormatter.parseBooks(urls);

        long end = System.currentTimeMillis();

        for(var b : books) {
            System.out.println(b.getTitle());
            System.out.println(b.getChapters().size());
        }

        System.out.println(end);
        System.out.printf("Time spent %d seconds\n", (end-start)/1000);
        Assertions.assertEquals(urls.size(), books.size(), "Books size must be " + urls.size());
    }

    private BookResponse createTestBookResponse() {

        return new BookResponse(
                10L,
                "test book",
                "test description",
                10,
                10L
        );
    }

    private BookInstance createTestBook() {

        return new BookInstance(
                "test book",
                "test description"
        );
    }

    private AuthorResponse createTestAuthorResponse() {

        return new AuthorResponse(
                10L,
                "test author name",
                "test biography"
        );
    }
}

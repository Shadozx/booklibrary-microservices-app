package com.shadoww.parserservice.formatter;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.parserservice.client.LibraryServiceClient;
import com.shadoww.parserservice.client.MediaServiceClient;
import com.shadoww.parserservice.service.RetryableLibraryService;
import com.shadoww.parserservice.util.formatters.BooksFormatter;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BooksFormatterTests {

//    @Mock
//    private LibraryServiceClient libraryServiceClient;

//    @Mock
//    private MediaServiceClient mediaServiceClient;

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



    private BookResponse createTestBookResponse() {

        return new BookResponse(
                10L,
                "test book",
                "test description",
                10,
                10L
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

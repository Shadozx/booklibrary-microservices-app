package com.shadoww.parserservice.util.formatters;


//import com.shadoww.BookLibraryApp.model.Author;
//import com.shadoww.BookLibraryApp.model.Book;
//import com.shadoww.BookLibraryApp.model.BookSeries;
//import com.shadoww.BookLibraryApp.model.Chapter;
//import com.shadoww.BookLibraryApp.model.image.BookImage;
//import com.shadoww.BookLibraryApp.model.image.ChapterImage;
//import com.shadoww.api.service.interfaces.*;

import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.api.util.texformatters.TextFormatter;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.client.BookServiceClient;
import com.shadoww.parserservice.client.ImageServiceClient;
import com.shadoww.parserservice.util.instances.*;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
//import org.springframework.beans.factory.annotation.Autowired;
import com.shadoww.parserservice.util.writers.BookFB2Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Component
public class BooksFormatter {

    private final BookServiceClient bookServiceClient;

    private final ImageServiceClient imageServiceClient;

    @Autowired
    public BooksFormatter(BookServiceClient bookServiceClient, ImageServiceClient imageServiceClient) {
        this.bookServiceClient = bookServiceClient;
        this.imageServiceClient = imageServiceClient;
    }


    public boolean format(String url) {

        if (url == null || Objects.equals(url, "")) {
            return false;
        }

        try {
            return parseUrl(url).size() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookInstance> parseUrl(String url) throws IOException {
        System.out.println("Start parsing url");

        URL u = new URL(url);

        Parser parser = ParserFactory.createParserForHost(u.getHost());

        System.out.println(u.getHost());

        List<BookInstance> books = new ArrayList<>();
        if (parser.canParseAuthorBooks(url)) {

            books.addAll(parseAuthorBooks(parser, url));
        } else if (parser.canParseBookSeriesBooks(url)) {

            books.addAll(parseBookSeriesBooks(parser, url));
        } else if (parser.canParseBook(url)) {

            // якщо вже колись була додана до бібліотеки книга(щоб не дублювати дані)
//            if (existsBookByUrl(url)) {
//                throw new ValueAlreadyExistsException("Книжка з таким посиланням вже існує!");
//            }


            books.add(parseFullBook(parser, url));
        } else {
            throw new IllegalArgumentException("Цей парсер нічого не підтримує");
        }

        System.out.println("End parsing url");

        return books;
    }

    private ByteArrayOutputStream parseToFb2(BookInstance bookInstance, List<ChapterInstance> chapters) throws ParserConfigurationException {

        BookFB2Writer writer = new BookFB2Writer();


        BookFB2Writer.DescriptionBuilder.TitleInfoBuilder titleInfo = writer.description()
                .titleInfo();

        if (bookInstance.getTitle() != null) {
            titleInfo.title(bookInstance.getTitle());
        }
        if (bookInstance.getDescription() != null) {
            titleInfo.annotation(bookInstance.getDescription());
        }

        if (bookInstance.getBookImage() != null) {

            titleInfo.coverImage(bookInstance.getBookImage().getData());


        }
        titleInfo.back()
                .back();

        var body = writer.body();
        for (var chapter : chapters) {

            var section = body.section();

            if (!chapter.isTitleEmpty()) {
                section.title(chapter.getTitle());
            }

//            int counterImages = 1;

            if (!chapter.isTextEmpty()) {

                List<ImageInstance> images = chapter.getImages();

                TextElements elements = chapter.getTextElements();

                for (var element : elements) {

//                    System.out.println(element);
                    if (element.hasType(ElementType.Paragraph)) {

                        section.paragraph(element.attr("text"));
                    } else if (element.hasType(ElementType.Image)) {


                        Optional<ImageInstance> foundImage = images.stream().filter(i -> i.getFileName().equals(element.attr("filename"))).findFirst();

                        if (foundImage.isPresent()) {
                            ImageInstance image = foundImage.get();
                            section.image(image.getFileName(), image.getData());
                        }
                    } else if (element.hasType(ElementType.Other)) {
                        section.paragraph(element.attr("text"));
                    }
                }
            }

            body = section.and();
        }

        body.back();

        System.out.println("End parsing url");


        return writer.build();
    }

    public ByteArrayOutputStream parseToFb2(String url) throws IOException, ParserConfigurationException {

        System.out.println("Start parsing url");

        URL u = new URL(url);

        Parser parser = ParserFactory.createParserForHost(u.getHost());

        System.out.println(u.getHost());

        if (parser.canParseBook(url)) {
            BookInstance bookInstance = parseBookDetails(parser, url);

            List<ChapterInstance> chapters = parser.parseChapters(url, bookInstance);

            return parseToFb2(bookInstance, chapters);

//            return new ByteArrayOutputStream();

        } else {
            throw new IllegalArgumentException("Цей парсер нічого не підтримує");
        }

    }

    public List<BookInstance> parseAuthorBooks(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseAuthorBooks(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг серій книг");
        }

        if (!parser.canParseAuthor(url)) {
            throw new IllegalArgumentException("Парсер не підтримує парсинг самого автора");
        }

        System.out.println("Start parse author");

        AuthorInstance parsedAuthor = parser.parseAuthor(url);

        System.out.println(parsedAuthor);

        System.out.println("End parse author");

        List<BookInstance> books = new ArrayList<>();

        // якщо вже колись був доданий до бібліотеки автор книг(щоб не дублювати дані)
//        if (existsAuthorByUrl(url)) {
//            throw new ValueAlreadyExistsException("Автор з таким посиланням вже існує!");
//        }

        List<String> urls = parser.parseBooksByAuthor(url);

        System.out.println("Start parse author books");
        for (var u : urls) {
            books.add(parseFullBook(parser, u));
        }

        System.out.println("End parse author books");

//            AuthorInstance author;

//            if (authorService.existsByName(parsedAuthor.getName())) {
//
//                author = authorService.readByName(parsedAuthor.getName());
//                author.addAllBooks(books);
//            } else {
//
//                parsedAuthor.setUploadedUrl(url);
//                parsedAuthor.addAllBooks(books);
//
//                author = authorService.create(parsedAuthor);
//            }

//            System.out.println(author);
//            books.forEach(book -> book.addAllAuthors(List.of(author)));

//            authorService.update(author);


        return books;
    }

    public List<BookInstance> parseBookSeriesBooks(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseBookSeriesBooks(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг серій книг");
        }

        List<BookInstance> books = new ArrayList<>();

        // якщо вже колись була додана до бібліотеки серія книг(щоб не дублювати дані)
//        if (existsBookSeriesByUrl(url)) {
//            throw new ValueAlreadyExistsException("Серія книг з таким посиланням вже існує!");
//        }

        List<String> urls = parser.parseBooksBySeries(url);

        for (var bookUrl : urls) {

            books.add(parseFullBook(parser, bookUrl));
        }

        if (parser.canParseBookSeries(url)) {
            BookSeriesInstance parsedBookSeries = parser.parseBookSeries(url);

//            BookSeries bookSeries;
//
//            if (bookSeriesService.existsByTitle(parsedBookSeries.getTitle())) {
//                bookSeries = bookSeriesService.readByTitle(parsedBookSeries.getTitle());
//
//                bookSeries.addAllBooks(books);
//
//            } else {
//                parsedBookSeries.setUploadedUrl(url);
//                parsedBookSeries.addAllBooks(books);
//
//                bookSeries = bookSeriesService.create(parsedBookSeries);
//            }

//            books.forEach(book -> book.addAllBookSeries(List.of(bookSeries)));

//            bookSeriesService.update(bookSeries);
        }

        return books;
    }

    public BookInstance parseFullBook(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);
//        if (existsBookByUrl(url)) {
//            return bookService.getByUrl(url);
//        }

        System.out.println("Start parse book details");
        BookInstance instance = parseBookDetails(parser, url);
        ImageInstance bookImage = instance.getBookImage();

        System.out.println(instance);
        System.out.println("End parse book details");

        if (existBookByTitle(instance.getTitle())) {

            BookFilterRequest request = new BookFilterRequest();
            request.setSearchText(instance.getTitle());

            System.out.println("Start finding same books");
            List<BookResponse> books = bookServiceClient.filterBooks(request);

            System.out.println(books);
            System.out.println("End finding same books");
            if(!books.isEmpty()) {
                return mapToInstance(books.get(0));
            }

        }

//        if (existsBookByTitle(book.getTitle())) {
//            return bookService.getByTitle(book.getTitle());
//        }


//        book = bookService.create(book);

        BookResponse bookResponse = bookServiceClient.addBook(mapToRequest(instance));

        if (bookImage != null) {

            bookImage.setFileName(bookResponse.getId() + ".png");
            ImageRequest imageRequest = mapToRequest(bookImage);

            System.out.println("Start adding book image");
            ImageResponse imageResponse = imageServiceClient.addBookImage(bookResponse.getId(), imageRequest);

            System.out.println(imageResponse);
            System.out.println("End adding book image");

            BookRequest bookRequest = mapToRequest(mapToInstance(bookResponse));
            bookRequest.setImageId(imageResponse.getId());

            System.out.println("Start updating book");
            System.out.println(bookServiceClient.updateBook(bookResponse.getId(), bookRequest));

            System.out.println("End updating book");
//            instance.setBookImage(bookImage);
//            bookImage.setBook(book);

//            imageService.create(bookImage);
        }

        parseBookChapters(parser, instance, bookResponse.getId(), url);

        return instance;
    }

    public BookInstance parseBookDetails(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseBook(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг книг");
        }

        BookInstance book = parser.parseBook(url);

        if (book == null) {
            throw new RuntimeException("Сталася помилка при парсингу книги");
        }

        if (parser.canParseBookImage()) {

            System.out.println("Cover image url:" + url);

            book.setBookImage(parser.parseBookImage(url));

        }

        return book;
    }

    public List<ChapterInstance> parseBookChapters(Parser parser,
                                                   BookInstance book,
                                                   long bookId,
                                                   String url) throws IOException {

        checkIsParserNull(parser);

        System.out.println("Start parse chapters");
        List<ChapterInstance> chapters = parser.parseChapters(url, book);

//        for (var c : chapters) {
//            System.out.println(c);
//        }
        System.out.println("End parse chapters");
//        List<ImageInstance> images = parser.getImages();


        if (chapters != null) {
            book.setAmount(chapters.size());

            int chapterNumber = 1;
            for (var chapter : chapters) {
//                chapter.setBook(book);

                chapter.setChapterNumber(chapterNumber);

//                chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());

                List<ImageInstance> chapterImages = chapter.getImages();

                for(var i : chapterImages) {
                    i.setFileName(bookId + "_" + UUID.randomUUID() + ".jpeg");
                }

                ChapterResponse response = bookServiceClient.addChapter(bookId, mapToRequest(chapter));

                for (var i : chapterImages) {
                    imageServiceClient.addChapterImage(bookId, response.getId(), mapToRequest(i));
                }

                chapterNumber++;
            }

            return chapters;
        }

        return null;
    }

    private boolean existBookByTitle(String title) {
        BookFilterRequest request = new BookFilterRequest();
        request.setSearchText(title);
        return !bookServiceClient.filterBooks(request).isEmpty();
    }

    private void checkIsParserNull(Parser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("Парсер для стягування книг не повинен бути пустим!");
        }
    }

    private BookRequest mapToRequest(BookInstance instance) {

        BookRequest request = new BookRequest();

        request.setTitle(instance.getTitle());
        request.setDescription(instance.getDescription());
//        request.setBookImage(instance.getBookImage().getFileName());

        return request;
    }

    private ChapterRequest mapToRequest(ChapterInstance instance) {

        ChapterRequest request = new ChapterRequest();

        request.setTitle(instance.getTitle());
        request.setText(instance.getTextElements().toPatternText());
        request.setChapterNumber(instance.getChapterNumber());

        return request;
    }

    private ImageRequest mapToRequest(ImageInstance instance) {

        ImageRequest request = new ImageRequest();

        request.setFileName(instance.getFileName());
        request.setData(instance.getData());

        return request;
    }

    private BookInstance mapToInstance(BookResponse response) {
        BookInstance instance = new BookInstance();

        instance.setTitle(response.getTitle());
        instance.setDescription(response.getDescription());
        instance.setAmount(response.getAmount());

        return instance;
    }

    private ChapterInstance mapToInstance(ChapterResponse response) {

        ChapterInstance instance = new ChapterInstance();

        instance.setTitle(response.getTitle());
//        instance.setText(response.getText());
        instance.setTextElements(TextFormatter.parsePatterns(response.getText()));
        instance.setChapterNumber(response.getChapterNumber());

        return instance;
    }

    private ImageInstance mapToInstance(ImageResponse response) {

        ImageInstance instance = new ImageInstance();

        instance.setFileName(response.getFileName());

        return instance;
    }
}
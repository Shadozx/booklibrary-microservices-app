package com.shadoww.parserservice.util.formatters;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.request.BookSeriesRequest;
import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.*;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.api.util.texformatters.TextFormatter;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.service.RetryableLibraryService;
import com.shadoww.parserservice.service.RetryableMediaService;
import com.shadoww.parserservice.util.instances.*;
import com.shadoww.parserservice.util.parser.factories.ParserFactory;
import com.shadoww.parserservice.util.parser.parsers.Parser;
import com.shadoww.parserservice.util.writers.BookFB2Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class BooksFormatter {

    private final RetryableLibraryService retryableLibraryService;
    private final RetryableMediaService retryableMediaService;

    @Autowired
    public BooksFormatter(RetryableLibraryService retryableLibraryService, RetryableMediaService retryableMediaService) {
        this.retryableLibraryService = retryableLibraryService;
        this.retryableMediaService = retryableMediaService;
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

    public ByteArrayOutputStream parseToFb2(BookInstance bookInstance, List<ChapterInstance> chapters) throws ParserConfigurationException {

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

            if (parser.canParseBookImage()) {
                bookInstance.setBookImage(parser.parseBookImage(url));
            }

            List<ChapterInstance> chapters = parser.parseChapters(url, bookInstance);

            return parseToFb2(bookInstance, chapters);

//            return new ByteArrayOutputStream();

        } else {
            throw new IllegalArgumentException("Цей парсер нічого не підтримує");
        }

    }

    public ByteArrayOutputStream parseMultipleBooksToFb2Combined(List<String> urls)
            throws IOException, ParserConfigurationException {

        List<BookInstance> books = new ArrayList<>();

        // Обробляємо кожен URL лише один раз
        for (String url : urls) {

            URL u = new URL(url);
            Parser parser = ParserFactory.createParserForHost(u.getHost());
            if (!parser.canParseBook(url)) {
                System.out.println("Пропускаємо URL (не підтримується парсинг книги): " + url);
                continue;
            }
            // Парсимо базові дані книги (назва, опис)
            BookInstance bookInstance = parseBookDetails(parser, url);
            if (parser.canParseBookImage()) {
                bookInstance.setBookImage(parser.parseBookImage(url));
            }
            // Отримуємо розділи книги та зберігаємо їх у полі chapters
            List<ChapterInstance> chapters = parser.parseChapters(url, bookInstance);
            bookInstance.setChapters(chapters);

            System.out.println("Title:" + bookInstance.getTitle());
            System.out.println(parser.getChapterParser().getStatistics());

            books.add(bookInstance);
        }

        // Ініціалізуємо FB2 writer
        BookFB2Writer writer = new BookFB2Writer();

        String annotation = "Цей файл містить декілька книг, зібраних в один fb2 файл.";
        // Додаємо до анотації опис кожної книги
        annotation += "\n" + books.stream()
                .map(BookInstance::getDescription)
                .collect(Collectors.joining("\n"));
        System.out.println("Annotation: " + annotation);
        // Загальний опис для комбінованого файлу
        BookFB2Writer.DescriptionBuilder.TitleInfoBuilder titleInfo = writer.description().titleInfo();
        // Генеруємо title із назв книг у потрібному форматі
        titleInfo.title(generateTitleInfo(books));
        titleInfo.annotation(annotation);

        Optional<ImageInstance> fb2BookCover = books.stream().map(BookInstance::getBookImage).filter(Objects::nonNull).findFirst();

        if (fb2BookCover.isPresent()) {
            titleInfo.coverImage(fb2BookCover.get().getData());
        }

        titleInfo.back().back();
        writer.description().back();

        // Отримуємо головний блок для всіх книг
        var body = writer.body();

        // Перший блок: індекс книг із назвами та описами
        var indexSection = body.section();
        indexSection.title("Індекс книг");
        for (BookInstance book : books) {
            indexSection.paragraph(book.getTitle());
            if (book.getBookImage() != null) {
                indexSection.image(book.getBookImage().getFileName(), book.getBookImage().getData());
            }
            indexSection.paragraph(book.getDescription());
            indexSection.paragraph("---------------------------------");
        }
        body = indexSection.and();

        // Другий блок: детальний вміст кожної книги
        for (BookInstance book : books) {
            var bookSection = body.section();
//             Додаємо назву книги
            bookSection.title(book.getTitle());
            // Додаємо опис книги, якщо він є

            if (book.getBookImage() != null) {
                bookSection.image(book.getBookImage().getFileName() + 1, book.getBookImage().getData());
            }

            if (book.getDescription() != null && !book.getDescription().isEmpty()) {
                bookSection.paragraph(book.getDescription());
            }

            body = bookSection.and();

            List<ChapterInstance> chapters = book.getChapters();
            if (chapters != null) {
                formatFb2Chapters(body, chapters);
            }
        }

        body.back();

        return writer.build();
    }

    private String generateTitleInfo(Collection<BookInstance> books) {
        return "Колекція книг: " + books.stream()
                .map(BookInstance::getTitle)
                .collect(Collectors.joining(" | "));
    }

    private void formatFb2Chapters(BookFB2Writer.BodyBuilder body, List<ChapterInstance> chapters) throws ParserConfigurationException {
        for (var chapter : chapters) {

            var section = body.section();

            if (!chapter.isTitleEmpty()) {
                section.title(chapter.getTitle());
            }

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

    }

    public List<BookInstance> parseBooks(List<String> urls) {
        System.out.println("Available processors for parsing books: " + Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Callable<BookInstance>> tasks = urls.stream().map(u-> (Callable<BookInstance>)() -> parseBook(u)).toList();

        List<BookInstance> results = new ArrayList<>();

        try {
            List<Future<BookInstance>> futures = executor.invokeAll(tasks);

            for (Future<BookInstance> future : futures) {
                try {
                    BookInstance book = future.get();
                    results.add(book); // Додаємо тільки успішний результат
                } catch (ExecutionException e) {
                    System.err.println("Помилка під час обробки книги: " + e.getCause());
                    // Зупиняємо всі інші задачі
                    futures.forEach(f -> f.cancel(true));
                    break; // Вихід з циклу при помилці
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Обробку перервано");
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdownNow(); // Завершення потоків
        }

//        for(var u : urls) {
//            try {
//                results.add(parseBook(u));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        return results;

    }

    private BookInstance parseBook(String url) throws IOException {
        URL u = new URL(url);
        Parser parser = ParserFactory.createParserForHost(u.getHost());
        if (!parser.canParseBook(url)) {
            System.out.println("Пропускаємо URL (не підтримується парсинг книги): " + url);
            throw new RuntimeException("Парсер не підтримує парсинг книг");
        }
        // Парсимо базові дані книги (назва, опис)
        BookInstance bookInstance = parseBookDetails(parser, url);
        if (parser.canParseBookImage()) {
            bookInstance.setBookImage(parser.parseBookImage(url));
        }
        // Отримуємо розділи книги та зберігаємо їх у полі chapters
        List<ChapterInstance> chapters = parser.parseParallelChapters(url, bookInstance);
        bookInstance.setChapters(chapters);

        System.out.println("Title:" + bookInstance.getTitle());
        System.out.println(parser.getChapterParser().getStatistics());

        return bookInstance;
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

        AuthorRequest request = new AuthorRequest();
        request.setName(parsedAuthor.getName());

        List<AuthorResponse> authors = retryableLibraryService.filterAuthors(request);

        if (!authors.stream().filter(a -> a.getName().equals(parsedAuthor.getName())).toList().isEmpty()) {
            throw new ValueAlreadyExistsException("Такий автор вже існує!");
        }

        List<String> urls = parser.parseBooksByAuthor(url);

        System.out.println("Start parse author books");
        for (var u : urls) {
            books.add(parseFullBook(parser, u));
        }

        System.out.println("End parse author books");


        Optional<AuthorResponse> foundAuthor = authors.stream().filter(a -> a.getName().equals(parsedAuthor.getName())).findFirst();

        AuthorResponse authorResponse;

        AuthorInstance author;

        if (foundAuthor.isPresent()) {

            authorResponse = foundAuthor.get();
            author = mapToInstance(foundAuthor.get());
            author.addAllBooks(books);
        } else {

            parsedAuthor.addAllBooks(books);

            authorResponse = retryableLibraryService.addAuthor(mapToRequest(parsedAuthor));
            author = mapToInstance(authorResponse);
        }

        System.out.println(author);
//        books.forEach(book -> book.addAllAuthors(List.of(author)));

//        retryableLibraryService.updateAuthor(authorResponse.getId(), mapToRequest(author));

        for (var book : books) {
            BookFilterRequest bookRequest = new BookFilterRequest();
            bookRequest.setSearchText(book.getTitle());
            Optional<BookResponse> bookResponse = retryableLibraryService.filterBooks(bookRequest).stream().filter(b -> b.getTitle().equals(book.getTitle())).findFirst();

            if (bookResponse.isPresent() && retryableLibraryService.getAuthorBooks(authorResponse.getId()).stream().noneMatch(b -> b.getTitle().equals(book.getTitle()))) {
                retryableLibraryService.addAuthorToBook(bookResponse.get().getId(), authorResponse.getId());
                System.out.printf("Author with id - %s added to book with id %s%n", authorResponse.getId(), bookResponse.get().getId());
            } else {
                System.out.printf("Book with title - %s%n", book.getTitle());
            }
        }

        return books;
    }

    public List<BookInstance> parseBookSeriesBooks(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseBookSeriesBooks(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг серій книг");
        }

        if (!parser.canParseBookSeries(url)) {
            throw new IllegalArgumentException("Парсер не підтримує парсинг серій книг");
        }

        System.out.println("Start parse bookseries");

        BookSeriesInstance parsedSeries = parser.parseBookSeries(url);

        System.out.println(parsedSeries);

        System.out.println("End parse bookseries");

        List<BookInstance> books = new ArrayList<>();

        // якщо вже колись був доданий до бібліотеки автор книг(щоб не дублювати дані)

        BookSeriesRequest request = new BookSeriesRequest();
        request.setTitle(parsedSeries.getTitle());

        List<BookSeriesResponse> series = retryableLibraryService.filterBookSeries(request);

        if (!series.stream().filter(a -> a.getTitle().equals(parsedSeries.getTitle())).toList().isEmpty()) {
            throw new ValueAlreadyExistsException("Така серія книг вже існує!");
        }

        List<String> urls = parser.parseBooksBySeries(url);

        System.out.println("Start parse bookseries books");
        for (var u : urls) {
            books.add(parseFullBook(parser, u));
        }

        System.out.println("End parse bookseries books");

        Optional<BookSeriesResponse> foundSeries = series.stream().filter(a -> a.getTitle().equals(parsedSeries.getTitle())).findFirst();

        BookSeriesResponse seriesResponse;

        BookSeriesInstance bookSeries;

        if (foundSeries.isPresent()) {
            seriesResponse = foundSeries.get();
            bookSeries = mapToInstance(foundSeries.get());
            bookSeries.addAllBooks(books);
        } else {
            parsedSeries.addAllBooks(books);

            seriesResponse = retryableLibraryService.addBookSeries(mapToRequest(parsedSeries));
            bookSeries = mapToInstance(seriesResponse);
        }

        System.out.println(bookSeries);

        for (var book : books) {
            BookFilterRequest bookRequest = new BookFilterRequest();
            bookRequest.setSearchText(book.getTitle());
            Optional<BookResponse> bookResponse = retryableLibraryService.filterBooks(bookRequest).stream().filter(b -> b.getTitle().equals(book.getTitle())).findFirst();

            if (bookResponse.isPresent() && !retryableLibraryService.getBookSeriesBooks(seriesResponse.getId()).stream().anyMatch(b -> b.getTitle().equals(book.getTitle()))) {
                retryableLibraryService.addBookSeriesToBook(bookResponse.get().getId(), seriesResponse.getId());
                System.out.printf("BookSeries with id - %s added to book with id %s%n", seriesResponse.getId(), bookResponse.get().getId());
            } else {
                System.out.printf("Book with title - %s%n", book.getTitle());
            }
        }

        return books;
    }

    public BookInstance parseFullBook(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        System.out.println("Start parse book details");
        BookInstance instance = parseBookDetails(parser, url);

        System.out.println(instance);
        System.out.println("End parse book details");

        if (existBookByTitle(instance.getTitle())) {

            BookFilterRequest request = new BookFilterRequest();
            request.setSearchText(instance.getTitle());

            System.out.println("Start finding same books");
            List<BookResponse> books = retryableLibraryService.filterBooks(request);

            System.out.println(books);
            System.out.println("End finding same books");
            Optional<BookResponse> foundBook = books.stream().filter(b -> b.getTitle().equals(instance.getTitle())).findFirst();
            if (foundBook.isPresent()) {
                return mapToInstance(foundBook.get());
            }

        }

        ImageInstance bookImage = null;

        if (parser.canParseBookImage()) {

            System.out.println("Cover image url:" + url);

            bookImage = parser.parseBookImage(url);

            instance.setBookImage(bookImage);
        }

        BookResponse bookResponse = retryableLibraryService.addBook(mapToRequest(instance));

        if (bookImage != null) {

            bookImage.setFileName(bookResponse.getId() + ".png");
            ImageRequest imageRequest = mapToRequest(bookImage);

            System.out.println("Start adding book image");
            ImageResponse imageResponse = retryableMediaService.addBookImage(bookResponse.getId(), imageRequest);

//            System.out.println(imageResponse);
            System.out.println("End adding book image");

            BookRequest bookRequest = mapToRequest(mapToInstance(bookResponse));
            bookRequest.setImageId(imageResponse.getId());

            System.out.println("Start updating book");
            retryableLibraryService.updateBook(bookResponse.getId(), bookRequest);

            System.out.println("End updating book");
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

        return book;
    }

    public List<ChapterInstance> parseBookChapters(Parser parser,
                                                   BookInstance book,
                                                   long bookId,
                                                   String url) throws IOException {

        checkIsParserNull(parser);

        System.out.println("Start parse chapters");
        List<ChapterInstance> chapters = parser.parseChapters(url, book);
        System.out.println("End parse chapters");

        System.out.println(parser.getChapterParser().getStatistics());

        if (chapters == null || chapters.isEmpty()) {
            return null;
        }

        book.setAmount(chapters.size());

        List<Long> addedChapters = new ArrayList<>();
        Map<Long, List<Long>> addedImages = new HashMap<>(); // bookId -> list of image IDs
        int chapterNumber = 1;

        try {
            for (var chapter : chapters) {
                chapter.setChapterNumber(chapterNumber);

                // Формуємо унікальні імена для зображень
                List<ImageInstance> chapterImages = chapter.getImages();
                for (var i : chapterImages) {
                    i.setFileName(bookId + "_" + UUID.randomUUID() + ".jpeg");
                }

                // 🔄 Додаємо розділ з повторними спробами
                ChapterResponse response = retryableLibraryService.addChapter(bookId, mapToRequest(chapter));
                addedChapters.add(response.getId());

                List<Long> imageIds = new ArrayList<>();
                for (var i : chapterImages) {
                    try {
                        Long imageId = retryableMediaService.addChapterImage(bookId, response.getId(), mapToRequest(i)).getId();
                        imageIds.add(imageId);
                    } catch (Exception e) {
                        System.err.println("❌ Не вдалося додати зображення для розділу: " + response.getId());
                        rollback(bookId, addedChapters, addedImages);
                        throw new RuntimeException("🚨 Помилка при додаванні зображення, всі зміни скасовані.");
                    }
                }

                addedImages.put(response.getId(), imageIds);
                chapterNumber++;
            }

            return chapters;

        } catch (Exception e) {
            rollback(bookId, addedChapters, addedImages);
            throw new RuntimeException("🚨 Помилка при обробці книги, всі зміни скасовані.", e);
        }
    }


    // 🔄 Rollback у разі помилки
    private void rollback(long bookId, List<Long> chapterIds, Map<Long, List<Long>> imageIdsMap) {
        for (Map.Entry<Long, List<Long>> entry : imageIdsMap.entrySet()) {
            List<Long> imageIds = entry.getValue();

            for (Long imageId : imageIds) {
                try {
                    retryableMediaService.deleteImageById(imageId);
                } catch (Exception e) {
                    System.err.println("⚠️ Неможливо видалити зображення: " + imageId);
                }
            }
        }

        for (Long chapterId : chapterIds) {
            try {
                retryableLibraryService.deleteChapter(bookId, chapterId);
            } catch (Exception e) {
                System.err.println("⚠️ Неможливо видалити розділ: " + chapterId);
            }
        }

        retryableLibraryService.deleteBook(bookId);


        System.err.println("🔄 Всі зміни скасовані.");
    }


    private boolean existBookByTitle(String title) {
        BookFilterRequest request = new BookFilterRequest();
        request.setSearchText(title);
        return !retryableLibraryService.filterBooks(request).isEmpty();
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
        request.setPatternText(instance.getTextElements().toPatternText());
        request.setChapterNumber(instance.getChapterNumber());

//        System.out.println(request);

        return request;
    }

    private ImageRequest mapToRequest(ImageInstance instance) {

        ImageRequest request = new ImageRequest();

        request.setFileName(instance.getFileName());
        request.setData(instance.getData());

        return request;
    }

    private AuthorRequest mapToRequest(AuthorInstance instance) {

        AuthorRequest request = new AuthorRequest();

        request.setName(instance.getName());
        request.setBiography(instance.getBiography());

        return request;
    }

    private BookSeriesRequest mapToRequest(BookSeriesInstance instance) {

        BookSeriesRequest request = new BookSeriesRequest();

        request.setTitle(instance.getTitle());

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

    private AuthorInstance mapToInstance(AuthorResponse response) {
        AuthorInstance instance = new AuthorInstance();

        instance.setName(response.getName());
        instance.setBiography(response.getBiography());

        return instance;
    }

    private BookSeriesInstance mapToInstance(BookSeriesResponse response) {
        BookSeriesInstance instance = new BookSeriesInstance();

        instance.setTitle(response.getTitle());


        return instance;
    }
}
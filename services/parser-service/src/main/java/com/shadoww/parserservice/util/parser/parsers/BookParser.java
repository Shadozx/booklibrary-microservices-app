package com.shadoww.parserservice.util.parser.parsers;

//import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.parserservice.util.instances.BookInstance;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;


@Setter
@Getter
public class BookParser {

    private String titleSelector;

    private String descriptionSelector;


    private List<String> matchers;

    public boolean canParseBook(String bookUrl) {

        for (var matcher : matchers) {

            if (bookUrl.matches(".+" + matcher)) {
                return true;
            }
        }

        return false;
    }

    public BookInstance parse(String url) throws IOException {
        Document main = ParserHelper.getDocument(url);

        Element title = main.select(titleSelector).first();
        Element description = main.select(descriptionSelector).first();

        if (title == null) {
            throw new IllegalArgumentException("Книга повинна мати назву!");
        }

        BookInstance book = new BookInstance();

        book.setTitle(title.text());

        if (description != null) {
            book.setDescription(description.text());
        }

        return book;
    }
}

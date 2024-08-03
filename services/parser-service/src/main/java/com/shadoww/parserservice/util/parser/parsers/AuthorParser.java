package com.shadoww.parserservice.util.parser.parsers;

//import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.parserservice.util.instances.AuthorInstance;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

@Setter
public class AuthorParser {

    private AuthorInformationSelector authorInformationSelector;

    @Getter
    private String matcher;

    private List<String> deleteElements;

    public boolean canParseAuthor(String authorUrl) {

        return matcher != null && authorUrl.matches(".+" + matcher);
    }

    public AuthorInstance parseAuthor(String authorUrl) throws IOException {
        Document authorPage = ParserHelper.getDocument(authorUrl);

        if (deleteElements != null) {
            for (var e : deleteElements) {
                authorPage.select(e).remove();
            }
        }

        Element name = authorPage.select(authorInformationSelector.getNameSelector()).first();

        AuthorInstance author = new AuthorInstance();
        if (authorInformationSelector.getBiographySelector() != null) {
            Element biography = authorPage.select(authorInformationSelector.getBiographySelector()).first();
            if (biography != null) {
                author.setBiography(biography.text());
            }
        }

        if (name == null) {
            throw new IllegalArgumentException("Автор не може бути без імені!");
        }

        author.setName(name.text());

        return author;

    }

    public void setNameSelector(String nameSelector) {
        if (authorInformationSelector == null) {
            authorInformationSelector = new AuthorInformationSelector();
        }

        authorInformationSelector.setNameSelector(nameSelector);
    }


    public void setBiographySelector(String biographySelector) {
        if (authorInformationSelector == null) {
            authorInformationSelector = new AuthorInformationSelector();
        }

        authorInformationSelector.setBiographySelector(biographySelector);
    }


    @Setter
    @Getter
    private static class AuthorInformationSelector {
        private String nameSelector;
        private String biographySelector;
    }
}
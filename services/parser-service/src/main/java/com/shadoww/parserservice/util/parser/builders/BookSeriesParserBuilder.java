package com.shadoww.parserservice.util.parser.builders;

import com.shadoww.parserservice.util.parser.parsers.BookSeriesParser;
import lombok.Getter;

import java.util.Arrays;

public class BookSeriesParserBuilder {

    @Getter
    private BookSeriesParser bookSeriesParser;
    private ParserBuilder parserBuilder;

    public BookSeriesParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        bookSeriesParser = new BookSeriesParser();
    }

    public BookSeriesParserBuilder nameSelector(String selector) {
        bookSeriesParser.setNameSelector(selector);

        return this;
    }

    public BookSeriesParserBuilder authorNameSelector(String selector) {
        bookSeriesParser.setAuthorNameSelector(selector);

        return this;
    }

    public BookSeriesParserBuilder matcher(String match) {
        bookSeriesParser.setMatcher(match);

        return this;
    }

    public BookSeriesParserBuilder deleteSeriesElements(String... elements) {
        bookSeriesParser.setDeleteElements(Arrays.asList(elements));

        return this;
    }

    public ParserBuilder and() {

        return parserBuilder;
    }
}

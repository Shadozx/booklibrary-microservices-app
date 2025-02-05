package com.shadoww.parserservice.util.parser.parsers;


import com.shadoww.parserservice.util.instances.ImageInstance;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


public class BookImageParser {

    @Setter
    private String bookImageSelector;


    public ImageInstance parse(String url) throws IOException {
        Document image = ParserHelper.getDocument(url);

        Element elementLink = image.select(bookImageSelector).first();

        if (elementLink != null) {

            if (elementLink.tagName().equals("img")) {

                String imageUrl = elementLink.absUrl("src");
                return ParserHelper.parseImage(imageUrl);


            } else if (elementLink.tagName().equals("a")) {
                String imageUrl = elementLink.absUrl("href");
                return ParserHelper.parseImage(imageUrl);

            }
        }
        return null;
    }
}

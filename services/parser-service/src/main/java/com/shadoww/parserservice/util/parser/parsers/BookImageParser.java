package com.shadoww.parserservice.util.parser.parsers;

//import com.shadoww.parserservice.model.image.BookImage;
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

        ImageInstance bookImage = new ImageInstance();

        if (elementLink != null) {

            if (elementLink.tagName().equals("img")) {

                String imageUrl = elementLink.absUrl("src");
                bookImage = ParserHelper.parseImage(imageUrl);


                return bookImage;
            } else if (elementLink.tagName().equals("a")) {
                String imageUrl = elementLink.absUrl("href");
                bookImage = ParserHelper.parseImage(imageUrl);

                return bookImage;
            }

        }
        return null;
    }


}

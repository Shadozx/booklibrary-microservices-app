package com.shadoww.parserservice.util.parser.interfaces;

//import com.shadoww.BookLibraryApp.model.Book;
//import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.parserservice.util.instances.ImageInstance;
import org.jsoup.nodes.Element;

import java.util.List;

public interface Paragraph {

    TextElements getParagraph(Element element, ChapterInstance current, List<ImageInstance> chapterImages, BookInstance book);
}

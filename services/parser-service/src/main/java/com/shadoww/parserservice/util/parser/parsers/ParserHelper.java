package com.shadoww.parserservice.util.parser.parsers;

//import com.shadoww.BookLibraryApp.model.Book;
//import com.shadoww.BookLibraryApp.model.Chapter;
//import com.shadoww.BookLibraryApp.model.image.BookImage;
//import com.shadoww.BookLibraryApp.model.image.ChapterImage;
//import com.shadoww.BookLibraryApp.model.image.Image;
import com.shadoww.parserservice.util.instances.ChapterInstance;
import com.shadoww.api.util.texformatters.TextFormatter;
import com.shadoww.api.util.texformatters.elements.TextElement;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.util.instances.BookInstance;
import com.shadoww.parserservice.util.instances.ImageInstance;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserHelper {


    private ParserHelper(){}


    public static Document getDocument(String url) throws IOException {
        return Jsoup
                .connect(url)
                .header("Accept-Encoding", "gzip, deflate")
//                    .userAgent("Mozilla")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(60_0000)
                .get();
    }

//    public List<Document> getDocuments(List<String> urls) throws IOException {
//        return urls.stream().map(u -> getDocument(u)).toList();
//    }
//

    public static ImageInstance parseImage(String url) throws IOException {

        System.out.println(url);
        ImageInstance image = new ImageInstance();
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();

        image.setContentType("image/jpeg");

        image.setData(response.bodyAsBytes());

        return image;
    }

    public static List<ChapterInstance> addNumber(List<ChapterInstance> chapters) {
        for(int i = 1; i <= chapters.size(); i++) {
            chapters.get(i-1).setChapterNumber(i);
        }

        return chapters;
    }

    //    Image addChapterImage(Element el, List<Image> images, Book book) {
    public static ImageInstance addChapterImage(Element el, BookInstance book) {
        try {
            String src = el.absUrl("src");
            if (src.startsWith("http")) {
                ImageInstance image = new ImageInstance(parseImage(src));

                if (book != null) {


//                    image.setContentType("image/jpeg");
//                    image.setChapterImage(book);
                    book.setBookImage(image);
//                            image.setFilename(this.book.getId() + "_" + (this.images.size() + 1) + ".jpeg");

                    return image;
                }
            }else {
                Matcher matcher = Pattern.compile("base64,(?<data>.+)").matcher(src);

                ImageInstance image = new ImageInstance();
                if (matcher.find()) {
                 String data = matcher.group("data");
                    image.setData(Base64.getDecoder().decode(data));

//                    image.setContentType("image/jpeg");

//                    image.setChapterImage(book);

                    return image;
                }
                return null;
            }

        } catch (IOException e) {
            System.out.println("Error about adding new image: " + e.getMessage());
            return null;
        }

        return null;
    }

    public static TextElements getParagraphImage(Element el, ChapterInstance current, BookInstance book, List<ImageInstance> images) {

        ImageInstance image = addChapterImage(el, book);

        if(image != null) {

            current.addChapterImage(image);

            images.add(image);

            TextElement element = formatText(el.attr("src", image.getFileName()), ElementType.Image);

            if (element != null) return new TextElements(List.of(element));
            else return null;
        }

        return null;
    }

    public static TextElement formatText(Element element, ElementType type) {
        return TextFormatter.parse(element, type);
    }

}

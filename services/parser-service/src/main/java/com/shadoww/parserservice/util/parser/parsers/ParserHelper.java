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
import com.shadoww.parserservice.util.instances.ImageInstance;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserHelper {


    private ParserHelper() {
    }


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

        // temp filename to indentify in future this image and his data
        image.setFileName(UUID.randomUUID() + ".jpeg");

        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();

        image.setContentType("image/jpeg");

        byte[] data = response.bodyAsBytes();

//        image.setData(response.bodyAsBytes());
        image.setData(data);

        return image;
    }

    public static List<ChapterInstance> addNumber(List<ChapterInstance> chapters) {
        for (int i = 1; i <= chapters.size(); i++) {
            chapters.get(i - 1).setChapterNumber(i);
        }

        return chapters;
    }


    public static ImageInstance addChapterImage(Element el) {
        try {
//            String src = "https://flibusta.site" + el.attr("src");
            String src = el.absUrl("src");

            System.out.println(src);
            if (src.startsWith("http")) {


                return new ImageInstance(parseImage(src));
            } else {
                Matcher matcher = Pattern.compile("base64,(?<data>.+)").matcher(src);

                ImageInstance image = new ImageInstance();
                if (matcher.find()) {
                    String data = matcher.group("data");
                    image.setData(Base64.getDecoder().decode(data));

                    return image;
                }

                throw new RuntimeException("Image wasn't found!");
            }

        } catch (IOException e) {
            System.out.println("Error about adding new image: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static TextElements getParagraphImage(Element el, ChapterInstance current, List<ImageInstance> images) {

        ImageInstance image = addChapterImage(el);

        current.addChapterImage(image);

        images.add(image);

        TextElement element = formatText(el.attr("src", image.getFileName()), ElementType.Image);

        return new TextElements(List.of(element));

    }

    public static TextElement formatText(Element element, ElementType type) {
        return TextFormatter.parseToPattern(element, type);
    }

}

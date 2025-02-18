package com.shadoww.parserservice;


import com.shadoww.parserservice.client.LibraryServiceClient;
import com.shadoww.parserservice.client.MediaServiceClient;
import com.shadoww.parserservice.util.formatters.BooksFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BooksFormatterParseFb2Tests {

    @Mock
    LibraryServiceClient libraryServiceClient;

    @Mock
    MediaServiceClient mediaServiceClient;

    @InjectMocks
    BooksFormatter booksFormatter;

    @Test
    public void testParseFb2Book() throws IOException, ParserConfigurationException {
//        String url = "http://loveread.ec/view_global.php?id=6972";
//        String url = "https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya";
        String url = "http://flibusta.site/b/792970";
//        String url = "http://loveread.ec/view_global.php?id=67498";
//        String url = "https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya";
//        String url = "http://flibusta.site/b/436734";
//        String url = "https://ancientrome.ru/antlitr/cicero/index-phil.htm";
//        String url = "http://flibusta.site/b/199800";


        String currentDate = getCurrentDateString();


//        System.out.println("test-%s".formatted(currentDate));
//        writeBookInstance("ФИЛОСОФСКИЕ_ТРАКТАТЫ_(PHILOSOPHIA).fb2", booksFormatter.parseToFb2(url));
//        writeBookInstance("test-book-%s.fb2".formatted(currentDate), booksFormatter.parseToFb2(url));
        writeBookInstance("osobisti_kordoni.fb2", booksFormatter.parseToFb2(url));
//        booksFormatter.parseToFb2(url);
    }

    @Test
    public void testParseFb2BookAndSaveImages() {
        try {
            // Завантажте FB2-файл
            Document doc = Jsoup.parse(new File("C:\\PROGRAMMING\\projects\\idea\\booklibrary-microservices-app\\services\\parser-service\\src\\test\\resources\\books\\test-book-2025-01-28-23-50-02.fb2"), "UTF-8");

            // Знайдіть усі елементи <binary>
            Elements binaries = doc.select("binary");

            // Переберіть кожен елемент <binary>
            for (Element binary : binaries) {
                // Отримайте атрибут id (він може бути використаний як ім'я файлу)
                String id = binary.attr("id");

                // Отримайте вміст елемента (закодоване зображення)
                String base64Data = binary.text();

                // Декодуйте Base64 до байтового масиву
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                // Визначте тип зображення (наприклад, "image/jpeg")
                String contentType = binary.attr("content-type");
                String extension = "";
                if ("image/jpeg".equals(contentType)) {
                    extension = ".jpg";
                } else if ("image/png".equals(contentType)) {
                    extension = ".png";
                } // Додайте інші типи за потреби

                // Збережіть зображення на диск
                try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream("src/test/resources/test_images/" + id, true))) {
                    fos.write(imageBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDateString() {
        // Отримуємо поточну дату і час
        LocalDateTime now = LocalDateTime.now();

        // Формат з дефісами
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

        // Виводимо результат
        return now.format(formatter);
    }

    private void writeBookInstance(String fileName, OutputStream outputStream) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/books/" + fileName, true))) {

            writer.write(outputStream.toString());
            System.out.println("Текст успішно додано до файлу: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Test
    public void testJsoupXml() throws IOException {
//        String filename = "";
//
//        InputStream is = new FileInputStream(filename) {
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        };


//        Jsoup.parse(is, "UTF-8", "", Parser.xmlParser());
        Document doc = Jsoup.parse("", Parser.xmlParser());

        Element element = doc.createElement("FictionBook");



        doc.appendChild(element);

        System.out.println(parseXml("sgsgsga<i>gshgshgus</i> <strong>sqhuhq</strong>"));
    }

    private Elements parseXml(String xmlStr) {
        Document doc = Jsoup.parse(xmlStr, Parser.xmlParser());

        return doc.select("*");
    }
}
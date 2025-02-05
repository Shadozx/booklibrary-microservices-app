package com.shadoww.parserservice.util.writers;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class BookFB2Writer {

    private final Document document;
    @Getter
    private final Element book;

    private final List<Element> images;

    public BookFB2Writer() throws ParserConfigurationException {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        book = document
                .createElement("FictionBook");
        book.setAttribute("xmlns", "http://www.gribuser.ru/xml/fictionbook/2.0");
        book.setAttribute("xmlns:l", "http://www.w3.org/1999/xlink");

        document.appendChild(book);

        images = new ArrayList<>();
    }


    public DescriptionBuilder description() throws ParserConfigurationException {
        return new DescriptionBuilder(this);
    }


    public BodyBuilder body() throws ParserConfigurationException {
        return new BodyBuilder(this);
    }


    private void addElement(Element element) {
        book.appendChild(element);
    }

    /**
     * title
     * description
     * <p>
     * body (
     * <p>
     * )
     */

    public static class DescriptionBuilder {

        private final BookFB2Writer writer;

        private final Element description;

        public DescriptionBuilder(BookFB2Writer writer) throws ParserConfigurationException {
            this.writer = writer;

            description = writer.getDocument().createElement("description");

//            writer.book.appendChild(description);
        }

        public TitleInfoBuilder titleInfo() {
            return new TitleInfoBuilder(writer, this);
        }

        private void addElement(Element element) {
            description.appendChild(element);
        }

        public static class TitleInfoBuilder {
            private BookFB2Writer writer;
            private DescriptionBuilder descriptionBuilder;

            private final Element titleInfo;

            public TitleInfoBuilder(BookFB2Writer writer, DescriptionBuilder descriptionBuilder) {
                this.writer = writer;
                this.descriptionBuilder = descriptionBuilder;

                titleInfo = writer.getBook().getOwnerDocument().createElement("title-info");
            }

            public TitleInfoBuilder title(String title) throws ParserConfigurationException {
//            this.title = title;

//                Element titleElement = writer.getDocument().createElement("title-info");


                Element bookTitleElement = writer.getDocument().createElement("book-title");
                bookTitleElement.appendChild(writer.getDocument().createTextNode(title));

                titleInfo.appendChild(bookTitleElement);

                return this;
            }

            public TitleInfoBuilder annotation(String annotation) throws ParserConfigurationException {
                Element annotationElement = writer.getDocument().createElement("annotation");
                Element paragraphElement = writer.getDocument().createElement("p");
                paragraphElement.appendChild(writer.getDocument().createTextNode(annotation));
                annotationElement.appendChild(paragraphElement);

                titleInfo.appendChild(annotationElement);

                return this;
            }

            public TitleInfoBuilder coverImage(byte[] data) throws ParserConfigurationException {

                Element image = writer.image("cover.jpg", data);

                Element coverPage = writer.getBook().getOwnerDocument().createElement("coverpage");

                coverPage.appendChild(image);

                titleInfo.appendChild(coverPage);

                return this;
            }


            public DescriptionBuilder back() {

                descriptionBuilder.addElement(titleInfo);

                return descriptionBuilder;
            }
        }

        public BookFB2Writer back() {
//            System.out.println(description);
            writer.addElement(description);

            return this.writer;
        }
    }

    public static class BodyBuilder {
        private final BookFB2Writer writer;

        private final Element body;

        public BodyBuilder(BookFB2Writer writer) throws ParserConfigurationException {
            this.writer = writer;

            body = writer.getDocument().createElement("body");
        }

        public BodyBuilder title(String title) throws ParserConfigurationException {
            Element elementTitle = writer.getDocument().createElement("title");
            Element elementParagraph = writer.getDocument().createElement("p");
            elementParagraph.appendChild(writer.getDocument().createTextNode(title));
            elementTitle.appendChild(elementParagraph);

            body.appendChild(elementTitle);

            return this;
        }

        private void addSection(Element element) {
            body.appendChild(element);
        }

        public SectionBuilder section() {

            return new SectionBuilder(this);
        }

        public BookFB2Writer back() {
            writer.addElement(body);

            return writer;
        }

        public static class SectionBuilder {
            private final BodyBuilder bodyBuilder;

            private final Element section;


            public SectionBuilder(BodyBuilder bodyBuilder) {
                this.bodyBuilder = bodyBuilder;

                section = bodyBuilder.body.getOwnerDocument().createElement("section");
            }

            public SectionBuilder title(String title) throws ParserConfigurationException {
                Document doc = bodyBuilder.getDocument();

                Element elementTitle = doc.createElement("title");
                Element elementParagraph = doc.createElement("p");
                elementParagraph.appendChild(doc.createTextNode(title));
                elementTitle.appendChild(elementParagraph);

                section.appendChild(elementTitle);

                return this;
            }

            public SectionBuilder paragraph(String text) throws ParserConfigurationException {
                Document doc = bodyBuilder.getDocument();
                Element paragraph = doc.createElement("p");

                //parse text for tags
                List<Node> nodes = parseTextWithTags(text, doc);

                for (Node node: nodes) {
                    paragraph.appendChild(node);
                }

                section.appendChild(paragraph);
                return this;

            }

            private List<Node> parseTextWithTags(String text, Document doc) throws ParserConfigurationException {
                List<Node> nodes = new ArrayList<>();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                try {
                    text = StringEscapeUtils.unescapeHtml(text);
                    String wrappedText = "<wrapper>" + text + "</wrapper>";
                    InputSource is = new InputSource(new StringReader(wrappedText));
                    Document tempDoc = builder.parse(is);

                    NodeList childNodes = tempDoc.getDocumentElement().getChildNodes();

                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node node = childNodes.item(i);
                        nodes.add(parseNode(node, doc));
                    }

                }
                catch(Exception e) {
                    nodes.add(doc.createTextNode(text));
                }
                return nodes;
            }

            private String sanitizeText(String text) {
                if (text == null || text.isEmpty()) {
                    return text;
                }

                // Мапа популярних сутностей і їх XML-безпечних замін
                Map<String, String> entityReplacements = Map.ofEntries(
                        Map.entry("&nbsp;", "-"),  // Нерозривний пробіл
                        Map.entry("&lt;", "<"),        // Менше
                        Map.entry("&gt;", ">"),        // Більше
                        Map.entry("&amp;", "&"),       // Амперсанд
                        Map.entry("&quot;", "\""),     // Подвійна лапка
                        Map.entry("&apos;", "'"),      // Одинарна лапка
                        Map.entry("&cent;", "¢"),      // Символ центу
                        Map.entry("&pound;", "£"),     // Символ фунта
                        Map.entry("&yen;", "¥"),       // Символ єни
                        Map.entry("&euro;", "€"),      // Символ євро
                        Map.entry("&copy;", "©"),      // Знак копірайту
                        Map.entry("&reg;", "®"),       // Знак зареєстрованої торгової марки
                        Map.entry("&shy", "")
                );

                // Заміна популярних сутностей
                for (Map.Entry<String, String> entry : entityReplacements.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }

                // Заміна сирих амперсандів, які не є частиною сутностей
                text = text.replaceAll("&(?!#\\d+;|#[xX][0-9a-fA-F]+;|[a-zA-Z]+;)", "&amp;");

                return text;
            }

            private Node parseNode(Node node, Document doc) {
                if (node.getNodeType() == Node.TEXT_NODE) {
                    return doc.createTextNode(node.getTextContent());
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    Element newElement = doc.createElement(el.getTagName());
                    NodeList childNodes = el.getChildNodes();

                    for(int i = 0; i < childNodes.getLength(); i++){
                        Node child = childNodes.item(i);
                        newElement.appendChild(parseNode(child, doc));
                    }
                    return newElement;
                }

                return doc.createTextNode("");
            }


//            public static String unescapeHtml(String text) {
//                return text
//                        .replace("&lt;", "<")
//                        .replace("&gt;", ">")
//                        .replace("&amp;", "&")
//                        .replace("&quot;", "\"")
//                        .replace("&apos;", "'");
//            }

            public SectionBuilder text(String text) throws ParserConfigurationException {
                Document doc = bodyBuilder.getDocument();

                section.appendChild(doc.createTextNode(text));

                return this;
            }

            public SectionBuilder image(String imageId, byte[] data) throws ParserConfigurationException {
//                Document doc = bodyBuilder.getDocument();

//                Element imageElement = doc.createElement("image");
                Element imageElement = bodyBuilder.writer.image(imageId, data);
//                imageElement.setAttribute("href", "#" + imageId);

//                bodyBuilder.writer.addBinaryImage(imageId, data);

                section.appendChild(imageElement);

                return this;
            }

//            private void addBinaryImage(String imageId, byte[] imageData) throws ParserConfigurationException {
//                Element book = bodyBuilder.writer.getBook();
//
//                Element binary = book.getOwnerDocument().createElement("binary");
//                binary.setAttribute("id", imageId);
//                binary.setAttribute("content-type", "image/png");
//
//                String base64Image = Base64.getEncoder().encodeToString(imageData);
//                binary.appendChild(book.getOwnerDocument().createTextNode(base64Image));
//
//                bodyBuilder.writer.book.appendChild(binary);
//            }

            public BodyBuilder and() {

                bodyBuilder.addSection(section);

                return bodyBuilder;
            }
        }

        private Document getDocument() throws ParserConfigurationException {
            return writer.getDocument();
        }

    }

    private Element image(String imageId, byte[] data) throws ParserConfigurationException {

        Element imageElement = document.createElement("image");
        imageElement.setAttribute("l:href", "#" + imageId);
//        System.out.println(imageElement);

        addBinaryImage(imageId, data);

        return imageElement;
    }

    private void addBinaryImage(String imageId, byte[] imageData) throws ParserConfigurationException {

        Element binary = book.getOwnerDocument().createElement("binary");
        binary.setAttribute("id", imageId);
        binary.setAttribute("content-type", "image/png");

        String base64Image = Base64.getEncoder().encodeToString(imageData);

        binary.appendChild(book.getOwnerDocument().createTextNode(base64Image));


        images.add(binary);

    }


    private Document getDocument() throws ParserConfigurationException {
        return document;
    }

    public Document document() {
        document.setXmlStandalone(true);

        saveImages();

        return document;
    }

    public ByteArrayOutputStream build() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {



            writeXml(document(), outputStream);

            return outputStream;
        } catch (TransformerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public OutputStream getDocument() {
//        saveImages();
//
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//            writeXml(document, outputStream);
//
//            return outputStream;
//        } catch (TransformerException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }

    private void saveImages() {

        for (var binary : images) {
            book.appendChild(binary);
        }
    }
}

package com.shadoww.parserservice.writers;

import com.shadoww.api.util.texformatters.TextFormatter;
import com.shadoww.api.util.texformatters.elements.TextElement;
import com.shadoww.api.util.texformatters.elements.TextElements;
import com.shadoww.api.util.texformatters.types.ElementType;
import com.shadoww.parserservice.util.parser.parsers.ParserHelper;
import com.shadoww.parserservice.util.writers.BookFB2Writer;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class BookFB2WriterTests {

    @Test
    public void createFB2File() throws ParserConfigurationException, IOException {
        BookFB2Writer writer = new BookFB2Writer();

        Resource imgFile = new ClassPathResource("images/image.png");

        System.out.println(imgFile.getFile().toPath());

        // Читаємо файл у масив байтів
        byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());


        writer.description()
                .titleInfo()
                .coverImage(imageBytes)
                .title("test book name")
                .annotation("test description")
                .back()
                .back();

        String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ante nunc, mattis ac erat sit amet, volutpat tincidunt lorem. In hac habitasse platea dictumst. Etiam dui neque, imperdiet non orci eu, feugiat vehicula purus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque quis nisi id mi rhoncus auctor. Etiam dignissim metus ac urna faucibus, ac finibus felis pulvinar. Duis condimentum lobortis risus. Cras consectetur, sapien vel fermentum luctus, massa lacus pulvinar felis, at sodales purus turpis nec quam. Maecenas ac mattis urna, a pretium purus. Sed arcu nisi, lobortis finibus sapien nec, pulvinar sodales est. Proin aliquam bibendum lacus, vitae rutrum eros blandit in. Quisque pharetra vitae lectus non sodales. Sed faucibus ultrices magna, pharetra dapibus purus pellentesque ac. Integer dictum pulvinar quam id dignissim.
                Aliquam porttitor nisi quis enim molestie, sit amet gravida diam tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Interdum et malesuada fames ac ante ipsum primis in faucibus. Aenean quam massa, tincidunt vel viverra ac, consequat ut justo. Integer ut vehicula sapien. Nullam tempus non libero id laoreet. Curabitur elementum leo sed ex ultricies, in imperdiet diam sagittis. Maecenas pharetra congue lectus eu auctor. Proin vel erat suscipit, convallis felis vel, dictum libero. Aenean placerat nunc mauris. Donec nibh turpis, sodales quis commodo in, varius id sapien. Nam in ligula sem.
                Nam orci enim, auctor at odio vitae, laoreet tempus eros. Nam at mi iaculis, vestibulum mi non, sagittis quam. Curabitur sed hendrerit justo, ut accumsan risus. Vivamus vitae pulvinar diam. Cras eu interdum odio. Vestibulum id elit erat. Nulla et malesuada nulla.                
                Vivamus viverra nisi accumsan sem auctor, sed iaculis risus congue. Fusce vehicula lacus id felis dictum tristique. Praesent maximus neque id ligula efficitur, fringilla tristique ex gravida. Maecenas mauris felis, consequat sed fermentum laoreet, faucibus vitae nisi. Proin id elit nec dolor tempor semper. Integer sit amet sapien efficitur urna tincidunt posuere. Aliquam eget pharetra eros. Morbi maximus dapibus mi sed efficitur.
                Aliquam id nisi vel velit porta venenatis a vestibulum metus. Suspendisse vehicula leo et lacus feugiat, nec condimentum urna aliquam. Sed lobortis velit eget dolor rutrum, quis rhoncus ligula fermentum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Cras consequat venenatis elit, et sagittis sem pharetra quis. Phasellus non lobortis justo. Praesent eu metus et nisl pellentesque lobortis.
                """;

        String text1 = """
                <a name="002"><span id="selection_index4" class="selection_index"></span>2.</a> Тогда я гово­рю ему: <font class="kav">«</font>Чест­ное сло­во, я не имею обык­но­ве­ния высту­пать про­тив сто­и­ков без под­готов­ки, не пото­му, чтобы я согла­шал­ся с ними во всем, но мне меша­ет совесть, ибо я мно­го­го не пони­маю из того, что они гово­рят<font class="kav">»</font>. <font class="kav">«</font>Согла­сен,&nbsp;— гово­рит он,&nbsp;— кое-что у них дей­ст­ви­тель­но тем­но. Одна­ко они не нароч­но гово­рят таким обра­зом, но неяс­ность при­су­ща само­му пред­ме­ту<font class="kav">»</font><sup><a href="#n3" name="t3" onmouseover="changetext(content[3])" onmousemove="move(event)" onmouseout="out()">3</a></sup>. <font class="kav">«</font>Так поче­му же,&nbsp;— отве­чаю,&nbsp;— когда о том же самом гово­рят пери­па­те­ти­ки, у них нет ни еди­но­го сло­ва, кото­ро­го я бы не понял?<font class="kav">»</font> <font class="kav">«</font>О том ли самом?&nbsp;— гово­рит он.&nbsp;— Или я мало гово­рил о том, что сто­и­ки отли­ча­ют­ся от пери­па­те­ти­ков не в сло­вах, а всем сво­им уче­ни­ем?<font class="kav">»</font> <font class="kav">«</font>Ну что же, Катон,&nbsp;— гово­рю я,&nbsp;— если ты это дока­жешь, смо­жешь пере­тя­нуть меня цели­ком на свою сто­ро­ну<font class="kav">»</font>. <font class="kav">«</font>Я, меж­ду про­чим, пола­гал,&nbsp;— гово­рит он,&nbsp;— что ска­зал вполне доста­точ­но. А поэто­му, если не воз­ра­жа­ешь, обра­тим­ся сна­ча­ла к само­му вопро­су; ну а если воз­никнет что-то дру­гое, об этом пого­во­рим позд­нее<font class="kav">»</font>. <font class="kav">«</font>Наобо­рот,&nbsp;— отве­чаю,&nbsp;— будем [рас­смат­ри­вать] каж­дый [вопрос] на сво­ем месте по мое­му усмот­ре­нию (ar­bit­ra­tu meo)… Если толь­ко ты не воз­ра­жа­ешь<font class="kav">»</font>. <font class="kav">«</font>Как тебе угод­но,&nbsp;— отве­ча­ет он,&nbsp;— хотя мой порядок был удоб­нее, спра­вед­ли­вость тре­бу­ет усту­пать<font class="kav">»</font>.
                """;
        writer
                .body()
                .section()
                .title("title name")
                .paragraph("text new <i>text</i> ргірпгіпі <b>strong text</b> shusgus aa <strong>sgisg</strong> <div>faijfiafaifja <i>afjaifa</i>aafaif</div>")
                .image("i_002.png", imageBytes)

                .and()
                .section()
                .title("title with image")
                .image("i_001.png", imageBytes)
                .paragraph("<i>image</i>")
                .paragraph("So now this is <i>cursive</i> and <b>bord</b>")
                .and()
                .section()
                .title("Lorem Ipsum")
                .paragraph(text1)
                .and()
                .back();

        Element book = writer.getBook();

        System.out.println(book);

        Path folder = Paths.get("test-fb2");
        System.out.println(folder);

        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Path filePath = folder.resolve("test-book.fb2");
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toString())) {
            ByteArrayOutputStream outputStream = writer.build();
            outputStream.writeTo(fileOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            writeXml(writer.document(), outputStream);
//
//            System.out.println(outputStream);
//        } catch (IOException | TransformerException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Test
    public void testFormatText() {
        String text = "Фаларид (прибл. 570–554&nbsp;гг. до н.&nbsp;э.)&nbsp;— тиран г. Акраганта, славившийся своей жестокостью II, 4.";


//        org.jsoup.nodes.Document doc = Jsoup.parse("<wrapper>"+text+"</wrapper>", Parser.xmlParser());
//        doc.outputSettings().escapeMode(Entities.EscapeMode.base);
//        doc.outputSettings().charset(CharEncoding.UTF_16);
//
//        System.out.println(doc);

        System.out.println(StringEscapeUtils.unescapeHtml(text));


//        text = sanitizeText(text);

//        System.out.println(text);
    }

    @Test
    public void testTextFormatter() {
        System.out.println(TextFormatter.parse(new org.jsoup.nodes.Element("p"), ElementType.Paragraph));
    }
    @Test
    public void testParseHtmlElement() {
        String text = """
                <div class="otext">
                
                <div class="cit"><span class="z3"></span><a name="1"></a><strong><span id="selection_index4" class="selection_index"></span>А</strong>бори­ге­ны Г II, 5<p></p>

                <p><span id="selection_index5" class="selection_index"></span>Авен­тин (холм в Риме) Г II, 33, 58, 63</p>

                <p><span id="selection_index6" class="selection_index"></span>Авре­лий Кот­та, Луций, кон­сул 65&nbsp;г., цен­зор 64&nbsp;г. З III, 45</p>

                <p><span id="selection_index7" class="selection_index"></span>Ага­ла см. Сер­ви­лий Ага­ла</p>

                <p><span id="selection_index8" class="selection_index"></span>Агри­гент (Акра­гант) Г III, 45</p>

                <p><span id="selection_index9" class="selection_index"></span>Азия Малая Г, II, 9; III, 41; VI, 11</p>

                <p><span id="selection_index10" class="selection_index"></span>Ака­де­мия Новая З I, 39</p>

                <p><span id="selection_index11" class="selection_index"></span>Ака­де­мия Ста­рая З I, 38, 53 слл.</p>

                <p><span id="selection_index12" class="selection_index"></span>Акви­лий, Маний, кон­сул 129&nbsp;г., Г I, 14</p>

                <p><span id="selection_index13" class="selection_index"></span>Акви­лон (греч. Борей) З I, 3</p>

                <p><span id="selection_index14" class="selection_index"></span>Акра­гант см. Агри­гент</p>

                <p><span id="selection_index15" class="selection_index"></span>Аксин­ский Понт Г III, 15</p>

                <p><span id="selection_index16" class="selection_index"></span>Акций, Луций, тра­ги­че­ский поэт, З II, 54</p>

                <p><span id="selection_index17" class="selection_index"></span>Аль­ба Лон­га, город Г II, 4</p>

                <p><span id="selection_index18" class="selection_index"></span>Аль­гид, гора Г II, 63</p>

                <p><span id="selection_index19" class="selection_index"></span>Алек­сандр Вели­кий (356—<wbr>323) Г III, 15; З II, 41</p>

                <p><span id="selection_index20" class="selection_index"></span>Амаль­фей З II, 7</p>

                <p><span id="selection_index21" class="selection_index"></span>Ампий Бальб, Тит, пле­бей­ский три­бун 63&nbsp;г., З II, 6</p>

                <p><span id="selection_index22" class="selection_index"></span>Амфи­а­рай З II, 33</p>

                <p><span id="selection_index23" class="selection_index"></span>Аму­лий, царь Г II, 4</p>

                <p><span id="selection_index24" class="selection_index"></span>Ана­к­са­гор, фило­соф (500—<wbr>428) Г I, 25</p>

                <p><span id="selection_index25" class="selection_index"></span>Ана­к­си­мен, фило­соф Г I, 51</p>

                <p><span id="selection_index58" class="selection_index"></span>Аци­лий, Луций, юрист З II, 59</p>
                <br>
                <p><a name="2"></a><strong><span id="selection_index59" class="selection_index"></span>Б</strong>лаго­че­стие, боже­ство З II, 19, 28</p>

                </div>
                </div>
                """;//.replaceAll("\n", "");

//        String text = """
//                <div class="otext">
//                <p></p>
//                <div class="note"><span class="z3"></span><span id="selection_index4" class="selection_index"></span>Пере­вод с латин­ско­го сде­лан по изда­ни­ям: диа­лог <font class="kav">«</font>О государ­стве<font class="kav">»</font>: M. Tul­lius<font class="pr2"> Ci­ce­ro.</font> De re pub­li­ca. Bib­lio­the­ca Teub­ne­ria­na (K. Zieg­ler). Lip­siae, 1958; M. Tul­lius<font class="pr2"> Ci­ce­ro.</font> Vom Ge­meinwe­sen. La­tei­ni­sch und deutsch (K. Büch­ner). Zü­rich, 1960; диа­лог <font class="kav">«</font>О зако­нах<font class="kav">»</font>:<font class="pr2"> Ci­cé­ron.</font> Trai­té des lois. Tex­te étab­li et tra­duit par G. de Plin­val. Col­lec­tion Bu­dé. Pa­ris, 1958. В при­ме­ча­ни­ях ссыл­ки на антич­ную лите­ра­ту­ру дают­ся по пара­гра­фам; хро­но­ло­ги­че­ские даты&nbsp;— до нашей эры. Сти­хи пере­веде­ны <nobr>В.&nbsp;О.</nobr> Горен­штей­ном, кро­ме слу­ча­ев, ого­во­рен­ных осо­бо. При ссыл­ках на пись­ма Цице­ро­на ука­зы­ва­ет­ся, поми­мо обще­при­ня­тых дан­ных, номер пись­ма по изда­нию:<font class="pr2"> М. Тул­лий Цице­рон.</font> Пись­ма к Атти­ку, близ­ким, бра­ту Квин­ту, М. Бру­ту. Пере­вод и ком­мен­та­рии <nobr>В.&nbsp;О.</nobr> Горен­штей­на, т. I—<wbr>III, М.—<wbr>Л., Изд. АН СССР, 1949—<wbr>1951.<p></p></div>
//                <p><i><span id="selection_index61" class="selection_index"></span>ЛЕЛИЙ</i>.&nbsp;— …к роду его отца при­над­ле­жал наш извест­ный друг, достой­ный под­ра­жа­ния,&nbsp;—<wbr></p>
//                <p>(<a name="15"><span id="selection_index51" class="selection_index"></span>XV</a>, <a name="023">23</a>) <i>СЦИПИОН</i>.&nbsp;— …так как я и сам почи­тал это­го чело­ве­ка, и знал, что отец мой Павел<sup><a href="#n66" name="t66" onmouseover="changetext(content[66])" onmousemove="move(event)" onmouseout="out()">66</a></sup> осо­бен­но ува­жал и любил его. Пом­нит­ся, в моей ран­ней юно­сти, когда мой отец, в то вре­мя кон­сул, нахо­дил­ся в Македо­нии и мы сто­я­ли лаге­рем, наше вой­ско было охва­че­но суе­вер­ным стра­хом вслед­ст­вие того, что в ясную ночь яркая и пол­ная луна вдруг затми­лась. Тогда Галл, быв­ший нашим лега­том&nbsp;— при­бли­зи­тель­но за год до того, как его избра­ли в кон­су­лы,&nbsp;— на дру­гой день не поко­ле­бал­ся во все­услы­ша­ние объ­явить, что это вовсе не было зна­ме­ни­ем и про­изо­шло и все­гда будет про­ис­хо­дить через опре­де­лен­ное вре­мя&nbsp;— тогда, когда солн­це ока­жет­ся в таком месте, что его свет не смо­жет достиг­нуть луны.</p>
//                <table align="center" cellspacing="0" cellpadding="0" border="0"><tbody><tr><td valign="top" class="cit"><span id="selection_index64" class="selection_index"></span>Аст­ро­ло­гов зна­ки в небе, смысл их он понять жела­ет:<br><span id="selection_index65" class="selection_index"></span>Капра, Скор­пи­он вос­хо­дят, с ними и дру­гие зве­ри.<br><span id="selection_index66" class="selection_index"></span>Что<font class="ud">́</font> у ног, никто не видит, ози­ра­ет стра­ны неба.</td></tr></tbody></table>
//                </div>
//                """;

        org.jsoup.nodes.Element body = Jsoup.parse(text).body();
//        Elements elements = body.select("span, a").
//        org.jsoup.nodes.Element el = body.select("> *").first();
        Elements elements = body.select(".otext > *");

        TextElements textElements = new TextElements();

        for(var e : elements) {
            TextElements processedElements = getElements(e);

            textElements.addAll(processedElements);
        }

        System.out.println(textElements);

//        org.jsoup.nodes.Element el = body.select(".otext>*").first();

//        System.out.println(getElements(el));

    }

    private String getCurrentDateString() {
        // Отримуємо поточну дату і час
        LocalDateTime now = LocalDateTime.now();

        // Формат з дефісами
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        // Виводимо результат
        return now.format(formatter);
    }

    private TextElements getElements(org.jsoup.nodes.Element element) {


        TextElements elements = new TextElements();
        StringBuilder aggregatedText = new StringBuilder(); // Для тексту не-параграфних елементів

//        if (element.html().equals("")) {
//            return new TextElements(List.of(ParserHelper.formatText(element.text(element.text()), ElementType.Paragraph)));
//        }

        if (element.tagName().equals("div")) {
            // Якщо елемент є контейнером, обробити його дочірні елементи
            for (var node : element.childNodes()) {
//                if (node.toString().equals("")) {
//
////                    System.out.println(node);
//                }
                 if (node instanceof org.jsoup.nodes.Element childElement) {
                    String tagName = childElement.tagName();

//                    System.out.println(node);
                    if (tagName.equals("p")) {
                        // Додати накопичений текст як новий параграф перед обробкою <p>

                        if (!aggregatedText.isEmpty()) {

                            elements.add(ParserHelper.formatText(new org.jsoup.nodes.Element("p").text(aggregatedText.toString().trim()), ElementType.Paragraph));
                            aggregatedText.setLength(0);
                        }
                        // Обробити <p>
                        if(!childElement.html().equals("")) {
                            elements.add(ParserHelper.formatText(childElement.text(childElement.text().trim()), ElementType.Paragraph));
                        }
                    } else {
                        // Збирати текст із дочірніх елементів, які не є <p>
                        aggregatedText.append(childElement.text());
                    }
                } else if (node instanceof org.jsoup.nodes.TextNode textNode) {
                    // Додати текст із текстових вузлів
                    aggregatedText.append(textNode.text());
                }
            }
        } else if (element.tagName().equals("table")) {
            if (!element.html().equals("")) {
                org.jsoup.nodes.Element paragraphElement = new org.jsoup.nodes.Element("p");
                paragraphElement.appendChild(new org.jsoup.nodes.Element("i").text(element.text()));

                elements.addAll(new TextElements(List.of(ParserHelper.formatText(paragraphElement, ElementType.Paragraph))));

            }
        }

        else {
            // Для інших типів елементів просто додати їх текст
            if(!element.html().equals("")) {
                aggregatedText.append(element.text()).append(" ");
            }
        }

        // Додати зібраний текст, якщо залишився після обробки
        if (aggregatedText.length() > 0) {
            elements.add(ParserHelper.formatText(new org.jsoup.nodes.Element("p").text(aggregatedText.toString().trim()), ElementType.Paragraph));
        }

        return elements;
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

    // write doc to output stream
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }


}

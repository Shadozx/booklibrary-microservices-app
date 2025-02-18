package com.shadoww.parserservice.util.parser.statistics;

import org.jsoup.nodes.Element;

import java.util.*;

// Клас для збору статистики парсера
public class ChapterParserStatistics {
    private int pagesParsed = 0;
    private int elementsProcessed = 0;
    private Map<String, Integer> elementCounts = new TreeMap<>();

    public void addPageParsed() {
        pagesParsed++;
    }

    /**
     * Додає елемент і рекурсивно його дочірні елементи до статистики.
     */
    public void addElement(Element el) {
        addElementRecursive(el);
    }

    /**
     * Рекурсивно обробляє елемент і всі його дочірні елементи.
     */
    private void addElementRecursive(Element el) {
        // Отримуємо рядок-ідентифікатор, який включає інформацію про батька (якщо є) і сам елемент.
        String fullIdentifier = getFullIdentifier(el);
        elementCounts.put(fullIdentifier, elementCounts.getOrDefault(fullIdentifier, 0) + 1);
        elementsProcessed++;

        // Рекурсивно обходимо всі дочірні елементи
        for (Element child : el.children()) {
            addElementRecursive(child);
        }
    }

    /**
     * Формує рядок-ідентифікатор у форматі "батько > елемент".
     * Якщо батьківського елемента немає, повертає лише ідентифікатор поточного елемента.
     */
    private String getFullIdentifier(Element el) {
        Element parent = el.parent();
        if (parent != null) {
            return getSimpleIdentifier(parent) + " > " + getSimpleIdentifier(el);
        } else {
            return getSimpleIdentifier(el);
        }
    }

    /**
     * Формує простий ідентифікатор елемента у форматі: tag#id.class1.class2.
     */
    private String getSimpleIdentifier(Element el) {
        String identifier = el.tagName();
        if (el.hasAttr("id") && !el.attr("id").isEmpty()) {
            identifier += "#" + el.attr("id");
        }
        if (el.hasAttr("class") && !el.className().isEmpty()) {
            List<String> classes = new ArrayList<>(el.classNames());
            Collections.sort(classes); // Для стабільності порядку
            identifier += "." + String.join(".", classes);
        }
        return identifier;
    }

    public int getPagesParsed() {
        return pagesParsed;
    }

    public int getElementsProcessed() {
        return elementsProcessed;
    }

    public Map<String, Integer> getElementCounts() {
        return elementCounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParserStatistics:\n");
        sb.append("Pages parsed: ").append(pagesParsed).append("\n");
        sb.append("Elements processed: ").append(elementsProcessed).append("\n");
        sb.append("Element counts:\n");

        elementCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        sb.append("  \"").append(entry.getKey())
                                .append("\" : ").append(entry.getValue()).append("\n")
                );

        return sb.toString();
    }
}


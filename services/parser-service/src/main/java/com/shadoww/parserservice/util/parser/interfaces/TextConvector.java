package com.shadoww.parserservice.util.parser.interfaces;

import com.shadoww.parserservice.util.instances.ChapterInstance;
import org.jsoup.nodes.Element;

import java.util.Stack;

public interface TextConvector {

    void transform(Stack<ChapterInstance> chapterInstances, Element el);
}

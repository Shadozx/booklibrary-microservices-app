package com.shadoww.libraryservice.mapper.imp;

import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.libraryservice.mapper.ChapterMapper;
import com.shadoww.libraryservice.model.Chapter;
import org.springframework.stereotype.Component;

@Component
public class ChapterMapperImpl implements ChapterMapper {

    @Override
    public ChapterRequest dtoToRequest(Chapter chapter) {
        ChapterRequest request = new ChapterRequest();

        request.setTitle(chapter.getTitle());
        request.setPatternText(chapter.getText());
        request.setChapterNumber(chapter.getChapterNumber());

        return request;
    }

    @Override
    public Chapter dtoToModel(ChapterRequest request) {
        Chapter chapter = new Chapter();

        chapter.setTitle(request.getTitle());
        chapter.setText(request.getPatternText());
        chapter.setChapterNumber(request.getChapterNumber());

        return chapter;
    }

    @Override
    public ChapterResponse dtoToResponse(Chapter chapter) {

        return new ChapterResponse(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getText(),
                chapter.getBook().getId(),
                chapter.getChapterNumber()
        );
    }

    @Override
    public Chapter dtoToModel(ChapterResponse response) {

        Chapter chapter = new Chapter();

        chapter.setTitle(response.getTitle());
        chapter.setText(response.getText());
        chapter.setChapterNumber(response.getChapterNumber());

        return chapter;
    }
}
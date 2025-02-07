package com.shadoww.libraryservice.mapper;

import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.libraryservice.model.Chapter;

//@Mapper
public interface ChapterMapper {

    ChapterRequest dtoToRequest(Chapter chapter);

    Chapter dtoToModel(ChapterRequest request);

    ChapterResponse dtoToResponse(Chapter chapter);

    Chapter dtoToModel(ChapterResponse response);
}

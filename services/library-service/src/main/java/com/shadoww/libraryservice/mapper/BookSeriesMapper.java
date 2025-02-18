package com.shadoww.libraryservice.mapper;

import com.shadoww.api.dto.request.BookSeriesRequest;
import com.shadoww.api.dto.response.BookSeriesResponse;
import com.shadoww.libraryservice.model.BookSeries;

public interface BookSeriesMapper {

    BookSeriesRequest dtoToRequest(BookSeries series);

    BookSeries dtoToModel(BookSeriesRequest request);

    BookSeriesResponse dtoToResponse(BookSeries series);

    BookSeries dtoToModel(BookSeriesResponse response);
}

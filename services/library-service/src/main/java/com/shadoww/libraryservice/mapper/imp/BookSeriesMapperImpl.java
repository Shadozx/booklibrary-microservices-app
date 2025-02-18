package com.shadoww.libraryservice.mapper.imp;

import com.shadoww.api.dto.request.BookSeriesRequest;
import com.shadoww.api.dto.response.BookSeriesResponse;
import com.shadoww.libraryservice.mapper.BookSeriesMapper;
import com.shadoww.libraryservice.model.BookSeries;
import org.springframework.stereotype.Component;

@Component
public class BookSeriesMapperImpl implements BookSeriesMapper {


    @Override
    public BookSeriesRequest dtoToRequest(BookSeries series) {
        BookSeriesRequest request = new BookSeriesRequest();
        request.setTitle(series.getTitle());
        request.setDescription(series.getDescription());

        return request;
    }

    @Override
    public BookSeries dtoToModel(BookSeriesRequest request) {
        BookSeries series = new BookSeries();

        series.setTitle(request.getTitle());
        series.setDescription(request.getDescription());

        return series;
    }

    @Override
    public BookSeriesResponse dtoToResponse(BookSeries series) {
        return new BookSeriesResponse(
                series.getId(),
                series.getTitle(),
                series.getDescription()
                );
    }

    @Override
    public BookSeries dtoToModel(BookSeriesResponse response) {

        BookSeries series = new BookSeries();

        series.setId(response.getId());
        series.setTitle(response.getTitle());
        series.setDescription(response.getDescription());

        return series;
    }
}

package com.shadoww.api.dto.response;

//import com.shadoww.BookLibraryApp.model.BookSeries;
import lombok.Value;

@Value
public class BookSeriesResponse {

    long id;

    String title;

    String description;

}

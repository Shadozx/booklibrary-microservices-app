package com.shadoww.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

//@Value
//@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class ExceptionResponse {

    //    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")  // Вказуємо формат без зони часу
    LocalDateTime timestamp;
    //    @JsonProperty("status")
    int status;
    //    @JsonProperty("error")
    String error;
    //    @JsonProperty("message")
    String message;
    //    @JsonProperty("path")
    String path;
}

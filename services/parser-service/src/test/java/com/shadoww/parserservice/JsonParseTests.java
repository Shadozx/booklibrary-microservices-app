package com.shadoww.parserservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shadoww.exception.ExceptionResponse;
import org.junit.jupiter.api.Test;

public class JsonParseTests {

    @Test
    public void parseJson() throws JsonProcessingException {
        String json= "{\"timestamp\":\"2024-08-25T17:34:11.0559225\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Немає такої фотографії\",\"path\":\"uri=/api/media/img/20\"}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ExceptionResponse response = mapper.readValue(json, ExceptionResponse.class);

        System.out.println(response);
    }
}

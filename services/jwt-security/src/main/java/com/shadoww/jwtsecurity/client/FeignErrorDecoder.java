package com.shadoww.jwtsecurity.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.exception.ExceptionResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

//        if (response.status() < 400) {
//            throw new Exception("Відповідь немає тіла помилки");
//        }

        System.out.printf("Method key: %s, Response text: %s, status: %d", methodKey, response.body().toString(), response.status());

        try {

            byte[] body = response.body().asInputStream().readAllBytes();

//            System.out.println(new String(body, StandardCharsets.UTF_8));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ExceptionResponse exceptionResponse = mapper.readValue(new String(body, StandardCharsets.UTF_8), ExceptionResponse.class);

            return new Exception(exceptionResponse.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
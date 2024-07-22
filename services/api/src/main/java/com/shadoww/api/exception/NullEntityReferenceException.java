package com.shadoww.api.exception;

import com.shadoww.api.dto.response.AuthorResponse;

public class NullEntityReferenceException extends RuntimeException {
    public NullEntityReferenceException(String message) {

        super(message);
    }
}

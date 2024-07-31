package com.shadoww.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AuthorRequest {

    private String name;

    private String biography;

    @Override
    public String toString() {
        return "BookRequest{" +
                "name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}


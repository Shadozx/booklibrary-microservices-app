package com.shadoww.api.dto.request.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ParseLinkRequest {

    @JsonProperty("url")
    String url;
}

package com.shadoww.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ImageRequest {

    private String fileName;

    private byte[] data;
}

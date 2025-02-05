package com.shadoww.parserservice.util.instances;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImageInstance {

    private String fileName;
    private byte[] data;

    private String contentType = "image/jpeg";


    public ImageInstance(ImageInstance instance) {
        this.fileName = instance.getFileName();
        this.data = instance.getData();
        this.contentType = instance.getContentType();
    }

    @Override
    public String toString() {
        return "ImageInstance{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}

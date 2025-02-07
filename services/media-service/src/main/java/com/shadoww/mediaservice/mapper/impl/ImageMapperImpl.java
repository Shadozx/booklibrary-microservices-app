package com.shadoww.mediaservice.mapper.impl;

import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.mediaservice.mapper.ImageMapper;
import com.shadoww.mediaservice.model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper {
    @Override
    public ImageRequest dtoToRequest(Image image) {
        ImageRequest request = new ImageRequest();

        request.setFileName(image.getFileName());
        request.setData(request.getData());

        return request;
    }

    @Override
    public Image dtoToModel(ImageRequest request) {
        Image image = new Image();

        image.setId(null);
        image.setData(request.getData());

        return image;
    }

    @Override
    public ImageResponse dtoToResponse(Image image) {
        return new ImageResponse(image.getId(), image.getFileName());
    }

    @Override
    public Image dtoToModel(ImageResponse response) {
        Image image = new Image();

        image.setId(response.getId());
        image.setFileName(response.getFileName());

        return image;
    }
}
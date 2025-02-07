package com.shadoww.mediaservice.mapper;

import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.mediaservice.model.Image;
import org.mapstruct.Mapper;

@Mapper
public interface ImageMapper {

    ImageRequest dtoToRequest(Image image);

    Image dtoToModel(ImageRequest request);

    ImageResponse dtoToResponse(Image image);
//
    Image dtoToModel(ImageResponse response);
}

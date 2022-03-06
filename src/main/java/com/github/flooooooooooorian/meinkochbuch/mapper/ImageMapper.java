package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;

public interface ImageMapper {

    static ImageDto imageToImageDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .build();
    }
}

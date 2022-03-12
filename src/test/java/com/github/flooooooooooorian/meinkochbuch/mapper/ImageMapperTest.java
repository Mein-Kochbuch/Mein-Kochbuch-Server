package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageMapperTest {

    @Test
    void imageToImageDto() {
        //GIVEN
        Image image = Image.builder()
                .id("test-id")
                .owner(ChefUser.ofId("test-user-id"))
                .build();
        //WHEN
        ImageDto result = ImageMapper.imageToImageDto(image);

        //THEN
        ImageDto expected = ImageDto.builder()
                .id("test-id")
                .build();

        assertEquals(expected, result);
    }
}
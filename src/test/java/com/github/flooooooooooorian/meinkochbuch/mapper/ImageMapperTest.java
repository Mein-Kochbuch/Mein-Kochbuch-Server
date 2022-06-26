package com.github.flooooooooooorian.meinkochbuch.mapper;

import com.github.flooooooooooorian.meinkochbuch.dtos.ImageDto;
import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.AwsS3Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ImageMapperTest {

    private final AwsS3Service mockS3Service = mock(AwsS3Service.class);
    private final ImageMapper imageMapper = new ImageMapper(mockS3Service);

    @Test
    void imageToImageDto() {
        //GIVEN
        Image image = Image.builder()
                .id("test-id")
                .key("test-key")
                .owner(ChefUser.ofId("test-user-id"))
                .build();

        when(mockS3Service.getPreSignedImageUrl("test-key")).thenReturn("presigned-img-url");
        //WHEN
        ImageDto result = imageMapper.imageToImageDto(image);

        //THEN
        ImageDto expected = ImageDto.builder()
                .id("test-id")
                .url("presigned-img-url")
                .build();

        assertEquals(expected, result);
    }

    @Test
    void imageToThumbnailDto() {
        //GIVEN
        Image image = Image.builder()
                .id("test-id")
                .key("test-key")
                .owner(ChefUser.ofId("test-user-id"))
                .build();

        when(mockS3Service.getPreSignedThumbnailUrl("test-key")).thenReturn("presigned-img-url");
        //WHEN
        ImageDto result = imageMapper.imageToThumbnailDto(image);

        //THEN
        ImageDto expected = ImageDto.builder()
                .id("test-id")
                .url("presigned-img-url")
                .build();

        assertEquals(expected, result);
    }
}
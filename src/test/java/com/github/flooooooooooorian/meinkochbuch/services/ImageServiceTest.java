package com.github.flooooooooooorian.meinkochbuch.services;

import com.github.flooooooooooorian.meinkochbuch.models.image.Image;
import com.github.flooooooooooorian.meinkochbuch.repository.ImageRepository;
import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.services.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private final AwsS3Service awsS3Service = mock(AwsS3Service.class);
    private final ImageRepository imageRepository = mock(ImageRepository.class);
    private final IdUtils idUtils = mock(IdUtils.class);
    private final ImageService imageService = new ImageService(awsS3Service, imageRepository, idUtils);

    @Test
    void addImage() {
        //GIVEN
        Image expected = Image.builder()
                .id("test_image_id")
                .key("test_image_key")
                .owner(ChefUser.ofId("userId"))
                .build();

        when(awsS3Service.uploadImage(Mockito.any())).thenReturn("test_image_key");
        when(idUtils.generateId()).thenReturn("test_image_id");
        when(imageRepository.save(expected)).thenReturn(expected);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test-file.jpg".getBytes(StandardCharsets.UTF_8));

        //WHEN
        Image actual = imageService.addImage("userId", multipartFile);

        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void deleteImageExisting() {
        //GIVEN
        when(imageRepository.findById("test_id")).thenReturn(Optional.of(Image.builder()
                .id("test_id")
                .owner(ChefUser.ofId("userId"))
                .key("test_url")
                .build()));

        //WHEN
        imageService.deleteImage("userId", "test_id");

        //THEN
        verify(imageRepository).deleteById("test_id");
    }

    @Test
    void deleteImageNotAllowed() {
        //GIVEN
        when(imageRepository.findById("test_id")).thenReturn(Optional.of(Image.builder()
                .id("test_id")
                .key("test_url")
                .owner(ChefUser.ofId("wrong_id"))
                .build()));

        //WHEN
        //THEN
        assertThrows(RuntimeException.class, () -> imageService.deleteImage("user_id", "test_id"));
    }

    @Test
    void deleteImageNonExisting() {
        //GIVEN
        when(imageRepository.findById("test_id")).thenReturn(Optional.empty());

        //WHEN
        //THEN
        assertThrows(RuntimeException.class, () -> imageService.deleteImage("user_id", "test_id"));
    }
}